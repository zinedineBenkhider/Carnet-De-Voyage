package com.example.android.carnetDeVoyage.fragments;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;

import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.LayoutInflater;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.carnetDeVoyage.activities.noteActivities.ListPicturesActivity;
import com.example.android.carnetDeVoyage.activities.noteActivities.TakePictureNoteActivity;
import com.example.android.carnetDeVoyage.activities.tripActivities.NewTripActivity;
import com.example.android.carnetDeVoyage.adapters.ListNotePictureAdapter;
import com.example.android.carnetDeVoyage.adapters.ListTripAdapter;
import com.example.android.carnetDeVoyage.adapters.NotePictureForAdapter;
import com.example.android.carnetDeVoyage.adapters.TripForAdapter;
import com.example.android.carnetDeVoyage.entities.Trip;

import com.example.android.carnetDeVoyage.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ListTripFragment extends Fragment implements View.OnCreateContextMenuListener{


    public static ListTripAdapter itemsAdapter;
    public static ArrayAdapter<String> idsAdapter;
    ListTripFragmentViewModel mViewModel;
    public static int NEW_TRIP_ACTIVITY_REQUEST_CODE=17;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ListTripFragmentViewModel.class);
        idsAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,mViewModel.getListIdOfTrips());
        itemsAdapter = new ListTripAdapter(getContext(),mViewModel.getListAllItems());
        mViewModel.observeListTrip(getActivity());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Inflate the xml file for the fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_list_trip, parent, false);
    }
    private OnItemSelectedListener listener;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final ListView lvItems =  view.findViewById(R.id.lvItems);
        registerForContextMenu(lvItems);//pour le menu qui s'affiche lorsque un long click sur un voyage est produit
        lvItems.setAdapter(itemsAdapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//long click change trip selectionned when Orientation is landscape
            lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    listener.onTripItemSelected(pos);
                    return false;
                }
            });
        }
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listener.onTripItemSelected(position);
            }
        });

    }
    @Override //View.OnCreateContextMenuListener
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

    }
    @Override//View.OnCreateContextMenuListener
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
       Trip trip=mViewModel.getTrip(idsAdapter.getItem(info.position));

        switch (item.getItemId()) {

            case R.id.delete:
                if (info.position== mViewModel.sizeOfListTrips()-1){
                    TripDetailFragment.position=info.position-1;
                }
                mViewModel.deleteTrip(trip);
                return true;
            case R.id.exporter:
                    exportTrip(trip);
                Toast.makeText(this.getActivity(), "Le voyage "+trip.title+ " est enregistré dans le dossier téléchargement", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    public void exportTrip(Trip trip){
        final Gson gson = new GsonBuilder().create();
        String json = gson.toJson(trip);
        json+=gson.toJson(mViewModel.getTxtNotes(trip.getUniqueID()));
        json+=gson.toJson(mViewModel.getPicturesNotes(trip.getUniqueID()));

        json+=gson.toJson(mViewModel.getPicturesTagsOfTrip(trip.getUniqueID()));
        json+=gson.toJson(mViewModel.getTxtTagsOfTrip(trip.getUniqueID()));
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), trip.title +"_"+new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date())+".cv");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            stream.write(json.getBytes());
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){
            this.listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()//listTrip
                    + " must implement ListTripFragment.OnItemSelectedListener");
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {//menu de recherche et d'ajout de voyage
        inflater.inflate(R.menu.menu_list_notes, menu);
        SearchManager manager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.menu_list_note_search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String valueInput) {
                final ListView listTripLv =  getActivity().findViewById(R.id.lvItems);
                Pair<List<TripForAdapter>, List<String>> listItemFoundWithTheireIds=mViewModel.getListTripAndTheirIdWithDateOrTitle(valueInput);
                itemsAdapter = new ListTripAdapter(getContext(),listItemFoundWithTheireIds.first);
                idsAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,listItemFoundWithTheireIds.second);
                listTripLv.setAdapter(itemsAdapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String valueInput) {
                final ListView listTripLv =  getActivity().findViewById(R.id.lvItems);
                Pair<List<TripForAdapter>, List<String>> listItemFoundWithTheireIds=mViewModel.getListTripAndTheirIdWithDateOrTitle(valueInput);
                itemsAdapter = new ListTripAdapter(getContext(),listItemFoundWithTheireIds.first);
                idsAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,listItemFoundWithTheireIds.second);
                listTripLv.setAdapter(itemsAdapter);
                return true;


            }});
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_list_note_search:

                return true;

            case R.id.menu_list_note_add:
                Intent intent=new Intent(getActivity(),NewTripActivity.class);
                startActivityForResult(intent, NEW_TRIP_ACTIVITY_REQUEST_CODE);
                return true;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            mViewModel.activityResult(requestCode,resultCode,data);

    }

    public interface OnItemSelectedListener{
        void onTripItemSelected(int position);

    }
}


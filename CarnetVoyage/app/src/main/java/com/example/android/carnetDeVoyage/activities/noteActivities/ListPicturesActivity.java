package com.example.android.carnetDeVoyage.activities.noteActivities;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.carnetDeVoyage.R;
import com.example.android.carnetDeVoyage.adapters.ListNotePictureAdapter;
import com.example.android.carnetDeVoyage.adapters.NotePictureForAdapter;
import com.example.android.carnetDeVoyage.entities.PictureNote;
import com.example.android.carnetDeVoyage.entities.Trip;
import com.example.android.carnetDeVoyage.fragments.ListTripFragment;
import com.example.android.carnetDeVoyage.fragments.TripDetailFragment;

import java.util.ArrayList;
import java.util.List;
public class ListPicturesActivity extends AppCompatActivity {
    public static ListNotePictureAdapter itemsAdapter;
    private String tripId;
    private ListPicturesViewModel mViewModel;
    public static final int NEW_NOTES_PICTURES_ACTIVITY_REQUEST_CODE=7809;
    public static String EXTRA_REPLY_FULL_SCREEN="com.example.android.activities.takeNoteActivity.NOTES_IMG";
    public static ArrayAdapter<String> idsAdapter;
    public static  List<NotePictureForAdapter> allItemsAdapter;
    public static ListView lvItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pictures_notes);
        Intent intent = getIntent();
        tripId = intent.getStringExtra(TripDetailFragment.EXTRA_REPLY_ID);
        mViewModel = ViewModelProviders.of(this).get(ListPicturesViewModel.class);
        allItemsAdapter = mViewModel.getListAllItems(tripId).first;
        idsAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,mViewModel.getListAllItems(tripId).second);

        itemsAdapter = new ListNotePictureAdapter(this, allItemsAdapter);

        lvItems = findViewById(R.id.list_pictures);
        lvItems.setAdapter(itemsAdapter);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(ListPicturesActivity.this,FullScreenPictureActivity.class);
                String url= mViewModel.getUrlOfPictureWithIdNote(idsAdapter.getItem(position));
                i.putExtra(EXTRA_REPLY_FULL_SCREEN,url);
                startActivity(i);
            }

        });
        mViewModel.observeListNote(this,tripId);
        registerForContextMenu(lvItems); //pour avoir le menu apres un long click sur une note

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Supprimer");

        menu.getItem(0).setOnMenuItemClickListener( new  MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();

                mViewModel.deleteNote(idsAdapter.getItem(info.position));
                return true;
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_list_note_add:
                Intent intent=new Intent(ListPicturesActivity.this,TakePictureNoteActivity.class);
                startActivityForResult(intent, NEW_NOTES_PICTURES_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.menu_list_note_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_notes,menu);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.menu_list_note_search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Pair<List<NotePictureForAdapter>, List<String>> listItemsfoundAndIds;
                listItemsfoundAndIds=unionOfTwoResult(mViewModel.getListItemsWithDateOrCity(query, tripId),mViewModel.getListItemsWithTags(query,tripId));
                itemsAdapter = new ListNotePictureAdapter(getApplicationContext(),listItemsfoundAndIds.first);
                idsAdapter=new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1,listItemsfoundAndIds.second);
                ListView lvItems = findViewById(R.id.list_pictures);
                lvItems.setAdapter(itemsAdapter);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if(s.equals("")) {
                    itemsAdapter = new ListNotePictureAdapter(getApplicationContext(), mViewModel.getListAllItems(tripId).first);
                    idsAdapter=new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1,mViewModel.getListAllItems(tripId).second);
                    ListView lvItems = findViewById(R.id.list_pictures);
                    lvItems.setAdapter(itemsAdapter);
                }
                return true;
            }
        });
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_NOTES_PICTURES_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String txtNotes = data.getStringExtra(TakePictureNoteActivity.EXTRA_REPLY_NOTES_IMG);
            if (!txtNotes.equals("")) {
                mViewModel.insertAllPicturesNote(txtNotes, tripId);
            }
        }
    }
    public Pair<List<NotePictureForAdapter>, List<String>>  unionOfTwoResult(Pair<List<NotePictureForAdapter>, List<String>> l1, Pair<List<NotePictureForAdapter>, List<String>> l2){
        List<NotePictureForAdapter> listNotePicture=new ArrayList<>();
        List<String> listIds=new ArrayList<>();
        listNotePicture.addAll(l1.first);
        listIds.addAll(l1.second);
        boolean found=false;
        for(int i=0;i<l2.first.size();i++) {
            for (int j = 0; j < l1.first.size(); j++) {
                if (l2.first.get(i).compartTo(l1.first.get(j))) {
                  found=true;
                }
            }
            if(!found){
                listNotePicture.add(l2.first.get(i));
                listIds.add(l2.second.get(i));
            }
            found=false;
        }
            return Pair.create(listNotePicture,listIds);
        }
}

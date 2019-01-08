package com.example.android.carnetDeVoyage.activities.noteActivities;
import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;
import com.example.android.carnetDeVoyage.R;
import com.example.android.carnetDeVoyage.adapters.ListNoteTxtAdapter;
import com.example.android.carnetDeVoyage.adapters.NoteTxtForAdapter;
import com.example.android.carnetDeVoyage.entities.Trip;
import com.example.android.carnetDeVoyage.fragments.TripDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class ListTxtNotesActivity extends AppCompatActivity {
    public static ListNoteTxtAdapter itemsAdapter;
    public static  ListView lvItems;
    private ListTxtNotesViewModel mViewModel;
    private String tripId;
    public static ArrayAdapter<String> idsAdapter;
    public static final int NEW_NOTES_TXT_ACTIVITY_REQUEST_CODE=7883;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_txt_notes);
        Intent intent =getIntent();
        tripId= intent.getStringExtra(TripDetailFragment.EXTRA_REPLY_ID);

        mViewModel=ViewModelProviders.of(this).get(ListTxtNotesViewModel.class);
        idsAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,mViewModel.getListAllItems(tripId).second);
        itemsAdapter = new ListNoteTxtAdapter(this,mViewModel.getListAllItems(tripId).first);
        lvItems = findViewById(R.id.list_txt_notes);
        lvItems.setAdapter(itemsAdapter);
        mViewModel.observeListNote(this,tripId);
        registerForContextMenu(lvItems);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_list_note_add:
                Intent intent=new Intent(ListTxtNotesActivity.this,TakeTxtNoteActivity.class);
                startActivityForResult(intent, NEW_NOTES_TXT_ACTIVITY_REQUEST_CODE);
                return true;

            case R.id.menu_list_note_search:
                return true;
        }

        return super.onOptionsItemSelected(item);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_notes,menu);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.menu_list_note_search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                List<NoteTxtForAdapter> listItemsfound;
                listItemsfound= unionOfTwoResult(mViewModel.getListItemsWithDateOrCity(query, tripId).first,mViewModel.getListItemsWithTag(query,tripId).first);
                itemsAdapter = new ListNoteTxtAdapter(getApplicationContext(), listItemsfound);
                ListView lvItems = findViewById(R.id.list_txt_notes);
                lvItems.setAdapter(itemsAdapter);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if(s.equals("")) {
                    itemsAdapter = new ListNoteTxtAdapter(getApplicationContext(), mViewModel.getListAllItems(tripId).first);
                    ListView lvItems = findViewById(R.id.list_txt_notes);
                    lvItems.setAdapter(itemsAdapter);
                }
                return true;
            }
    });
        return true;
    }
    public List<NoteTxtForAdapter> unionOfTwoResult( List<NoteTxtForAdapter> l1, List<NoteTxtForAdapter> l2){
        List<NoteTxtForAdapter> result=new ArrayList<>();
        result.addAll(l1);

        boolean found=false;
        for(int i=0;i<l2.size();i++) {
            for (int j = 0; j < l1.size(); j++) {
                if (l2.get(i).compartTo(l1.get(j))) {
                    found=true;
                }
            }
            if(!found){
                result.add(l2.get(i));
            }
            found=false;
        }
        return result;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_NOTES_TXT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String txtNotes=data.getStringExtra(TakeTxtNoteActivity.EXTRA_REPLY_NOTES_TXT);
            if(!txtNotes.equals("")){
            mViewModel.insertAllTxtsNote(txtNotes,tripId);
            }
        }


    }

}

package com.example.android.carnetDeVoyage.activities.tripActivities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.android.carnetDeVoyage.R;
import com.example.android.carnetDeVoyage.fragments.ListTripFragment;
import com.example.android.carnetDeVoyage.fragments.TripDetailFragment;
import com.example.android.carnetDeVoyage.fragments.MapsFragment;

public class ListTripActivity extends AppCompatActivity  implements ListTripFragment.OnItemSelectedListener  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_trip);
        if(savedInstanceState==null){
        ListTripFragment firstFragment1 = new ListTripFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.flContainer, firstFragment1);
        ft.commit();

        }
        else{
            MapsFragment mapFragment=new MapsFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer,mapFragment ) // replace flContainer

                    .commit();
        }


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            TripDetailFragment secondFragment = new TripDetailFragment();
            Bundle args = new Bundle();
            args.putInt("position", TripDetailFragment.position);
            secondFragment.setArguments(args);
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.add(R.id.flContainer2, secondFragment);
            ft2.commit();
            ListTripFragment firstFragment = new ListTripFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer, firstFragment) // replace flContainer
                    .commit();
            MapsFragment mapFragment=new MapsFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer3, mapFragment) // replace flContainer3
                    //.addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public void onTripItemSelected(int position) {
        TripDetailFragment secondFragment = new TripDetailFragment();
       MapsFragment mapFragment=new MapsFragment();
        TripDetailFragment.position=position;
        Bundle args = new Bundle();
        args.putInt("position", position);
        secondFragment.setArguments(args);
        mapFragment.setArguments(args);
        if(this.getApplication().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){//mode paysage
           getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer2, secondFragment) // replace flContainer2
                    //.addToBackStack(null)
                    .commit();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer3, mapFragment) // replace flContainer3
                    //.addToBackStack(null)
                    .commit();

        }
        else{

          getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer,mapFragment ).replace(R.id.flContainer1,secondFragment) // replace flContainer
                    .addToBackStack(null)
                    .commit();

        }
}

}





package com.example.android.carnetDeVoyage.fragments;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.android.carnetDeVoyage.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsFragment extends Fragment {

    private String id;
    private int position;
    private MapsViewModel mViewModel;
    public MapsFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            if(getArguments() != null) {
                position = getArguments().getInt("position", 0);
            }
        }
        if(!ListTripFragment.idsAdapter.isEmpty()){ //if list trip is empty we can't find any id
            id=ListTripFragment.idsAdapter.getItem(position);
        }
        mViewModel = ViewModelProviders.of(this).get(MapsViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mViewModel.observeListNotes( mMap,  id, getActivity());

                mMap.clear();
                PolylineOptions lineOptions = new PolylineOptions();
                LatLng postion;
                for(int i=0;i<mViewModel.getAllNotesWithId(id).first.size();i++){

                    postion =new LatLng(Double.valueOf(mViewModel.getAllNotesWithId(id).first.get(i).getLatitude()),Double.valueOf(mViewModel.getAllNotesWithId(id).first.get(i).getLongItude()));
                             mMap.addMarker(new MarkerOptions()
                            .position(postion)
                            .title(mViewModel.getAllNotesWithId(id).first.get(i).getPlace())
                            .snippet(mViewModel.getAllNotesWithId(id).first.get(i).getDate())


                            );
                    lineOptions.add(postion);
                }
                for(int j=0;j<mViewModel.getAllNotesWithId(id).second.size();j++){
                postion=new LatLng(Double.valueOf(mViewModel.getAllNotesWithId(id).second.get(j).getLatitude()),Double.valueOf(mViewModel.getAllNotesWithId(id).second.get(j).getLongItude()));
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.valueOf(mViewModel.getAllNotesWithId(id).second.get(j).getLatitude()),Double.valueOf(mViewModel.getAllNotesWithId(id).second.get(j).getLongItude())))
                            .title(mViewModel.getAllNotesWithId(id).second.get(j).getPlace())
                            .snippet(mViewModel.getAllNotesWithId(id).second.get(j).getDate())
                    );
                    lineOptions.add(postion);
                }
                lineOptions.width(10);
                lineOptions.color(Color.RED);
                mMap.addPolyline(lineOptions);

            }
        });
        return rootView;
    }

}

package com.example.android.carnetDeVoyage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.android.carnetDeVoyage.R;
import java.util.List;
public class ListTripAdapter extends ArrayAdapter<TripForAdapter> {


    public ListTripAdapter(Context context, List<TripForAdapter> tripsItem) {
        super(context, 0, tripsItem);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trip_item,parent, false);
        }

        TripItemViewHolder viewHolder = (TripItemViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder= new TripItemViewHolder();
            viewHolder.title =  convertView.findViewById(R.id.title_item);
            viewHolder.date = convertView.findViewById(R.id.date_item);

            convertView.setTag(viewHolder);
        }
        TripForAdapter itemTrip = getItem(position);
        viewHolder.title.setText(itemTrip.getTitle());
        viewHolder.date.setText(itemTrip.getDate());

        return convertView;
    }

    private class TripItemViewHolder{
        public TextView title;
        public TextView date;
    }
}
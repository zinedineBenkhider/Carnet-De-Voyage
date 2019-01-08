package com.example.android.carnetDeVoyage.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.android.carnetDeVoyage.R;
import java.util.List;

public class ListNoteTxtAdapter  extends ArrayAdapter<NoteTxtForAdapter> {


    public ListNoteTxtAdapter(Context context, List<NoteTxtForAdapter> notesTxtItem) {
        super(context, 0, notesTxtItem);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.txt_note_item,parent, false);
        }

        TxtItemViewHolder viewHolder = (TxtItemViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder= new TxtItemViewHolder();
            viewHolder.text =  convertView.findViewById(R.id.txt_note_text_item);
            viewHolder.date = convertView.findViewById(R.id.txt_note_date_item);
            viewHolder.tag = convertView.findViewById(R.id.txt_note_tag_item);
            viewHolder.place = convertView.findViewById(R.id.txt_note_place_item);
            convertView.setTag(viewHolder);
        }
        NoteTxtForAdapter itemTxtNote = getItem(position);
        viewHolder.date.setText(itemTxtNote.getDate());
        viewHolder.tag.setText(itemTxtNote.getTag());
        viewHolder.place.setText(itemTxtNote.getPlace());
        viewHolder.text.setText(itemTxtNote.getText());

        return convertView;
    }

    private class TxtItemViewHolder{

        public TextView date;
        public TextView tag;
        public TextView place;
        public TextView text;
    }
}

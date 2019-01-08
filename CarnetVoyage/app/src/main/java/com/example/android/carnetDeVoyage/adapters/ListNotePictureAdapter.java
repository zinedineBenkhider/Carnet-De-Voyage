package com.example.android.carnetDeVoyage.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.carnetDeVoyage.R;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ListNotePictureAdapter extends ArrayAdapter<NotePictureForAdapter> {

    private Context ctx;
    public ListNotePictureAdapter(Context context, List<NotePictureForAdapter> tripsItem) {
        super(context, 0, tripsItem);
        this.ctx=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.picture_note_item,parent, false);
        }

        ImageItemViewHolder viewHolder = (ImageItemViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder= new ImageItemViewHolder();
            viewHolder.image =  convertView.findViewById(R.id.picture_url_item);
            viewHolder.date = convertView.findViewById(R.id.picture_date_item);
            viewHolder.tag = convertView.findViewById(R.id.picture_tag_item);
            viewHolder.place = convertView.findViewById(R.id.picture_place_item);
            convertView.setTag(viewHolder);
        }
        NotePictureForAdapter itemTrip = getItem(position);
        Bitmap compressedBitmap=compresseBitMap(itemTrip.getImage());



        viewHolder.image.setImageBitmap(compressedBitmap);
        viewHolder.date.setText(itemTrip.getDate());
        viewHolder.tag.setText(itemTrip.getTag());
        viewHolder.place.setText(itemTrip.getPlace());
        return convertView;
    }
public Bitmap compresseBitMap(Uri uri){
    InputStream inputStream=null;

    try {
        inputStream = this.ctx.getContentResolver().openInputStream(uri);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }

    BitmapFactory.Options opts = new BitmapFactory.Options();
    opts.inJustDecodeBounds = true;

    BitmapFactory.decodeStream(inputStream, null, opts);
    try {
        inputStream.close();
    } catch (IOException e) {

    }
    int photoW = opts.outWidth;
    int photoH = opts.outHeight;

    int scaleFactor = Math.min(photoH / 200, photoW / 200);
    opts.inJustDecodeBounds = false;
    opts.inSampleSize = scaleFactor;
    opts.inPurgeable = true;

    try {
        inputStream = this.ctx.getContentResolver().openInputStream(uri);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }

    Bitmap bitmap = BitmapFactory.decodeStream(inputStream , null, opts);
    try {
        inputStream.close();
    } catch (IOException e) {

    }

    return bitmap;

}
    private class ImageItemViewHolder{
        private ImageView image;
        public TextView date;
        public TextView tag;
        public TextView place;
    }
}
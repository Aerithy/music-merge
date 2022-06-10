package com.example.music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;

public class SongListAdapter extends BaseAdapter {

    private ArrayList<SongList> sList;
    private LayoutInflater mInflater;

    public SongListAdapter(ArrayList<SongList> sList, LayoutInflater mInflater) {
        this.sList = sList;
        this.mInflater = mInflater;
    }

    @Override
    public int getCount() {
        return sList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mInflater.inflate(R.layout.songlist_item_list, null);
        ImageView img_songlist_icon = (ImageView) convertView.findViewById(R.id.img_songlist_icon);
        TextView txt_songlist_name = (TextView) convertView.findViewById(R.id.txt_songlist_name);

        // TextView txt_aSpeak = (TextView) convertView.findViewById(R.id.txt_aSpeak);
        img_songlist_icon.setBackgroundResource(sList.get(position).getIcon());
        txt_songlist_name.setText(sList.get(position).getSongListName());
        return convertView;
    }

    class ViewHolder {
        protected ImageView img_songlist_icon;
        protected TextView txt_songlist_name;
    }
}

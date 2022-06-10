package com.example.music;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FriendFragment extends Fragment {

    private String content;
    private boolean hidden;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            this.hidden = false;
        }
        else  {
            this.hidden = true;
        }
    }

    public FriendFragment(String content) {
        this.content = content;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_friend,container,false);
        TextView txt_content = (TextView) view.findViewById(R.id.txt_content_3);
        txt_content.setText(content);
        return view;
    }
}
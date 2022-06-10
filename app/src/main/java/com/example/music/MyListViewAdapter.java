package com.example.music;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


class MyListViewCursorAdapter extends CursorAdapter{
    public MyListViewCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder viewHolder=new ViewHolder();
        //获取view
        View view = View.inflate(context, R.layout.item_listview, null);
        //寻找控件
        viewHolder.tv= (TextView) view.findViewById(R.id.textview);

        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder= (ViewHolder) view.getTag();
        //从cursor中获取值
        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
        //把数据设置到控件上面
        viewHolder.tv.setText(name);

    }

    class ViewHolder{
        TextView tv;
    }
}

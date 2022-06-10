package com.example.music;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class SearchActivity extends Activity {

    private EditText mEditText;
    private ListView mListView;
    private TextView mTextView;
    Context context;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        initView();

    }

    private void initView() {
        mTextView = (TextView) findViewById(R.id.textview);
        mEditText = (EditText) findViewById(R.id.edittext);
        mListView = (ListView) findViewById(R.id.listview);


        //EditText添加监听
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            //文本改变之前执行
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            //文本改变的时候执行
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //文本改变之后执行
            public void afterTextChanged(Editable s) {
            }
        });

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果输入框内容为空，提示请输入搜索内容
                if(TextUtils.isEmpty(mEditText.getText().toString().trim())){
                    Toast.makeText(SearchActivity.this,"请输入搜索内容",Toast.LENGTH_SHORT).show();
                }
                else {
                    String str = mEditText.getText().toString().trim();
                    Intent intent= new Intent(SearchActivity.this, WebViewActivity.class);
                    Bundle bundle = new Bundle();
                    Uri uri=Uri.parse("http://tools.liumingye.cn/music/?page=audioPage&type=migu&name="+str);
                    String uri1=uri.toString();
                    bundle.putString("abc",uri1);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    private void showListView() {
        mListView.setVisibility(View.VISIBLE);
        //获得输入的内容
        String str = mEditText.getText().toString().trim();
        //获取数据库对象
        MyOpenHelper myOpenHelper = new MyOpenHelper(getApplicationContext());
        SQLiteDatabase db = myOpenHelper.getReadableDatabase();
        //得到cursor
        cursor = db.rawQuery("select * from singers where name like '%" + str + "%'", null);
        MyListViewCursorAdapter adapter = new MyListViewCursorAdapter(context, cursor);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //把cursor移动到指定行
                cursor.moveToPosition(position);
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            }
        });
    }
}

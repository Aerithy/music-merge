package com.example.music;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
//存储用户信息
public class sharedPreference {
    private Context mContext;
    private RadioButton radioButton;
    public sharedPreference() {}
    public sharedPreference(Context mContext) {
        this.mContext = mContext;
    }

    //定义一个保存数据的方法
    public void save(String username,String password, String confirmPassword,String phone,String email,String address) {
        SharedPreferences sp = mContext.getSharedPreferences("mys", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //检查密码是否一致
        if(confirmPassword.equals(password)) {
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putString("ConfirmPassword", confirmPassword);
            editor.putString("phone", phone);
            editor.putString("email", email);
            editor.putString("address", address);
            editor.apply();
        }
        else
            Toast.makeText(mContext, "请检查密码是否一致", Toast.LENGTH_SHORT).show();
    }

    //定义一个读取SP文件的方法
    public Map<String, String> read() {
        Map<String, String> data = new HashMap<String, String>();
        SharedPreferences sp = mContext.getSharedPreferences("mys", Context.MODE_PRIVATE);
        data.put("username", sp.getString("username", ""));
        data.put("password", sp.getString("password", ""));
        data.put("ConfirmPassword", sp.getString("ConfirmPassword", ""));
        data.put("phone", sp.getString("phone", ""));
        data.put("email", sp.getString("email", ""));
        data.put("address", sp.getString("address", ""));
        return data;
    }
}


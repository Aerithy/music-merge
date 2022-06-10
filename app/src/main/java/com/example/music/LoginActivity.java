package com.example.music;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.litepal.LitePal;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private String number;
    private String password;
    private User user;
    SQLiteDatabase db ;
    private void getEditText(){
        number=((EditText)findViewById(R.id.ed_name)).getText().toString();
        password=((EditText)findViewById(R.id.ed_pass)).getText().toString();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LitePal.initialize(this);
        db = LitePal.getDatabase();
        Button login_btn=findViewById(R.id.login);
        TextView register=findViewById(R.id.sign_up);
        register.setOnClickListener(this);
        login_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //登录按钮
            case R.id.login:
                getEditText();
                //判断两个输入框
                if(number==null||number.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(password==null||password.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //根据手机号查找用户
                List<User> findUser= LitePal.where("userNumber=?",number)
                        .find(User.class);
                //判断有无用户

                //用户不存在
                if(findUser.size()==0){
                    Toast.makeText(this,"无此用户，请重新输入手机号",Toast.LENGTH_SHORT).show();
                    return;
                }
                //用户存在
                else{
                    user=findUser.get(0);

                    //验证密码
                    if(user.getUserPassword().equals(password)){
                        Toast.makeText(this,"登陆成功，返回主页面",Toast.LENGTH_SHORT).show();
                        //将当前用户登录状态status设为1
                        user.setUserStatus("1");
                        user.save();
                        Intent intent1=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent1);
                        finish();
                    }else{
                        Toast.makeText(this,"密码错误，请重新输入",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;
            case R.id.sign_up:
                //转入注册地方
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
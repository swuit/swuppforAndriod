package org.crazyit.auction.client;

import java.util.HashMap;
import java.util.Map;

import org.crazyit.auction.client.util.DialogUtil;
import org.crazyit.auction.client.util.HttpUtil;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
/**
 * Created by Administrator on 2016/4/19 0019.
 */
public class Register extends Activity {
    //
    EditText etName, etPass, etEmail;
    Button bnRegister, bnBack;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        etName = (EditText) findViewById(R.id.userEditText);
        etPass = (EditText) findViewById(R.id.pwdEditText);
        etEmail = (EditText)findViewById(R.id.emailEditText);
        //
        bnRegister = (Button) findViewById(R.id.bnRegister);
        bnBack = (Button) findViewById(R.id.bnBack);
        // ΪbnCancal
        //bnRegister.setOnClickListener(new HomeListener(this));

        bnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    // 执行输入校验
                    if (validate())  // ①
                    {
                        // 如果登录成功
                        if (registerPro())  // ②
                        {
                            // 启动Main Activity
                            Intent intent = new Intent(Register.this
                                    , AuctionClientActivity.class);
                            startActivity(intent);
                            // 结束该Activity
                            finish();
                        }
                        else
                        {
                            DialogUtil.showDialog(Register.this
                                    , "注册失败,该用户名可能已经被使用！", false);
                        }
                    }
                }
        });

        bnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
    // 对用户输入的用户名、密码,邮箱进行校验
    private boolean validate()
    {
        String username = etName.getText().toString().trim();
        if (username.equals(""))
        {
            DialogUtil.showDialog(this, "用户账户是必填项！", false);
            return false;
        }
        String pwd = etPass.getText().toString().trim();
        if (pwd.equals(""))
        {
            DialogUtil.showDialog(this, "用户口令是必填项！", false);
            return false;
        }
        String email = etEmail.getText().toString().trim();
        if (email.equals(""))
        {
            DialogUtil.showDialog(this, "Email是必填项！", false);
            return false;
        }
        return true;
    }

    private boolean registerPro()
    {
        // 获取用户输入的用户名、密码
        String username = etName.getText().toString();
        String pwd = etPass.getText().toString();
        String email = etEmail.getText().toString();
        JSONObject jsonObj;
        try
        {
            jsonObj = query(username, pwd, email);
            // 如果userId 大于0
            if (jsonObj.getInt("userId") > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            DialogUtil.showDialog(this
                    , "响应异常，请稍后再试！", false);
            e.printStackTrace();
        }
        return false;
    }
   // 定义发送请求的方法
   private JSONObject query(String username, String password, String email)
           throws Exception
   {
       // 使用Map封装请求参数
       Map<String, String> map = new HashMap<>();
       map.put("user", username);
       map.put("pass", password);
       map.put("email",email);
       // 定义发送请求的URL
       String url = HttpUtil.BASE_URL + "register.jsp";
       // 发送请求
       return new JSONObject(HttpUtil.postRequest(url, map));
   }
}
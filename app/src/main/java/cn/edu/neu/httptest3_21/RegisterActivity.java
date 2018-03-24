package cn.edu.neu.httptest3_21;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends Activity {

    private static final String TAG = "RegisterActivity";
    private EditText edit_Account ;
    private EditText edit_Password ;
    private EditText edit_Password_sure ;
    private Button bt_register ;
    private static String resCode ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edit_Account = (EditText)findViewById(R.id.rg_edit_user) ;
        edit_Password = (EditText)findViewById(R.id.rg_edit_pass) ;
        edit_Password_sure = (EditText)findViewById(R.id.rg_edit_pass_sure) ;
        bt_register = (Button)findViewById(R.id.rg_button_register) ;
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }
    public static void actionStart(Context context){
        Intent intent = new Intent(context, RegisterActivity.class) ;
        context.startActivity(intent) ;
    }

    private void register(){
        String acount = "" ;
        String password = "" ;
        String password_sure = "" ;
        acount = edit_Account.getText().toString().trim() ;
        password = edit_Password.getText().toString().trim() ;
        password_sure = edit_Password_sure.getText().toString().trim() ;
        if(acount.isEmpty() || acount.length() == 0 || password.isEmpty() || password_sure.isEmpty()){
            Log.d(TAG, "register: haha 账号为空");
            Toast.makeText(RegisterActivity.this, "账号或密码为空，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(password_sure)){
            Log.d(TAG, "register: haha 两次密码输入不一致");
            Toast.makeText(RegisterActivity.this, "两次密码输入不一致，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        String registerUrl = Constant.URL_Register + "?account=" + acount + "&password=" + password ;
        new RegisterAsyncTask().execute(registerUrl) ;
    }

    class RegisterAsyncTask extends AsyncTask<String, Integer, String> {

        public RegisterAsyncTask() {

        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute: haha task onPreExecute()");
        }

        /**
         * @param params 这里的params是一个数组，即AsyncTask在激活运行是调用execute()方法传入的参数
         */
        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "doInBackground: haha task doInBackground()");
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(params[0]); // 声明一个URL,注意如果用百度首页实验，请使用https开头，否则获取不到返回报文
                Log.d(TAG, "doInBackground: haha 22" + params[0]);
                connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
                connection.setRequestMethod("GET"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
                connection.setConnectTimeout(8000); // 设置连接建立的超时时间
                connection.setReadTimeout(8000); // 设置网络报文收发超时时间
                InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "doInBackground: haha "+response.toString());
            return response.toString(); // 这里返回的结果就作为onPostExecute方法的入参
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // 如果在doInBackground方法，那么就会立刻执行本方法
            // 本方法在UI线程中执行，可以更新UI元素，典型的就是更新进度条进度，一般是在下载时候使用
        }
        /**
         * 运行在UI线程中，所以可以直接操作UI元素
         * @param s
         */
        @Override
        protected void onPostExecute(String s){
            Log.d(TAG, "onPostExecute: haha task onPostExecute()111");
            try{
                JSONObject jsonObject = new JSONObject(s) ;
                resCode = jsonObject.get("resCode").toString() ;
                if("200".equals(resCode)){
                    Toast.makeText(RegisterActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                    finish();
                }else if("100".equals(resCode)){
                    Toast.makeText(RegisterActivity.this, "该账号已注册，请使用其他账号", Toast.LENGTH_SHORT).show();
                }else if("300".equals(resCode)){
                    Toast.makeText(RegisterActivity.this, "有人在数据库上搞事情，注册失败了", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

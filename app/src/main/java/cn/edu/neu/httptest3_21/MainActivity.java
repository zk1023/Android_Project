package cn.edu.neu.httptest3_21;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private EditText edit_Account ;
    private EditText edit_Password ;
    private TextView textView ;
    private Button bt_register ;
    private Button bt_login ;
    private static String resCode ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit_Account = (EditText)findViewById(R.id.edit_user) ;
        edit_Password = (EditText)findViewById(R.id.edit_pass) ;
        textView = (TextView)findViewById(R.id.text_view) ;
        textView.setText("Test");
        bt_register = (Button)findViewById(R.id.button_register) ;
        bt_register.setOnClickListener(this);
        bt_login = (Button)findViewById(R.id.button_login) ;
        bt_login.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_login:
                String account_login = edit_Account.getText().toString().trim() ;
                String password_login = edit_Password.getText().toString().trim() ;
                if(!account_login.isEmpty()&&!password_login.isEmpty()){
                    login(account_login, password_login);
                }else{
                    Toast.makeText(MainActivity.this, "账号或密码为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_register:
                String account_register = edit_Account.getText().toString().trim() ;
                String password_register = edit_Password.getText().toString().trim() ;
                if(!account_register.isEmpty()&&!password_register.isEmpty()){
                    register(account_register, password_register);
                }else{
                    Toast.makeText(MainActivity.this, "账号或密码为空", Toast.LENGTH_SHORT).show();
                }
                break ;
            default:
                break ;
        }
    }

    private void register(String account, String password){
        String registerUrl = Constant.URL_Register + "?account=" + account + "&password="+password ;
        new MyAsyncTask(textView, getApplicationContext()).execute(registerUrl) ;

    }

    private void login(String account, String password){
        String loginUrl = Constant.URL_Login + "?account=" + account + "&password="+password ;
        new MyAsyncTask(textView, getApplicationContext()).execute(loginUrl) ;
    }

    /**
     * AsyncTask类的三个泛型参数：
     * （1）Param 在执行AsyncTask是需要传入的参数，可用于后台任务中使用
     * （2）后台任务执行过程中，如果需要在UI上先是当前任务进度，则使用这里指定的泛型作为进度单位
     * （3）任务执行完毕后，如果需要对结果进行返回，则这里指定返回的数据类型
     */
    class MyAsyncTask extends AsyncTask<String, Integer, String> {

        private TextView textView2; // 举例一个UI元素，后边会用到
        private Context context2 ;

        public MyAsyncTask(TextView v, Context context) {
            textView2 = v;
            context2 = context ;
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
            textView2.setText(s);
            try{
                JSONObject jsonObject = new JSONObject(s) ;
                resCode = jsonObject.get("resCode").toString() ;
                if("201".equals(resCode)){
                    SecondActivity.actionStart(MainActivity.this, jsonObject.get("userId").toString(), edit_Account.getText().toString().trim());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

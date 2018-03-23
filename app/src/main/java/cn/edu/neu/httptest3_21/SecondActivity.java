package cn.edu.neu.httptest3_21;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent = getIntent() ;
        String userId = intent.getStringExtra("userId") ;
        String account = intent.getStringExtra("account") ;
        TextView textView = (TextView)findViewById(R.id.text2) ;
        textView.setText("userId: " + userId + "   account   " + account);
    }

    public static void actionStart(Context context, String userId, String account){
        Intent intent = new Intent(context, SecondActivity.class) ;
        intent.putExtra("userId", userId) ;
        intent.putExtra("account", account) ;
        context.startActivity(intent) ;
    }
}

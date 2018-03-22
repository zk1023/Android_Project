package cn.edu.neu.httptest3_21;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, SecondActivity.class) ;
        context.startActivity(intent) ;
    }
}

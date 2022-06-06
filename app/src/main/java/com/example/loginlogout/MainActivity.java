package com.example.loginlogout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;

public class MainActivity extends AppCompatActivity
{
    private static final String apiurl = "http://192.168.229.232/android_db_pool/login_maker.php";
    EditText t1,t2;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checklogoutmsg(tv);
    }

    public void login_process(View view)
    {
        t1=(EditText)findViewById(R.id.t1);
        t2=(EditText)findViewById(R.id.t2);
        tv=(TextView)findViewById(R.id.tv);

        String qry="?t1="+t1.getText().toString().trim()+"&t2="+t2.getText().toString().trim();

          class dbprocess extends AsyncTask<String,Void,String>
          {
              @Override
              protected  void onPostExecute(String data)
              {
                  if(data.equals("found"))
                  {
                      SharedPreferences sp=getSharedPreferences("credentials",MODE_PRIVATE);
                      SharedPreferences.Editor editor=sp.edit();
                      editor.putString("uname",t1.getText().toString());
                      editor.commit();
                      startActivity(new Intent(getApplicationContext(),dashboard.class));
                  }
                  else
                  {
                      t1.setText("");
                      t2.setText("");
                      tv.setTextColor(Color.parseColor("#8B0000"));
                      tv.setText(data);
                  }
              }
                  @Override
              protected String doInBackground(String... params)
              {
                  String furl=params[0];

                  try
                  {
                      URL url=new URL(furl);
                      HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                      BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                      return br.readLine();

                  }catch (Exception ex)
                  {
                      return ex.getMessage();
                  }
              }
          }

          dbprocess obj=new dbprocess();
          obj.execute(apiurl+qry);

    }

    public void checklogoutmsg(View view)
    {
        tv=(TextView)findViewById(R.id.tv);

        SharedPreferences sp=getSharedPreferences("credentials",MODE_PRIVATE);
        if(sp.contains("msg"))
        {
            tv.setText(sp.getString("msg", ""));
            SharedPreferences.Editor ed=sp.edit();
            ed.remove("msg");
            ed.commit();
        }
    }
}

package com.example.loginlogout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class dashboard extends AppCompatActivity
{
    private TextView mHeadingLabel;
    private ImageView mFingerprintImage;
    private TextView mParaLabel;

    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    TextView tv;
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tv=(TextView)findViewById(R.id.tv);
        tv=(TextView)findViewById(R.id.tv);
        tv2=(TextView)findViewById(R.id.textView);
        SharedPreferences sp=getSharedPreferences("credentials",MODE_PRIVATE);
        if(sp.contains("uname"))
            tv.setText(sp.getString("uname",""));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            if(!fingerprintManager.isHardwareDetected()) {
                tv2.setText("Fingerprint Scanner not detected.");
            } else if(ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                tv2.setText("Permission not granted to use fingerprint scanner.");
            } else if(!keyguardManager.isKeyguardSecure()) {
                tv2.setText("Add lock to your phone.");
            } else if(!fingerprintManager.hasEnrolledFingerprints()) {
                tv2.setText("You should add atleast 1 fingerprint to use this feature.");
            } else {
                tv2.setText("Place your finger on scanner to access the app.");

                FingerprintHandler fingerprintHandler = new FingerprintHandler(this);
                fingerprintHandler.startAuth(fingerprintManager, null);

            }
        }
    }

    public void logout_process(View view)
    {
        SharedPreferences sp=getSharedPreferences("credentials",MODE_PRIVATE);
        if(sp.contains("uname"))
        {
            SharedPreferences.Editor editor=sp.edit();
            editor.remove("uname");

            editor.putString("msg","Logged out Successfully");
            editor.commit();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

    }
}

package com.example.loginlogout;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    public FingerprintHandler(Context context) {
        this.context = context;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Auth Failed. ", false);
    }

    @Override
    public void onAuthenticationError (int errorCode, CharSequence errString) {
        this.update("There was an auth error." + errString, false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update("Error: " + helpString, false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("You can now access the app.", true);
    }

    private void update(String s, boolean b) {
        TextView textView = (TextView) ((Activity)context).findViewById(R.id.textView);
        ImageView imageView = (ImageView) ((Activity)context).findViewById(R.id.imageView);

        textView.setText(s);
        if(b == false) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            SharedPreferences sp=context.getSharedPreferences("credentials", Context.MODE_PRIVATE);

            String url = "http://192.168.229.232/vtg-website/hr/html/servers/attendance-scan-server.php?operation=true&extension_number=" + sp.getString("uname","");
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        }
    }

}

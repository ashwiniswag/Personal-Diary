package com.example.notes2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.widget.Toast;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class FingerprintAuth extends AppCompatActivity {

    private String KEY_NAME="somekeyname";
    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_auth);

        KeyguardManager keyguardManager=(KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager=(FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        if(!fingerprintManager.isHardwareDetected()){
            Toast.makeText(FingerprintAuth.this,"Fingerprint Scanner Is not Present",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT)!= PackageManager.PERMISSION_GRANTED){
            return;
        }

        if(!keyguardManager.isKeyguardSecure()){
            return;
        }

        KeyStore keyStore;
        try{
            keyStore=KeyStore.getInstance("AndroidKeyStore");
        }catch (Exception e){
            return;
        }

        KeyGenerator keyGenerator;

        try{
            keyGenerator=KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
        }catch (Exception e){
            Log.e("KeyGenerator",e.getMessage());
            return;
        }

        try {
            keyStore.load(null);
            keyGenerator.init(
                    new KeyGenParameterSpec.Builder(KEY_NAME,KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                            .setUserAuthenticationRequired(true)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                            .build());
            keyGenerator.generateKey();

        }catch (Exception e){
            return;
        }

        Cipher cipher;

        try {
            cipher=Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES+"/"+KeyProperties.BLOCK_MODE_CBC+"/"+KeyProperties.ENCRYPTION_PADDING_PKCS7);
        }catch (Exception e){
            return;
        }


        try{
            keyStore.load(null);
            SecretKey key=(SecretKey) keyStore.getKey(KEY_NAME,null);
            cipher.init(Cipher.ENCRYPT_MODE,key);
        }catch (Exception e){
            return;
        }

        FingerprintManager.CryptoObject cryptoObject=new FingerprintManager.CryptoObject(cipher);
        CancellationSignal cancellationSignal=new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject,cancellationSignal,0,new AuthenticationHandler(this),null );
    }
}

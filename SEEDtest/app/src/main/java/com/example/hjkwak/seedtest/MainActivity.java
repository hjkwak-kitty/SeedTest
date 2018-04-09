package com.example.hjkwak.seedtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import org.kisa.*;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("KISACrypto");
    }

//    SEEDCBC seed = new SEEDCBC();
    public static final byte[] key = { 0x01, 0x02, 0x03, 0x04,
            0x05, 0x06, 0x07, 0x08,
            0x09, 0x0A, 0x0B, 0x0C,
            0x0D, 0x0E, 0x0F, 0x10 };


    public static final byte[] iv = { 0x01, 0x01, 0x01, 0x01,
            0x01, 0x01, 0x01, 0x01,
            0x01, 0x01, 0x01, 0x01,
            0x01, 0x01, 0x01, 0x01 };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String text = "MY Secret code";
        Log.d("seed","암호화 "+ enCodeWithSEED(text) + " 복호화 " + deCodeWithSEED(enCodeWithSEED(text)));


    }

    public String deCodeWithSEED(String encrypedStr) {
        byte[] cipherText = Base64.decode(encrypedStr, Base64.DEFAULT);
        int nInputLen = cipherText.length;
        int nOutputLen = nInputLen;
        byte[] plainText = new byte[nOutputLen];
        String plainStr = null;
        if( cipherText != null && cipherText.length > 0 ) {
            int outputTextLen = 0;
            int outputTextLen2 = 0;
            SEEDCBC seed = new SEEDCBC();
            int nRslt = seed.init(SEEDCBC.DEC, key, iv);
            if( nRslt == 1 ) {
                outputTextLen = seed.process(cipherText, 0, nInputLen, plainText,0); //반환값 : 복호화시 process() method는 원문(Original text)의 블록단위의 크기로 나누고 남겨진 데이터를 제외한 길이
                if (outputTextLen >= 0){
                    outputTextLen2 = seed.close(plainText,outputTextLen); //반환값 : 복호화시 원문(Original text)의 블록단위의 크기로 나누고 남겨진 데이터의 길이
                    if (outputTextLen2 >= 0)
                        plainStr = new String(plainText, 0, outputTextLen + outputTextLen2);
                }
            }
        }
        return plainStr;
    }

    public String enCodeWithSEED(String plainStr) {
        String encryptedStr = null;
        int nInputLen = plainStr.length();
        int nOutputLen = nInputLen + 16 - nInputLen % 16;
        byte[] plainText = new byte[nInputLen];
        byte[] cipherText = new byte[nOutputLen];
        plainText = plainStr.getBytes();
        int outputTextLen = 0;
        SEEDCBC seed = new SEEDCBC();
        int nRslt = seed.init(SEEDCBC.ENC, key, iv);
        if (nRslt == 1) {
            outputTextLen = seed.process(plainText, 0, plainText.length, cipherText, 0);
            seed.close(cipherText, outputTextLen);
            encryptedStr = Base64.encodeToString(cipherText, Base64.DEFAULT);
        }
        return encryptedStr;
    }



}

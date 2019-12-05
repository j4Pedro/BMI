package com.example.bmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Bmi extends AppCompatActivity {

    Button calcBtn;
    EditText etHeight, etWeight, etHeightFeet, etHeightInch;
    TextView result, suggest, result2;
    String txtHeight, txtWeight;
    RadioButton rdbMale, rdbFemale;
    String lang;

    private static final String TAG = "LifeCycle";
    private static final String PREF = "BMI_PREF";
    private static final String PREF_HEIGHT = "BMI_Height";
    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), PREF);

    public Bmi() {
        txtHeight = "0";
        txtWeight = "0";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Bmi-onCreate");

        lang = getResources().getConfiguration().locale.toString();
        findViews();
        setListener();
    }

    protected void onStart() {
        Log.d(TAG, "Bmi-onStart");
        super.onStart();
    }

    private void restorePrefs() {
        SharedPreferences settings = getSharedPreferences(PREF, 0);
        String pref_height = settings.getString(PREF_HEIGHT, "");
        if (!"".equals(pref_height)) {
            etHeight.setText(pref_height);
            etWeight.requestFocus();
        }

    }

    protected void onPause() {
        Log.d(TAG, "Bmi-onPause");
        super.onPause();
        //save user preferences.
//        SharedPreferences settings = getSharedPreferences(PREF,0);
//        settings.edit().putString(PREF_HEIGHT,etHeight.getText().toString()).apply();
        if (isExternalStorageWritable()) {
            try {
                OutputStream os = new FileOutputStream(file);
                byte[] data = etHeight.getText().toString().getBytes();
                os.write(data);
                os.flush();
                os.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    protected void onResume() {
//        Log.d(TAG, "Bmi-onResume");
        super.onResume();
//        restorePrefs();
        if (isExternalStorageReadable()) {
            try {
                InputStream is = new FileInputStream(file);
                byte[] buffer = new byte[100];
                is.read(buffer);
                is.close();
                String height = new String(buffer).trim();
                if (!"".equals(height)) {
                    etHeight.setText(height);
                    etWeight.requestFocus();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onStop() {
//        Log.d(TAG, "Bmi-onStop");
        super.onStop();
    }

    protected void onRestart() {
//        Log.d(TAG, "Bmi-onRestart");
        super.onRestart();
    }

    protected void onDestroy() {
//        Log.d(TAG, "Bmi-onDestroy");
        super.onDestroy();
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
/*
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
        */
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
/*
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
        */
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    private void findViews() {
        calcBtn = findViewById(R.id.calcBtn);
        if("en_US".equals(lang)){
            etHeightFeet = findViewById(R.id.etHeight);
            etHeightInch = findViewById(R.id.etHeightInch);
        }else {
            etHeight = findViewById(R.id.etHeight);
        }


        etWeight = findViewById(R.id.etWeight);
        result = findViewById(R.id.result);
        suggest = findViewById(R.id.suggest);
        rdbMale = findViewById(R.id.rdbMale);
        rdbFemale = findViewById(R.id.rdbFemale);
    }

    private void setListener() {
        calcBtn.setOnClickListener(calcBMI);
    }

    View.OnClickListener calcBMI = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(Bmi.this, Report.class);
            Bundle bundle = new Bundle();
            //double feet = Double.parseDouble(Bmi.this.etHeightFeet.getText().toString());
            if("en_US".equals(lang)){
                bundle.putString("KeyHF", etHeightFeet.getText().toString());
                bundle.putString("KeyHI", etHeightInch.getText().toString());
            }else{
                bundle.putString("KeyH", etHeight.getText().toString());
            }
            bundle.putString("KeyW", etWeight.getText().toString());
            bundle.putBoolean("KeyMale", rdbMale.isChecked());
            intent.putExtras(bundle);
            startActivity(intent);
/*
            String txtHeight = etHeight.getText().toString();
            String txtWeight = etWeight.getText().toString();
            double BMI = Double.parseDouble(txtWeight) / Math.pow(Double.parseDouble(txtHeight) / 100, 2);
            //DecimalFormat df = new DecimalFormat("0.00");
            result.setText(String.format(getResources().getString(R.string.bmi_result), BMI));
            boolean isrMale = rdbMale.isChecked();
            if (isrMale) {
                if (BMI > 25) {
                    suggest.setText(R.string.advice_heavy);
                } else if (BMI < 20) {
                    suggest.setText(R.string.advice_light);
                } else {
                    suggest.setText(R.string.advice_average);
                }
            } else {
                if (BMI > 24) {
                    suggest.setText(R.string.advice_heavy);
                } else if (BMI < 18.5) {
                    suggest.setText(R.string.advice_light);
                } else {
                    suggest.setText(R.string.advice_average);
                }
                optionsDialog();
            }
            */
        }
    };
/*    private void optionsDialog() {

        final EditText et = new EditText(this);

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("自訂Dialog");
        ab.setMessage("使用EditText");
        ab.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result2.setText(et.getText().toString());
            }
        });
        ab.setView(et);
        ab.setNegativeButton("取消", null);
        ab.show();
    }*/
}

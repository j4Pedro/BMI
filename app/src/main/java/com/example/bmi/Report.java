package com.example.bmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Report extends AppCompatActivity {

    TextView result, suggest;
    String lang;
    private static final String TAG = "LifeCycle";
    private static final String channeBMI = "channeBMI";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
//        Log.d(TAG, "Report-onCreate");

        lang = getResources().getConfiguration().locale.toString();
        findViews();
        calcBMI();

    }
    protected void onStart() {
//        Log.d(TAG, "Report-onStart");
        super.onStart();
    }
    protected void onResume() {
        Log.d(TAG, "Report-onResume");
        super.onResume();
    }
    protected void onPause() {
        Log.d(TAG, "Report-onPause");
        super.onPause();
    }
    protected void onStop() {
//        Log.d(TAG, "Report-onStop");
        super.onStop();
    }
    protected void onRestart() {
//        Log.d(TAG, "Report-onRestart");
        super.onRestart();
    }
    protected void onDestroy() {
//        Log.d(TAG, "Report-onDestroy");
        super.onDestroy();
    }
    private void findViews() {

        result = findViewById(R.id.result);
        suggest = findViewById(R.id.suggest);

    }
    private void calcBMI() {
        double BMI=0;
        Bundle bundle = getIntent().getExtras();
        if("en_US".equals(lang)){
            String Feet = bundle.getString("KeyHF");
            String Inch = bundle.getString("KeyHI");
            String txtWeight = bundle.getString("KeyW");
            BMI = Double.parseDouble(txtWeight)*0.45359  / Math.pow((Double.parseDouble(Feet)*12+Double.parseDouble(Inch))*2.54 / 100, 2);
/*

            Log.e(TAG, "Feet = "+ Feet);
            Log.e(TAG, "Inch = "+ Inch);
            Log.e(TAG, "BMI = "+ BMI);

*/
        }else{
            String txtHeight = bundle.getString("KeyH");
            String txtWeight = bundle.getString("KeyW");
            BMI = Double.parseDouble(txtWeight)/ Math.pow(Double.parseDouble(txtHeight) / 100, 2);
        }
        //DecimalFormat df = new DecimalFormat("0.00");
        result.setText(String.format(getResources().getString(R.string.bmi_result), BMI));
        boolean isrMale = bundle.getBoolean("KeyMale");
        if (isrMale) {
            if (BMI > 25) {
                showNotification(BMI);
                suggest.setText(R.string.advice_heavy);
                result.setTextColor(Color.rgb(255, 0, 0));
            } else if (BMI < 20) {
                suggest.setText(R.string.advice_light);
                result.setTextColor(Color.rgb(200, 100, 0));
            } else {
                suggest.setText(R.string.advice_average);
            }
        } else {
            if (BMI > 24) {
                suggest.setText(R.string.advice_heavy);
                result.setTextColor(Color.rgb(255, 0, 0));
            } else if (BMI < 18.5) {
                suggest.setText(R.string.advice_light);
                result.setTextColor(Color.rgb(200, 100, 0));
            } else {
                suggest.setText(R.string.advice_average);
            }
        }

    }
    protected void showNotification(double BMI) {

        NotificationManager barManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Bmi.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification barMsg = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            barMsg = new Notification.Builder(this)
                    .setContentTitle("BMI值過高")
                    .setContentText("你的BMI值太高了!!!!")
                    .setSmallIcon(R.mipmap.icons8)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build();
        }

        barManager.notify(0, barMsg);

    }
}

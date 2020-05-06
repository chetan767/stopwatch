package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityButtonController;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText min;
    EditText sec;
    TextView time;
    Button start;
    Button stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        min = findViewById(R.id.min);
        sec = findViewById(R.id.sec);
        time = findViewById(R.id.time);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        final int play[] = new int[5];
        play[0] = 1;
        final CountTask ct = new CountTask();


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    int time_sec = Integer.valueOf(sec.getText().toString());
                    int time_min = Integer.valueOf(min.getText().toString());
                    ct.execute(time_sec, time_min);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Open Again",Toast.LENGTH_SHORT).show();
                }

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ct.cancel(true);
            }

        });

    }

    class CountTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... integers) {

            int time_sec = integers[0];
            int time_min = integers[1];


            while (time_min >= 0) {
                if(isCancelled()) break;
                while (time_sec > 0) {
                    if(isCancelled()) break;
                    time_sec--;
                    wait1Sec();
                    publishProgress(time_min, time_sec);
                }

                time_sec = 60;
                time_min--;

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values[0] == values[1] && values[0] == 0)
                time.setText("TIMEOUT");
            else {
                String t = String.format("%02d%n", values[0]) + "  : " + String.format("%02d%n", values[1]);
                time.setText(t);

            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    void wait1Sec() {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < startTime + 1000) ;
    }

    void waitNSec(int n) {
        for (int i = 0; i < n; i++) {
            wait1Sec();
        }
    }


}

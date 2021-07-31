package com.fuusy.aptex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fuusy.arouterapi.ARouter;
import com.fuusy.lib_annotation.Route;

@Route(path = "main/MainActivity")
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void intentToOne(View view) {
        Log.d(TAG, "intentToOne: ");
        ARouter.getInstance().jumpActivity("module_one/OneActivity");
    }
}
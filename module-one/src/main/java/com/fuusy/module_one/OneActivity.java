package com.fuusy.module_one;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fuusy.lib_annotation.Route;

@Route(path = "module_one/OneActivity")
public class OneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
    }
}
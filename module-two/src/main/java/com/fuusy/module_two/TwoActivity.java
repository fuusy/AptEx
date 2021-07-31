package com.fuusy.module_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fuusy.lib_annotation.Route;

@Route(path = "module_two/TwoActivity")
public class TwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
    }
}
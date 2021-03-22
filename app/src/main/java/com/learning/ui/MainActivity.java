package com.learning.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.learning.XMode.XModeView;
import com.learning.XMode.XfermodeEraserView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(new GradientLayout(this));
//        setContentView(new XModeView(this));
        setContentView(new XfermodeEraserView(this));
    }
}
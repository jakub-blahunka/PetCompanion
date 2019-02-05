package org.jaku8ka.petcompanion;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Information extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);


        btn = findViewById(R.id.btnOnOff);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment loadingFragment = fragmentManager.findFragmentById(R.id.fragment);

                if(!loadingFragment.isVisible()) {
                    fragmentManager.beginTransaction().show(fragmentManager.findFragmentById(R.id.fragment)).commit();
                } else {
                    fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragment)).commit();
                }
            }
        });
    }
}

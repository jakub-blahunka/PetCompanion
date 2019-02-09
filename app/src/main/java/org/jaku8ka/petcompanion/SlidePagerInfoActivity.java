package org.jaku8ka.petcompanion;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;

public class SlidePagerInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        ViewPager mPager = findViewById(R.id.pager);
        SlidePagerInfoAdapter adapter = new SlidePagerInfoAdapter(this, getSupportFragmentManager());
        mPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}

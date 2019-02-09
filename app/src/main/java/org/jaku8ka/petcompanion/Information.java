package org.jaku8ka.petcompanion;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class Information extends AppCompatActivity implements ItemSelected{

    FragmentManager fragmentManager;
    private FragmentRefreshListener fragmentRefreshListener;

    public interface FragmentRefreshListener {
        void onRefresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragmentLoad)).commit();
    }

    @Override
    public void onItemSelected(int index) {
        MainActivity.POSITION = index;

        if(getFragmentRefreshListener() != null) {
            getFragmentRefreshListener().onRefresh();
        }
    }

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }
}

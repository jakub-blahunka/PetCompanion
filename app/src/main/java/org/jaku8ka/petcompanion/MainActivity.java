package org.jaku8ka.petcompanion;


import android.content.DialogInterface;
import android.content.Intent;

import android.support.v4.app.FragmentManager;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ListView;

import android.widget.Toast;

import com.backendless.Backendless;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemSelected {

    private FragmentRefreshListener fragmentRefreshListener;
    PetsAdapter adapter;
    ListView lvPetsList;
    FragmentManager fragmentManager;

    public static int POSITION = 9999;


    public interface FragmentRefreshListener {
        void onRefresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String whereClause = "userEmail = '" + ApplicationClass.user.getEmail() + "'";

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("name");

        Backendless.Persistence.of(Pet.class).find(queryBuilder, new AsyncCallback<List<Pet>>() {
            @Override
            public void handleResponse(List<Pet> response) {
                ApplicationClass.pets = response;
                lvPetsList = findViewById(android.R.id.list);
                adapter = new PetsAdapter(MainActivity.this, ApplicationClass.pets);
                lvPetsList.setAdapter(adapter);

                POSITION = 9999;

                if (findViewById(R.id.layoutPortrait) == null) {
                    if (!response.isEmpty()) {
                        onItemSelected(0);
                    }
                }

                if (findViewById(R.id.layoutLand) == null) {
                    if (response.isEmpty()) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setMessage("You have not entered any data, please add new pets with plus sign");
                        dialog.setPositiveButton("I got it!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        dialog.show();
                    }
                } else if (findViewById(R.id.layoutPortrait) == null) {
                    if (response.isEmpty()) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragmentDetail)).commit();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setMessage("You have not entered any data, please add new pets with plus sign");
                        dialog.setPositiveButton("I got it!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        dialog.show();
                    }
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(MainActivity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        fragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.layoutPortrait) != null) {
            fragmentManager.beginTransaction()
                    .hide(fragmentManager.findFragmentById(R.id.fragmentDetail))
                    .hide(fragmentManager.findFragmentById(R.id.fragmentButtons))
                    .show(fragmentManager.findFragmentById(R.id.fragmentList))
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .show(fragmentManager.findFragmentById(R.id.fragmentDetail))
                    .show(fragmentManager.findFragmentById(R.id.fragmentButtons))
                    .show(fragmentManager.findFragmentById(R.id.fragmentList))
                    .commit();
        }

    }

    @Override
    public void onItemSelected(int index) {
        POSITION = index;

        if (getFragmentRefreshListener() != null) {
            getFragmentRefreshListener().onRefresh();
        }

        if (findViewById(R.id.layoutPortrait) != null) {

            fragmentManager.beginTransaction()
                    .show(fragmentManager.findFragmentById(R.id.fragmentDetail))
                    .show(fragmentManager.findFragmentById(R.id.fragmentButtons))
                    .hide(fragmentManager.findFragmentById(R.id.fragmentList))
                    .addToBackStack(null)
                    .commit();
        } else {
            for (int i = 0; i < ApplicationClass.pets.size(); i++) {
                ApplicationClass.pets.get(i).setSelected(false);
            }
            ApplicationClass.pets.get(index).setSelected(true);
            adapter.notifyDataSetChanged();
        }
    }

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                POSITION = 9999;

                startActivity(new Intent(MainActivity.this, NewPet.class));
                return true;
            case R.id.action_info:
                startActivity(new Intent(MainActivity.this, SlidePagerInfoActivity.class));
                return true;
            case R.id.action_logout:
                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {

                        Toast.makeText(MainActivity.this, "User logged out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, Login.class));
                        MainActivity.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        Toast.makeText(MainActivity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void dataChanged() {
        adapter.notifyDataSetChanged();
    }
}
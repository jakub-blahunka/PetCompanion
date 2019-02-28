package org.jaku8ka.petcompanion;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemSelected {

    private FragmentRefreshListener fragmentRefreshListener;
    private PetsAdapter adapter;
    private ListView lvPetsList;
    private FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;

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

                if (response.isEmpty()) {

                    if (findViewById(R.id.layoutPortrait) == null) {
                        fragmentManager.beginTransaction()
                                .hide(fragmentManager.findFragmentById(R.id.fragmentDetail))
                                .hide(fragmentManager.findFragmentById(R.id.fragmentButtons))
                                .hide(fragmentManager.findFragmentById(R.id.fragmentList))
                                .commit();
                    }

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
            ImageView imageView = findViewById(R.id.ivPet);
            imageView.getLayoutParams().height = 0;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            layoutParams.setMargins(8, 0, 0, 0);
            TextView textView = findViewById(R.id.tv_name);
            textView.setLayoutParams(layoutParams);
            textView.getLayoutParams().height = 0;
        }
        setNavDrawer();
        NotificationScheduler.createNotificationChannel(this);
    }

    private void setNavDrawer() {

        NavigationView navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header);
        TextView name = headerView.findViewById(R.id.header_name);
        TextView email = headerView.findViewById(R.id.header_mail);
        name.setText(ApplicationClass.user.getProperty("name").toString());
        email.setText(ApplicationClass.user.getEmail());
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        switch (menuItem.getItemId()) {

                            case R.id.nav_info:
                                startActivity(new Intent(MainActivity.this, SlidePagerInfoActivity.class));
                                return true;
                            case R.id.nav_logout:
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
                        }
                        return true;
                    }
                });

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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        if(!fragmentManager.findFragmentById(R.id.fragmentDetail).isVisible())
            MainActivity.this.finish();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                POSITION = 9999;
                startActivity(new Intent(MainActivity.this, NewPet.class));
                MainActivity.this.finish();
                return true;
            case android.R.id.home:
                if(!drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.openDrawer(GravityCompat.START);
                else drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void dataChanged() {
        adapter.notifyDataSetChanged();
    }
}
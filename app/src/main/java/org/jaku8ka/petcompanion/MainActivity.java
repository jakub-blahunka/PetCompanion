package org.jaku8ka.petcompanion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    public static int POSITION = 9999;

    PetsAdapter adapter;
    ListView lvList;
    ImageView ivInfo, ivEdit, ivDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        String whereClause = "userEmail = '" + ApplicationClass.user.getEmail() + "'";

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("name");

        showProgress(true);
        tvLoad.setText("Getting all pets...please wait...");

        Backendless.Persistence.of(Pet.class).find(queryBuilder, new AsyncCallback<List<Pet>>() {
            @Override
            public void handleResponse(List<Pet> response) {

                ApplicationClass.pets = response;
                adapter = new PetsAdapter(MainActivity.this, ApplicationClass.pets);
                lvList.setAdapter(adapter);
                showProgress(false);
                POSITION = 9999;
            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(MainActivity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });
    }

    private void initViews() {
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        lvList = findViewById(R.id.lvList);

        ivInfo = findViewById(R.id.ivInfo);
        ivEdit = findViewById(R.id.ivEdit);
        ivDelete = findViewById(R.id.ivDelete);

        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (POSITION == 9999) {
                    Toast.makeText(MainActivity.this, "First choose pet to get closer information!", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(MainActivity.this, PetInfo.class));
                }

            }
        });

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (POSITION == 9999) {
                    Toast.makeText(MainActivity.this, "First choose pet to edit!", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(MainActivity.this, NewPet.class));
                }
            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (POSITION == 9999) {
                    Toast.makeText(MainActivity.this, "First choose pet to delete!", Toast.LENGTH_SHORT).show();
                } else {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setMessage("Are you sure you want to delete this pet?");

                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showProgress(true);
                            tvLoad.setText("Deleting pet...please wait...");

                            Backendless.Persistence.of(Pet.class).remove(ApplicationClass.pets.get(POSITION), new AsyncCallback<Long>() {
                                @Override
                                public void handleResponse(Long response) {
                                    ApplicationClass.pets.remove(POSITION);
                                    Toast.makeText(MainActivity.this, "Pet successfully removed!", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    adapter.notifyDataSetChanged();
                                    showProgress(false);
                                    POSITION = 9999;
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {

                                    Toast.makeText(MainActivity.this, "Error: " +fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    showProgress(false);
                                }
                            });
                        }
                    });

                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialog.show();
                }

            }
        });

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MainActivity.POSITION = position;
                for(int i = 0; i < ApplicationClass.pets.size(); i++) {
                    ApplicationClass.pets.get(i).setSelected(false);
                }
                ApplicationClass.pets.get(position).setSelected(true);
                adapter.notifyDataSetChanged();
            }
        });

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
                //TODO: implement info class
                return true;
            case R.id.action_logout:
                showProgress(true);
                tvLoad.setText("User logging out...please wait...");
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
                        showProgress(false);
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}

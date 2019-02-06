package org.jaku8ka.petcompanion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    ISetTextInFragment myText;
    FragmentManager fragmentManager;

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

        fragmentManager.beginTransaction().show(fragmentManager.findFragmentById(R.id.fragment_main)).commit();
        myText.showText("Getting all pets...please wait...");

        Backendless.Persistence.of(Pet.class).find(queryBuilder, new AsyncCallback<List<Pet>>() {
            @Override
            public void handleResponse(List<Pet> response) {

                ApplicationClass.pets = response;
                adapter = new PetsAdapter(MainActivity.this, ApplicationClass.pets);
                lvList.setAdapter(adapter);
                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragment_main)).commit();

                POSITION = 9999;
            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(MainActivity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragment_main)).commit();
            }
        });
    }

    private void initViews() {

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragment_main)).commit();
        myText = (ISetTextInFragment) fragmentManager.findFragmentById(R.id.fragment_main);

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
                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fadeout);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            startActivity(new Intent(MainActivity.this, PetInfo.class));
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    ivInfo.startAnimation(animation);
                }

            }
        });

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (POSITION == 9999) {
                    Toast.makeText(MainActivity.this, "First choose pet to edit!", Toast.LENGTH_SHORT).show();
                } else {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fadeout);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            startActivity(new Intent(MainActivity.this, NewPet.class));
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    ivEdit.startAnimation(animation);
                }
            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (POSITION == 9999) {
                    Toast.makeText(MainActivity.this, "First choose pet to delete!", Toast.LENGTH_SHORT).show();
                } else {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fadeout);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(final Animation animation) {

                            final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                            dialog.setMessage("Are you sure you want to delete this pet?");

                            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    fragmentManager.beginTransaction().show(fragmentManager.findFragmentById(R.id.fragment_main)).commit();
                                    myText.showText("Deleting pet...please wait...");

                                    Backendless.Persistence.of(Pet.class).remove(ApplicationClass.pets.get(POSITION), new AsyncCallback<Long>() {
                                        @Override
                                        public void handleResponse(Long response) {
                                            ApplicationClass.pets.remove(POSITION);
                                            Toast.makeText(MainActivity.this, "Pet successfully removed!", Toast.LENGTH_SHORT).show();
                                            ivDelete.clearAnimation();
                                            setResult(RESULT_OK);
                                            adapter.notifyDataSetChanged();
                                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragment_main)).commit();
                                            POSITION = 9999;
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {

                                            Toast.makeText(MainActivity.this, "Error: " +fault.getMessage(), Toast.LENGTH_SHORT).show();
                                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragment_main)).commit();
                                        }
                                    });
                                }
                            });

                            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ivDelete.clearAnimation();

                                }
                            });
                            dialog.show();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    ivDelete.startAnimation(animation);


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
                startActivity(new Intent(MainActivity.this, Information.class));
                return true;
            case R.id.action_logout:
                fragmentManager.beginTransaction().show(fragmentManager.findFragmentById(R.id.fragment_main)).commit();
                myText.showText("User logging out...please wait...");
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
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragment_main)).commit();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

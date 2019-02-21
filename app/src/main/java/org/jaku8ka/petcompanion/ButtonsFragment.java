package org.jaku8ka.petcompanion;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;


/**
 * A simple {@link Fragment} subclass.
 */
public class ButtonsFragment extends Fragment {

    ImageView ivDelete, ivEdit;

    public ButtonsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttons, container, false);

        ivEdit = view.findViewById(R.id.ivEdit);
        ivDelete = view.findViewById(R.id.ivDelete);

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MainActivity.POSITION == 9999) {
                    Toast.makeText(getContext(), "First choose pet to edit!", Toast.LENGTH_SHORT).show();
                } else {

                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            startActivity(new Intent(getContext(), NewPet.class));
                            ivEdit.clearAnimation();
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

                if (MainActivity.POSITION == 9999) {
                    Toast.makeText(getContext(), "First choose pet to delete!", Toast.LENGTH_SHORT).show();
                } else {

                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(final Animation animation) {

                            final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                            dialog.setMessage("Are you sure you want to delete this pet?");

                            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Backendless.Persistence.of(Pet.class).remove(ApplicationClass.pets.get(MainActivity.POSITION), new AsyncCallback<Long>() {
                                        @Override
                                        public void handleResponse(Long response) {
                                            ApplicationClass.pets.remove(MainActivity.POSITION);
                                            Toast.makeText(getContext(), "Pet successfully removed!", Toast.LENGTH_SHORT).show();
                                            ivDelete.clearAnimation();

                                            if(getActivity().findViewById(R.id.layoutPortrait) != null) {
                                                getFragmentManager().beginTransaction()
                                                        .show(getFragmentManager().findFragmentById(R.id.fragmentList))
                                                        .hide(getFragmentManager().findFragmentById(R.id.fragmentDetail))
                                                        .hide(getFragmentManager().findFragmentById(R.id.fragmentButtons))
                                                        .commit();
                                            }

                                            ((MainActivity)getActivity()).dataChanged();

                                            if(getActivity().findViewById(R.id.layoutLand) != null) {
                                                if(!ApplicationClass.pets.isEmpty()) {
                                                    ((MainActivity)getActivity()).onItemSelected(0);
                                                }
                                                else
                                                    getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentById(R.id.fragmentButtons)).commit();
                                            }

                                            if(ApplicationClass.pets.isEmpty()) {

                                                getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentById(R.id.fragmentDetail)).commit();
                                                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                                                dialog.setMessage("You have not entered any data, please add new pets with plus sign");
                                                dialog.setPositiveButton("I got it!", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                });
                                                dialog.show();

                                            }


                                            MainActivity.POSITION = 9999;
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {

                                            Toast.makeText(getContext(), "Error: " +fault.getMessage(), Toast.LENGTH_SHORT).show();
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

        return view;
    }

}

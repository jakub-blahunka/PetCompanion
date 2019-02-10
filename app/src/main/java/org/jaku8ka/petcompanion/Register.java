package org.jaku8ka.petcompanion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class Register extends AppCompatActivity {

    ISetTextInFragment myText;
    FragmentManager fragmentManager;

    EditText etName, etMail, etPassword, etReEnter;
    CheckBox cbGDPR;
    TextView tvGDPR;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragment_register)).commit();
        myText = (ISetTextInFragment) fragmentManager.findFragmentById(R.id.fragment_register);

        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragment_register)).commit();

        etName = findViewById(R.id.etName);
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        etReEnter = findViewById(R.id.etReEnter);
        btnRegister = findViewById(R.id.btnRegister);
        tvGDPR = findViewById(R.id.tvGDPR);
        cbGDPR = findViewById(R.id.cbGDPR);

        tvGDPR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
                dialog.setMessage("Your personal data will be processed based on General Data Protection Regulation");
                dialog.setPositiveButton("I agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cbGDPR.setChecked(true);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                dialog.show();
            }

        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etName.getText().toString().isEmpty() || etMail.getText().toString().isEmpty() ||
                        etPassword.getText().toString().isEmpty() || etReEnter.getText().toString().isEmpty()) {
                    Toast.makeText(Register.this, "Please enter all details!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(etPassword.getText().toString().trim().equals(etReEnter.getText().toString().trim())) {

                        if(!cbGDPR.isChecked()) {
                            Toast.makeText(Register.this, "Please check GDPR statement!", Toast.LENGTH_SHORT).show();
                        } else  {
                            String name = etName.getText().toString().trim();
                            String mail = etMail.getText().toString().trim();
                            String password = etPassword.getText().toString().trim();
                            Boolean gdpr = cbGDPR.isChecked();

                            BackendlessUser user = new BackendlessUser();
                            user.setEmail(mail);
                            user.setPassword(password);
                            user.setProperty("name", name);
                            user.setProperty("gdpr", gdpr);

                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentById(R.id.fragment_register)).commit();

                            myText.showText("Registering user...please wait...");
                            Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                                @Override
                                public void handleResponse(BackendlessUser response) {

                                    Toast.makeText(Register.this, "User Succesfully registered!", Toast.LENGTH_SHORT).show();
                                    Register.this.finish();
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {

                                    Toast.makeText(Register.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragment_register)).commit();
                                }
                            });
                        }
                    }
                    else {
                        Toast.makeText(Register.this, "Please make sure that your password and re-type password is the same!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String etNameU = etName.getText().toString();
        String etMailU = etMail.getText().toString();
        String etPasswordU = etReEnter.getText().toString();
        String etReEnterU = etReEnter.getText().toString();
        Boolean checkedU = cbGDPR.isChecked();

        outState.putString("savedName", etNameU);
        outState.putString("savedMail", etMailU);
        outState.putString("savedPassword", etPasswordU);
        outState.putString("savedRetype", etReEnterU);
        outState.putBoolean("savedChecked", checkedU);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        String name = savedInstanceState.getString("savedName");
        String mail = savedInstanceState.getString("savedMail");
        String password = savedInstanceState.getString("savedPassword");
        String retype = savedInstanceState.getString("savedRetype");
        Boolean checked = savedInstanceState.getBoolean("savedChecked");

        etName.setText(name);
        etMail.setText(mail);
        etPassword.setText(password);
        etReEnter.setText(retype);
        cbGDPR.setChecked(checked);
    }
}

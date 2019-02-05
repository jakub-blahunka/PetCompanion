package org.jaku8ka.petcompanion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;

public class Login extends AppCompatActivity {


    ISetTextInFragment myText;
    FragmentManager fragmentManager;
    EditText etMail, etPassword;
    Button btnLogin, btnRegister;
    TextView tvReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragment_login)).commit();
        myText = (ISetTextInFragment) fragmentManager.findFragmentById(R.id.fragment_login);

        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tvReset = findViewById(R.id.tvReset);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etMail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    String mail = etMail.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();

                    fragmentManager.beginTransaction().show(fragmentManager.findFragmentById(R.id.fragment_login)).commit();
                    myText.showText("Logging you in...please wait...");

                    Backendless.UserService.login(mail, password, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {

                            ApplicationClass.user = response;
                            Toast.makeText(Login.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, MainActivity.class));
                            Login.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(Login.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragment_login)).commit();
                        }
                    }, true);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Login.this, Register.class));
            }
        });

        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etMail.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "Please enter your e-mail address in the e-mail field!", Toast.LENGTH_SHORT).show();
                } else  {
                    String mail = etMail.getText().toString().trim();

                    fragmentManager.beginTransaction().show(fragmentManager.findFragmentById(R.id.fragment_login)).commit();
                    myText.showText("Sending reset instructions...please wait...");

                    Backendless.UserService.restorePassword(mail, new AsyncCallback<Void>() {
                        @Override
                        public void handleResponse(Void response) {

                            Toast.makeText(Login.this, "Reset instructions sent to e-mail address!", Toast.LENGTH_SHORT).show();
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragment_login)).commit();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(Login.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragment_login)).commit();
                        }
                    });
                }
            }
        });

        fragmentManager.beginTransaction().show(fragmentManager.findFragmentById(R.id.fragment_login)).commit();
        myText.showText("Checking login credentials...please wait...");

        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {

                if(response) {
                    String userObjectId = UserIdStorageFactory.instance().getStorage().get();

                    myText.showText("Logging you in...please wait...");
                    Backendless.Data.of(BackendlessUser.class).findById(userObjectId, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {

                            ApplicationClass.user = response;
                            startActivity(new Intent(Login.this, MainActivity.class));
                            Login.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(Login.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragment_login)).commit();
                        }
                    });
                } else {
                    fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragment_login)).commit();
                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(Login.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.fragment_login)).commit();
            }
        });

    }

}

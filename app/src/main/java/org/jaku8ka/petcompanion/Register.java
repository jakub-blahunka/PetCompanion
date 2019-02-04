package org.jaku8ka.petcompanion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
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

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText etName, etMail, etPassword, etReEnter;
    CheckBox cbGDPR;
    TextView tvGDPR;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

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
                dialog.setMessage("GDPR STUPID STUFF I CANNOT FIND");
                dialog.setPositiveButton("WHATEVER", new DialogInterface.OnClickListener() {
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

                            showProgress(true);

                            tvLoad.setText("Registering user...please wait...");
                            Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                                @Override
                                public void handleResponse(BackendlessUser response) {

                                    Toast.makeText(Register.this, "User Succesfully registered!", Toast.LENGTH_SHORT).show();
                                    Register.this.finish();
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {

                                    Toast.makeText(Register.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    showProgress(false);
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

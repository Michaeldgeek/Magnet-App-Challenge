package lol.challenge.magnet.android.lol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.magnet.mmx.client.api.MMX;
import com.magnet.mmx.client.api.MMXUser;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class RegistrationActivity extends AppCompatActivity {

    final String TAG = "LoginActivity";

    EditText email, userName, passWord, confirmPassword;
    ActionProcessButton btnSignUp;

    private AtomicBoolean mLoginSuccess = new AtomicBoolean(false);
    private SharedPreferences settings;
    private boolean firstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        settings = getSharedPreferences("prefs", 0);
        firstRun = settings.getBoolean("firstRun", true);

        //GET YOUR WHISPER.PROPERTIES FROM YOUR SANDBOX INTO THE RAW FOLDER, THEN UNCOMMENT THIS SECTION
        //TO PREVENT APP FROM CRASHING

        // whisper.properties configures Magnet Message endpoints
//        MMX.init(this, R.raw.whisper);

        if (firstRun) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();

            email = (EditText) findViewById(R.id.signup_email_input);
            userName = (EditText) findViewById(R.id.signup_username_input);
            passWord = (EditText) findViewById(R.id.signup_password_input);
            confirmPassword = (EditText) findViewById(R.id.signup_confirm_password_input);

            btnSignUp = (ActionProcessButton) findViewById(R.id.button_signup);

            btnSignUp.setMode(ActionProcessButton.Mode.ENDLESS);

            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (email.getText().toString().length() == 0)
                        email.setError("An email is required");
                    else if (userName.getText().toString().length() == 0)
                        userName.setError("A username is required");
                    else if (passWord.getText().toString().length() == 0)
                        passWord.setError("A password is required");
//                Objects.equals(passWord.getText().toString(), confirmPassword.getText().toString())
                    else if (!passWord.getText().toString().equals(confirmPassword.getText().toString()))
                        confirmPassword.setError("Passwords are not the same");
                    else {
                        // set progress > 0 to start progress indicator animation
                        btnSignUp.setProgress(1);
                        String username = userName.getText().toString();
                        byte[] password = passWord.getText().toString().getBytes();
                        attemptRegister(username, password, true);
                    }
                }
            });
        } else {
            loadMainActivity();
        }
    }


    private void attemptRegister(final String user, final byte[] pass, final boolean isNewUser) {
        MMXUser mmxUser = new MMXUser.Builder().username(user).displayName(user).build();
        mmxUser.register(pass, new MMXUser.OnFinishedListener<Void>() {
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "attemptRegister() success");
                mLoginSuccess.set(true);

//                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();

                attemptLogin(user, pass);
            }

            public void onFailure(MMXUser.FailureCode failureCode, Throwable throwable) {
                if (MMXUser.FailureCode.REGISTRATION_INVALID_USERNAME.equals(failureCode)) {
                    Log.e(TAG, "attemptRegister() error: " + failureCode, throwable);
                    Toast.makeText(RegistrationActivity.this, "Sorry, that's not a valid username.", Toast.LENGTH_LONG).show();
                }
                if (MMXUser.FailureCode.REGISTRATION_USER_ALREADY_EXISTS.equals(failureCode)) {
                    if (isNewUser) {
                        Log.e(TAG, "attemptRegister() error: " + failureCode, throwable);
                        Toast.makeText(RegistrationActivity.this, "Sorry, this user already exists.", Toast.LENGTH_LONG).show();
                    } else {
                        attemptLogin(user, pass);
                    }
                }
                mLoginSuccess.set(false);
            }
        });
    }


    private void attemptLogin(String user, byte[] pass) {
        MMX.login(user, pass, new MMX.OnFinishedListener<Void>() {
            public void onSuccess(Void aVoid) {
                //if an EventListener has already been registered, start receiving messages
                MMX.start();

//                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();

                goToWhispererSelectActivity();

            }

            public void onFailure(MMX.FailureCode failureCode, Throwable throwable) {
                Log.e(TAG, "attemptLogin() error: " + failureCode, throwable);
                if (MMX.FailureCode.SERVER_AUTH_FAILED.equals(failureCode)) {
                    //login failed, probably an incorrect password
                    Toast.makeText(RegistrationActivity.this, "Invalid username and/or password.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void goToWhispererSelectActivity() {
        Intent intent;
        intent = new Intent(RegistrationActivity.this, WhisperersActivity.class);
        startActivity(intent);
    }

    private void loadMainActivity() {

        settings = getSharedPreferences("prefs", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("firstRun", false);
        editor.apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

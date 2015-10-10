package lol.challenge.magnet.android.lol;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.magnet.mmx.client.api.MMX;

public class LoginActivity extends AppCompatActivity {

    final String TAG = "LoginActivity";

    ActionProcessButton btnLogIn;
    Button buttonSignUp;
    EditText userName, passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //GET YOUR WHISPER.PROPERTIES FROM YOUR SANDBOX INTO THE RAW FOLDER, THEN UNCOMMENT THIS SECTION
        //TO PREVENT APP FROM CRASHING

        // whisper.properties configures Magnet Message endpoints
//        MMX.init(this, R.raw.whisper);

            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();

            userName = (EditText) findViewById(R.id.login_username_input);
            passWord = (EditText) findViewById(R.id.login_password_input);

            btnLogIn = (ActionProcessButton) findViewById(R.id.button_login);
            // you can display endless google like progress indicator
            btnLogIn.setMode(ActionProcessButton.Mode.ENDLESS);
            btnLogIn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (userName.getText().toString().length() == 0)
                                                    userName.setError("Please enter your username first");
                                                else if (passWord.getText().toString().length() == 0)
                                                    passWord.setError("Please enter your password");
                                                else {
                                                    // set progress > 0 to start progress indicator animation
                                                    btnLogIn.setProgress(1);

                                                    String username = userName.getText().toString();
                                                    byte[] password = passWord.getText().toString().getBytes();
                                                    attemptLogin(username, password);
                                                }
                                            }
                                        }
            );


            buttonSignUp = (Button) findViewById(R.id.signup_button);
            buttonSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                    startActivity(i);
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
                    Toast.makeText(LoginActivity.this, "Invalid username and/or password.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void goToWhispererSelectActivity() {
        Intent intent;
        intent = new Intent(LoginActivity.this, WhisperersActivity.class);
        startActivity(intent);
    }
//
// STILL UNDER CONSTRUCTION


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

package lol.challenge.magnet.android.lol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;

import lol.challenge.magnet.android.lol.slides.SampleSlide;


public class IntroActivity extends AppIntro2 {

    public SharedPreferences settings;
    public boolean firstRun;

    // Please DO NOT override onCreate. Use init
    @Override
    public void init(Bundle savedInstanceState) {

        // Add your slide's fragments here
        // AppIntro will automatically generate the dots indicator and buttons.
//        addSlide(first_fragment);
//        addSlide(second_fragment);
//        addSlide(third_fragment);
//        addSlide(fourth_fragment);
        settings = getSharedPreferences("prefs", 0);
        firstRun = settings.getBoolean("firstRun", true);

        if (firstRun) {
            addSlide(SampleSlide.newInstance(R.layout.intro_first));
            addSlide(SampleSlide.newInstance(R.layout.intro_second));
            addSlide(SampleSlide.newInstance(R.layout.intro_third));

            setFadeAnimation();
        } else {
            loadMainActivity();
        }

        // OPTIONAL METHODS
//         Override bar/separator color
//        setBarColor(Color.parseColor("#3F51B5"));
//        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button
//        showSkipButton(false);
        showDoneButton(true);

        // Turn vibration on and set intensity
        // NOTE: you will probably need to ask VIBRATE permesssion in Manifest
//        setVibrate(true);
//        setVibrateIntensity(30);
    }

//    @Override
//    public void onSkipPressed() {
//        // Do something when users tap on Skip button.
//    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        loadMainActivity();
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
}
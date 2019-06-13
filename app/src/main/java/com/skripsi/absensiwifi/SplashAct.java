package com.skripsi.absensiwifi;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashAct extends AppCompatActivity {
    Animation app_splash, bottom_to_top;
    ImageView splah_logo;
    TextView app_subtitle, app_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //load animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        bottom_to_top = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);

        //load element
        splah_logo = findViewById(R.id.splash_logo);
        app_title = findViewById(R.id.app_title);
        app_subtitle = findViewById(R.id.app_subtitle);

        //run animation
        splah_logo.startAnimation(app_splash);
        app_subtitle.startAnimation(bottom_to_top);
        app_title.startAnimation(bottom_to_top);

        // setting timer untuk dua detik
        Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // merubah activity ke activity lain
                Intent gotologin = new Intent(SplashAct.this, LoginAct.class);
                startActivity(gotologin);
                finish();

            }
        }, 2000 ); // 2000 ms = 2s
    }
}

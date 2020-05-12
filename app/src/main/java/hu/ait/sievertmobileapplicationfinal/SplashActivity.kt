package hu.ait.sievertmobileapplicationfinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.Thread.sleep

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var splashAnim = AnimationUtils.loadAnimation(this, R.anim.splash_anim)
        red.startAnimation(splashAnim)
        orange.startAnimation(splashAnim)
        yellow.startAnimation(splashAnim)
        green.startAnimation(splashAnim)
        blue.startAnimation(splashAnim)
        purple.startAnimation(splashAnim)
        shuttle.startAnimation(splashAnim)

        splashAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }
            override fun onAnimationEnd(animation: Animation?) {
                sleep(1000)
                val intent = Intent(this@SplashActivity, SearchActivity::class.java)
                startActivity(intent)
            }
            override fun onAnimationStart(animation: Animation?) {
            }
        })
    }
}

package org.jdcc.atrapamesipuedes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_final.*
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.test.runner.screenshot.ScreenCapture
import androidx.test.runner.screenshot.Screenshot.capture
import org.jdcc.atrapamesipuedes.Utility.shareGame
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class FinalActivity : AppCompatActivity(), View.OnClickListener {

    private var context = this
    private var score = 0
    private lateinit var mpApplause : MediaPlayer
    private lateinit var mpButton : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final)

        initActivity()
        initSound()
        bt_final_home.setOnClickListener(this)
        bt_final_share.setOnClickListener(this)
        bt_final_applause.setOnClickListener(this)
    }

    private fun initSound(){
        mpButton = MediaPlayer.create(this, R.raw.button)
        mpButton.isLooping = false

        mpApplause = MediaPlayer.create(this, R.raw.applause)
        mpApplause.isLooping = false

    }

    private fun initActivity(){
        val bundle = intent.extras
        score = bundle?.getInt("score")!!
        tv_score.text = score.toString()

        val prefs =  getSharedPreferences("prefs", Context.MODE_PRIVATE)
        var score = prefs.getInt("score", 0)
        tv_best_score.text = getString(R.string.level_menu) + " $score"
    }

    override fun onBackPressed() {                                                                  // Block back button
    }

    override fun onClick(v: View?) {
        when (v){
            bt_final_home -> {
                mpButton.start()
                mpApplause.stop()
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            bt_final_share -> {
                mpApplause.stop()
                shareGame(context)
            }
            bt_final_applause -> mpApplause.start()
            else -> Log.v("FinalActivity", "Error");
        }
    }

}
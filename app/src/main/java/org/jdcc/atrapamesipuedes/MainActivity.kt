package org.jdcc.atrapamesipuedes

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jdcc.atrapamesipuedes.Utility.shareGame


class MainActivity : AppCompatActivity() {

    private var context = this
    private lateinit var mpButton: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSound()
        buttons()
    }

    private fun initSound() {
        mpButton = MediaPlayer.create(this, R.raw.button)
        mpButton.isLooping = false
    }

    private fun buttons() {
        bt_play.setOnClickListener {
            mpButton.start()
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        bt_share.setOnClickListener {
            mpButton.start()
            shareGame(context)
        }
    }
}



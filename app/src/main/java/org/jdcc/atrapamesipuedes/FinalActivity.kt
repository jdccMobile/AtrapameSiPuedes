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
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class FinalActivity : AppCompatActivity(), View.OnClickListener {

    private var bitmap : Bitmap? = null
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

    private fun shareGame(){                                                                        // Share the game with a screenshot
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)

        var ssc : ScreenCapture = capture(this)    // capture the all activity
        bitmap = ssc.bitmap

        if(bitmap != null){
            var idGame = SimpleDateFormat("yyyy/MM/dd").format(Date())
            idGame = idGame.replace(":","")
            idGame = idGame.replace("/","")
            val path = saveImage(bitmap, "${idGame}.jpg")
            val bmpUri = Uri.parse(path)

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            shareIntent.putExtra(Intent.EXTRA_STREAM,bmpUri)
            var text = "Logré una puntuación de $score!! Podrás superarme??"    //TODO FALTA PONER EL ENLACE
            shareIntent.putExtra(Intent.EXTRA_TEXT,text)
            shareIntent.type = "image/png"

            val finalShareIntent = Intent.createChooser(shareIntent, "Escoge la aplicacion con la que compartir")
            finalShareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            this.startActivity(finalShareIntent)

        }

    }

    private fun saveImage(bitmap: Bitmap?, fileName: String) : String?{
        if(bitmap == null) return null
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q){
            val contentValues = ContentValues().apply{
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Screenshots")
            }

            val uri = this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (uri != null){
                this.contentResolver.openOutputStream(uri).use{
                    if(it == null) return@use

                    bitmap.compress(Bitmap.CompressFormat.PNG,85, it)
                    it.flush()
                    it.close()

                    // Add pic to gallery
                    MediaScannerConnection.scanFile(this,arrayOf(uri.toString()),null,null)
                }
            }
            return uri.toString()
        }

        val filePath = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES + "/Screenshots"
        ).absolutePath

        val dir = File(filePath)
        if(!dir.exists()) dir.mkdirs()
        val file = File(dir, fileName)
        val fOut = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.PNG,85, fOut)
        fOut.flush()
        fOut.close()

        MediaScannerConnection.scanFile(this,arrayOf(file.toString()),null,null)
        return filePath

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
                shareGame()
            }
            bt_final_applause -> mpApplause.start()
            else -> Log.v("FinalActivity", "Error");
        }
    }

}
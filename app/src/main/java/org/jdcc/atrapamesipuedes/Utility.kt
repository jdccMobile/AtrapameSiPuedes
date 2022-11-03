package org.jdcc.atrapamesipuedes

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.test.runner.screenshot.ScreenCapture
import androidx.test.runner.screenshot.Screenshot
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object Utility {

    fun shareGame(context: Activity){
        var bitmap : Bitmap? = null // Share the game with a screenshot
        ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
        ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)

        var ssc : ScreenCapture = Screenshot.capture(context)    // capture the all activity
        bitmap = ssc.bitmap

        if(bitmap != null){
            var idGame = SimpleDateFormat("yyyy/MM/dd").format(Date())
            idGame = idGame.replace(":","")
            idGame = idGame.replace("/","")
            val path = saveImage(context, bitmap, "${idGame}.jpg")
            val bmpUri = Uri.parse(path)

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            shareIntent.putExtra(Intent.EXTRA_STREAM,bmpUri)
            var text = "¿Serás del 5% que logra atrapar al rey?"    //TODO FALTA PONER EL ENLACE
            shareIntent.putExtra(Intent.EXTRA_TEXT,text)
            shareIntent.type = "image/png"

            val finalShareIntent = Intent.createChooser(shareIntent,"Escoge la aplicacion con la que compartir")
            finalShareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(finalShareIntent)

        }

    }

    fun saveImage(context: Activity, bitmap: Bitmap?, fileName: String) : String?{
        if(bitmap == null) return null
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q){
            val contentValues = ContentValues().apply{
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Screenshots")
            }

            val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (uri != null){
                context.contentResolver.openOutputStream(uri).use{
                    if(it == null) return@use

                    bitmap.compress(Bitmap.CompressFormat.PNG,85, it)
                    it.flush()
                    it.close()

                    // Add pic to gallery
                    MediaScannerConnection.scanFile(context,arrayOf(uri.toString()),null,null)
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

        MediaScannerConnection.scanFile(context,arrayOf(file.toString()),null,null)
        return filePath

    }

}
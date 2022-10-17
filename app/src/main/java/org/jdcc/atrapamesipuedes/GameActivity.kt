package org.jdcc.atrapamesipuedes

import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TableRow
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        initScreenGame()
    }

    private fun initScreenGame(){
        setSizeBoard()  // Set the size equally for each screen
        hideMessage()  // Hide de next level message
        randomPosition()    // Set randomly the king's cell
    }


    private fun setSizeBoard(){
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        var widthDp = (width / resources.displayMetrics.density)
        var lateralMarginDp = 0
        val widthCell = (widthDp - lateralMarginDp) / 8
        val heigthCell = widthCell

        var iv : ImageView
        for(i in 0..7){
           for(j in 0..7){
                iv = findViewById(resources.getIdentifier("iv_c$i$j","id",packageName))

               var height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heigthCell, resources.displayMetrics).toInt()
               var width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthCell, resources.displayMetrics).toInt()

               iv.layoutParams = TableRow.LayoutParams(width,height)
           }
       }
    }

    private fun hideMessage(){
        ll_next_level.visibility = View.INVISIBLE

    }

    private fun randomPosition() {

    }


}
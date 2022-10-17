package org.jdcc.atrapamesipuedes

import android.graphics.Color
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TableRow
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var countDown : CountDownTimer
    private var msInFuture : Long = 10000           // 10 seconds
    private var countDownInterval : Long = 1000     // 1 second
    private var extraMs : Long = 0
    private var kingCaught = false
    private lateinit var board : Array<IntArray>
    private var level = 1
    private var numberCells = 16

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        initScreenGame()
        buttons()
    }

    private fun initScreenGame(){
        setSizeBoard()  // Set the size equally for each screen
        hideMessage()  // Hide de next level message
        resetBoard()
        randomPosition()    // Set randomly the king's cell
        startTimer()        // Start the timer
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

    private fun resetBoard(){
        board = arrayOf(
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),     // 0 -> empty
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),     // 1 -> pawn
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),     // 2 -> rook
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),     // 3 -> knight
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),     // 4 -> bishop
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),     // 5 -> queen
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),     // 6 -> king
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        )

        // TODO SOLO EL PRTIMER NIVEL, LEUGO NO HAY QUE RESETAR LAS VISIBLES
        var iv : ImageView
        var x = 0
        var y = 0

        if(level == 1){ //Initial cells
            for(x in 0..7){
                for(y in 0..7){
                    iv = findViewById(resources.getIdentifier("iv_c$x$y","id",packageName))
                    if(x != 2 && x != 3 && x != 4 && x != 5) iv.visibility = View.INVISIBLE
                    if(y != 2 && y != 3 && y != 4 && y != 5) iv.visibility = View.INVISIBLE
                }
            }
        } else{     // Add a new random visible cell
            do{
                x = (0..7).random()
                y = (0..7).random()
                iv = findViewById(resources.getIdentifier("iv_c$x$y","id",packageName))
                // TODO COMPROBAR QUE ESTE AL LADO DE UNA CELDA VISIBLE
            } while(iv.visibility == View.VISIBLE)
            iv.visibility = View.VISIBLE
            numberCells++
        }
    }

    private fun randomPosition() {
        var typePiece = 0
        var iv: ImageView
        var i = 0
        var j = 0

        for (x in 0..7) {                                                                     // Randomize the pieces in the whole board
            for (y in 0..7) {
                iv = findViewById(resources.getIdentifier("iv_c$x$y", "id", packageName))
                typePiece = (0..5).random()
                when (typePiece) {
                    1 -> {
                        iv.setImageResource(R.drawable.ic_pawn)
                        board[x][y] = 1
                    }
                    2 -> {
                        iv.setImageResource(R.drawable.ic_rook)
                        board[x][y] = 2
                    }
                    3 -> {
                        iv.setImageResource(R.drawable.ic_knight)
                        board[x][y] = 3
                    }
                    4 -> {
                        iv.setImageResource(R.drawable.ic_bishop)
                        board[x][y] = 4
                    }
                    5 -> {
                        iv.setImageResource(R.drawable.ic_queen)
                        board[x][y] = 5
                    }
                    else -> board[x][y] = 0
                }
            }
        }
        do{                                                                                         // Set the king's position
            i = (0..7).random()
            j = (0..7).random()
            iv = findViewById(resources.getIdentifier("iv_c$i$j", "id", packageName))
        } while(iv.visibility != View.VISIBLE)
        iv.setImageResource(R.drawable.ic_king_game)
        board[i][j] = 6
    }

    private fun startTimer(){
        msInFuture += extraMs
        countDown = object : CountDownTimer(msInFuture, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                tv_time_bottom.text = (millisUntilFinished / 1000).toString() + " ''"
                if(kingCaught){
                    extraMs = millisUntilFinished
                    countDown.cancel()
                }
                if (millisUntilFinished / 1000 < 6 ) tv_time_bottom.setTextColor(Color.parseColor("#FF0000")) // the last 5 seconds are red
            }

            override fun onFinish() {
                tv_time_bottom.text = getString(R.string.game_lose)
                ll_next_level.visibility = View.VISIBLE             // TODO DERROTA
            }

        }

        countDown.start()
    }

    private fun buttons(){
        iv_c00.setOnClickListener(this)
        iv_c01.setOnClickListener(this)
        iv_c02.setOnClickListener(this)
        iv_c03.setOnClickListener(this)
        iv_c04.setOnClickListener(this)
        iv_c05.setOnClickListener(this)
        iv_c06.setOnClickListener(this)
        iv_c07.setOnClickListener(this)

        iv_c10.setOnClickListener(this)
        iv_c11.setOnClickListener(this)
        iv_c12.setOnClickListener(this)
        iv_c13.setOnClickListener(this)
        iv_c14.setOnClickListener(this)
        iv_c15.setOnClickListener(this)
        iv_c16.setOnClickListener(this)
        iv_c17.setOnClickListener(this)

        iv_c20.setOnClickListener(this)
        iv_c21.setOnClickListener(this)
        iv_c22.setOnClickListener(this)
        iv_c23.setOnClickListener(this)
        iv_c24.setOnClickListener(this)
        iv_c25.setOnClickListener(this)
        iv_c26.setOnClickListener(this)
        iv_c27.setOnClickListener(this)

        iv_c30.setOnClickListener(this)
        iv_c31.setOnClickListener(this)
        iv_c32.setOnClickListener(this)
        iv_c33.setOnClickListener(this)
        iv_c34.setOnClickListener(this)
        iv_c35.setOnClickListener(this)
        iv_c36.setOnClickListener(this)
        iv_c37.setOnClickListener(this)

        iv_c40.setOnClickListener(this)
        iv_c41.setOnClickListener(this)
        iv_c42.setOnClickListener(this)
        iv_c43.setOnClickListener(this)
        iv_c44.setOnClickListener(this)
        iv_c45.setOnClickListener(this)
        iv_c46.setOnClickListener(this)
        iv_c47.setOnClickListener(this)

        iv_c50.setOnClickListener(this)
        iv_c51.setOnClickListener(this)
        iv_c52.setOnClickListener(this)
        iv_c53.setOnClickListener(this)
        iv_c54.setOnClickListener(this)
        iv_c55.setOnClickListener(this)
        iv_c56.setOnClickListener(this)
        iv_c57.setOnClickListener(this)

        iv_c60.setOnClickListener(this)
        iv_c61.setOnClickListener(this)
        iv_c62.setOnClickListener(this)
        iv_c63.setOnClickListener(this)
        iv_c64.setOnClickListener(this)
        iv_c65.setOnClickListener(this)
        iv_c66.setOnClickListener(this)
        iv_c67.setOnClickListener(this)

        iv_c70.setOnClickListener(this)
        iv_c71.setOnClickListener(this)
        iv_c72.setOnClickListener(this)
        iv_c73.setOnClickListener(this)
        iv_c74.setOnClickListener(this)
        iv_c75.setOnClickListener(this)
        iv_c76.setOnClickListener(this)
        iv_c77.setOnClickListener(this)


    }

    override fun onClick(v: View?) {        // Just clicks on cells
        var id = v?.tag.toString()
        var x = id.subSequence(1,2).toString().toInt()
        var y = id.subSequence(2,3).toString().toInt()

        if(board[x][y] == 6){
            //TODO AÑADIR PANTALLA PARA EL NUEVO NIVEL
            val toast = Toast.makeText(this,"OK", Toast.LENGTH_SHORT).show()
        }

    }


}
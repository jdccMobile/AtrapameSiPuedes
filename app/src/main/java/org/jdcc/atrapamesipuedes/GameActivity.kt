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
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var countDown : CountDownTimer
    private var msInFuture : Long = 10000           // 10 seconds
    private var countDownInterval : Long = 1000     // 1 second
    private var extraMs : Long = 0
    private var kingCatched = false
    private lateinit var board : Array<IntArray>
    private var level = 1

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
            } while(iv.visibility == View.VISIBLE)
            iv.visibility = View.VISIBLE
        }
    }

    private fun randomPosition() {
        var numberOfPieces = 0
        var typePiece = 0
        var x = 0
        var y = 0
        var iv : ImageView
        numberOfPieces = (5..9).random()    //TODO EL 5 ES LA MITAD DE LAS VISIBLES + 2, EL NUEVE ES EL NUMERO DE CASILLAS VISIBLES

        for(i in 0..numberOfPieces){
            do {
                x = (0..7).random()
                y = (0..7).random()
            } while(board[x][y] != 0)           //TODO Y QUE LA CASILLA SEA VISIBLE

            iv = findViewById(resources.getIdentifier("iv_c$x$y","id",packageName))
            if(i == 0){
                iv.setImageResource(R.drawable.ic_king_game)
                board[x][y] = 6
            }
            else {
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

    }

    private fun startTimer(){
        msInFuture += extraMs
        countDown = object : CountDownTimer(msInFuture, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                tv_time_bottom.text = (millisUntilFinished / 1000).toString() + " ''"
                if(kingCatched){
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
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }


}
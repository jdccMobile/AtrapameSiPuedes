package org.jdcc.atrapamesipuedes

import android.content.Intent
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

    private var msInFuture: Long = 0      // 6 seconds
    private var msExtra : Long = 0
    private lateinit var countDown : CountDownTimer
    private var kingCaught = false
    private var lost = false
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
        resetBoard()
        startGame()
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

    private fun resetBoard(){
        msInFuture = 6000
        board = arrayOf(
            intArrayOf(7, 7, 7, 7, 7, 7, 7, 7),     // 0 -> empty/visible
            intArrayOf(7, 7, 7, 7, 7, 7, 7, 7),     // 1 -> pawn
            intArrayOf(7, 7, 7, 7, 7, 7, 7, 7),     // 2 -> rook
            intArrayOf(7, 7, 7, 7, 7, 7, 7, 7),     // 3 -> knight
            intArrayOf(7, 7, 7, 7, 7, 7, 7, 7),     // 4 -> bishop
            intArrayOf(7, 7, 7, 7, 7, 7, 7, 7),     // 5 -> queen
            intArrayOf(7, 7, 7, 7, 7, 7, 7, 7),     // 6 -> king
            intArrayOf(7, 7, 7, 7, 7, 7, 7, 7)      // 7 -> Invisible Cell
        )
        var iv : ImageView
        var x = 0
        var y = 0
        for(i in 0..7){                                                                       // Set all the cells as invisible (7)
            for(j in 0..7){
                iv = findViewById(resources.getIdentifier("iv_c$i$j","id",packageName))
                iv.visibility = View.INVISIBLE

            }
        }
    }


    private fun startGame(){
        tv_level_bottom.text = level.toString()
        tv_time_bottom.setTextColor(Color.parseColor("#FFFFFFFF"))
        ll_next_level.visibility = View.INVISIBLE
        kingCaught = false
        lost = false

        visibleCells()
        randomPosition()    // Set randomly the king's cell
        startTimer()        // Start the timer
    }

    private fun visibleCells(){
        var iv : ImageView
        var x = 0
        var y = 0
        var auxRandom = 0
        var cont = 0
        var numberNewCell = 0
        val freeCell: MutableList<String> = mutableListOf()

        for(i in 0..7){
            for(j in 0..7) {
                if(board[i][j] == 7) freeCell.add("$i$j")                                           // Add free cells to the list
            }
        }
        if (level == 1) numberNewCell = 24
        else numberNewCell = 2                                                                      // 2 new cells each level

        do{
            auxRandom = (0..freeCell.size).random()                                           // Choose a random free cell from the list
            x = freeCell.elementAt(auxRandom).subSequence(0,1).toString().toInt()
            y = freeCell.elementAt(auxRandom).subSequence(1,2).toString().toInt()
            freeCell.removeAt(auxRandom)
            iv = findViewById(resources.getIdentifier("iv_c$x$y", "id", packageName))
            iv.visibility = View.VISIBLE
            board[x][y] = 0
            cont++

        } while(cont != numberNewCell)

    }

    private fun randomPosition() {
        var typePiece = 0
        var iv: ImageView
        var i = 0
        var j = 0

        for (x in 0..7) {                                                                     // Randomize the pieces in the whole visible board
            for (y in 0..7) {
                iv = findViewById(resources.getIdentifier("iv_c$x$y", "id", packageName))
                if(iv.visibility == View.VISIBLE){
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
                        else -> {
                            board[x][y] = 0
                            iv.setImageResource(0)
                        }
                    }
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
        var countDownInterval : Long = 1000    // 1 second

        countDown = object : CountDownTimer(msInFuture, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                msExtra = millisUntilFinished
                if ((millisUntilFinished / 1000) < 6 ) tv_time_bottom.setTextColor(Color.parseColor("#FF0000")) // the last 5 seconds are red
                tv_time_bottom.text = (millisUntilFinished / 1000).toString() + " ''"
            }

            override fun onFinish() {
                countDown.cancel()
                lost = true
                ll_next_level.visibility = View.VISIBLE
                tv_title.text = getString(R.string.lose_menu)
                tv_subtitle.text = getString(R.string.level_menu) + " ${level-1}"
                ib_next_level.setImageResource(R.drawable.ic_home_black)
            }
        }

        countDown.start()

    }

    private fun buttons(){
        ib_next_level.setOnClickListener(this)

        if(!lost){
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

    }

    override fun onClick(v: View?) {        // Just clicks on cells
        var id = v?.tag.toString()

        if(id == "ib_next_level"){
            if(!lost){
                if(level == 20){                                                                    // final level
                    var intent = Intent(this, FinalActivity::class.java)
                    startActivity(intent)
                } else{
                    level++
                    startGame()
                }
            }else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        else{
            var x = id.subSequence(1,2).toString().toInt()
            var y = id.subSequence(2,3).toString().toInt()

            if(board[x][y] == 6){
                ll_next_level.visibility = View.VISIBLE
                if(level == 20) tv_title.text = getString(R.string.final_win_menu)
                else tv_title.text = getString(R.string.win_menu)
                ib_next_level.setImageResource(R.drawable.ic_arrow_next_black)
                tv_subtitle.text = getString(R.string.level_menu) + " ${level}"
                kingCaught = true
                countDown.cancel()
                msInFuture = msExtra  + 2000                                                        // Add 2 extra ms each level
            }
        }


    }


}
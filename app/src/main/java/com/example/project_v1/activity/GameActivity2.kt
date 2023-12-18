package com.example.project_v1.activity

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eunly00.MachTheTanghuru.models.BoardSize
import com.eunly00.MachTheTanghuru.models.MemoryCard
import com.eunly00.MachTheTanghuru.models.MemoryGame
import com.example.project_v1.R
import com.example.project_v1.utils.DEFAULT_ICONS
import com.google.android.material.snackbar.Snackbar

class GameActivity2 : AppCompatActivity() {
    companion object {
        private const val TAG = "GameActivity_2"
    }

    private lateinit var textViewTimer: TextView
    private lateinit var clRoot: ConstraintLayout
    private lateinit var rvBoard: RecyclerView // late init = late initialization
    private lateinit var tvNumMoves: TextView // lateinit set in onCreate method by android not on construction
    private lateinit var tvNumPairs: TextView

    private lateinit var memoryGame: MemoryGame
    private lateinit var adapter: MemoryBoardAdapter
    private var boardSize: BoardSize = BoardSize.EASY
    private lateinit var countDownTimer: CountDownTimer
    private var initialCountdown: Long = 15000 // 15 seconds


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game2)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        textViewTimer = findViewById(R.id.textViewTimer)
        clRoot = findViewById(R.id.clRoot)
        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)

        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImages = (chosenImages + chosenImages).shuffled() // IMPORTANT FOR CHANGING LATER**************************
        val memoryCards = randomizedImages.map{ MemoryCard(it) }
        initCountdownTimer()
        setupBoard()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_game, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.mi_refresh -> {
                if (memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()){
                    showAlertDialog("Quit your current game?", null, View.OnClickListener { setupBoard() })
                } else {
                    setupBoard()
                }
                return true
            }
            R.id.mi_new_size -> {
                showNewSizeDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showNewSizeDialog() {
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val radioGroupSize = boardSizeView.findViewById<RadioGroup>(R.id.radioGroup)
        when (boardSize) {
            BoardSize.EASY -> radioGroupSize.check(R.id.rbEasy)
            BoardSize.MEDIUM -> radioGroupSize.check(R.id.rbMedium)
            BoardSize.HARD -> radioGroupSize.check(R.id.rbHard)
        }
        showAlertDialog("모드를 선택하세요!", boardSizeView, View.OnClickListener {
            // Set a new value for the board size
            boardSize = when (radioGroupSize.checkedRadioButtonId){
                R.id.rbEasy -> BoardSize.EASY
                R.id.rbMedium -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            setupBoard()
        })
    }

    private fun showAlertDialog(title: String, view: View?, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK") {_, _->
                positiveClickListener.onClick(null)
            }.show()
    }

    private fun setupBoard() {
        countDownTimer.cancel()
        initCountdownTimer()
        when (boardSize){
            BoardSize.EASY -> {
                tvNumMoves.text = "Easy 모드 "
                tvNumPairs.text = "짝: 0 / 4"
            }
            BoardSize.MEDIUM -> {
                tvNumMoves.text = "Medium 모드"
                tvNumPairs.text = "짝: 0 / 9"
            }
            BoardSize.HARD -> {
                tvNumMoves.text = "Hard 모드"
                tvNumPairs.text = "짝: 0 / 12"
            }
        }
        tvNumPairs.setTextColor(ContextCompat.getColor(this, R.color.color_progress_none))
        memoryGame = MemoryGame(boardSize)
        adapter = MemoryBoardAdapter(this, boardSize, memoryGame.cards, object: MemoryBoardAdapter.CardClickListener {
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }

        }) // Adapter - provide a binding for the data set to the views of the RecyclerView (rv - the big card space)
        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth()) // layout manager - measures and positions items and views
        countDownTimer.start()

    }


    private fun showAlertDialog(
        title: String,
        message: String,
        positiveButtonText: String,
        positiveClickListener: View.OnClickListener,
        negativeButtonText: String,
        negativeClickListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ ->
                positiveClickListener.onClick(null)
            }
            .setNegativeButton(negativeButtonText, negativeClickListener)
            .show()
    }
    private fun showCongratulationsDialog() {
        AlertDialog.Builder(this)
            .setTitle("축하합니다!")
            .setMessage("전부 다 뒤집었어요!")
            .setPositiveButton("재시작") { _, _ ->
                setupBoard()
            }
            .setNegativeButton("취소", null)
            .show()
    }
    private fun initCountdownTimer() {
        countDownTimer = object : CountDownTimer(initialCountdown, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update UI with remaining time
                val secondsRemaining = millisUntilFinished / 1000
                textViewTimer.text = "Time Remaining: $secondsRemaining seconds"
            }

            override fun onFinish() {
                // Check if all cards are flipped
                if (memoryGame.haveWonGame()) {
                    showCongratulationsDialog()
                } else {
                    // Game over, show alert dialog
                    showAlertDialog(
                        "게임 종료",
                        "시간이 종료되었습니다.\n당신의 점수는: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()} 다시 플레이 하시겠어요?",
                        "재시작",
                        View.OnClickListener {
                            setupBoard()
                        },
                        "취소",
                        DialogInterface.OnClickListener { _, _ ->
                            // User clicked "취소"
                            // Navigate back to the previous screen
                            onBackPressed()
                        }
                    )
                }
            }
        }
    }

            private fun updateGameWithFlip(position: Int) {
                // Error checking
                if (memoryGame.haveWonGame()) {
                    // Alert the user of an invalid move
                    //Snackbar.make(clRoot, "이미 이겼어요!", Snackbar.LENGTH_LONG).show()
                    showCongratulationsDialog()
                    return
                }
                if (memoryGame.isCardFaceUp(position)) {
                    // Alert the user of an invalid move
                    Snackbar.make(clRoot, "불가능한 움직임입니다!", Snackbar.LENGTH_SHORT).show()
                    return
                }
                // Actually flip over the card
                if (memoryGame.flipCard(position)) {
                    Log.i(TAG, "짝을 찾으세요! 이만큼 찾았어요: ${memoryGame.numPairsFound}")
                    val color = ArgbEvaluator().evaluate(
                        memoryGame.numPairsFound.toFloat() / boardSize.getNumPairs(),
                        ContextCompat.getColor(this, R.color.color_progress_none),
                        ContextCompat.getColor(this, R.color.color_progress_full)
                    ) as Int
                    tvNumPairs.setTextColor(color)
                    tvNumPairs.text = "짝: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"
                    if (memoryGame.haveWonGame()) {
                        Snackbar.make(clRoot, "이겼어! 축하해!!.", Snackbar.LENGTH_LONG).show()
                        finish()
                    }
                }
                tvNumMoves.text = "움직인횟수: ${memoryGame.getNumMoves()}"
                adapter.notifyDataSetChanged()
            }

            override fun onResume() {
                super.onResume()
                countDownTimer.start()
            }

            override fun onPause() {
                super.onPause()
                countDownTimer.cancel()
            }
        }


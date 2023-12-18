package com.example.project_v1.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AlertDialog
import com.example.project_v1.R
import com.example.project_v1.databinding.ActivityGame3Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Random

class GameActivity3 : AppCompatActivity(), Balloon.BalloonListener {

    // Initialize score with a default value
    override var score: Int = 0

    private lateinit var binding: ActivityGame3Binding
    var mScreenHeight: Int? = null
    var mBalloon = ArrayList<Balloon>()
    lateinit var mBalloonColors: List<Int>
    var screenWidth: Int? = null
    lateinit var mContentView: ViewGroup
    var balloonsLaunched: Int = 0
    lateinit var burstRandomList: List<String>
    lateinit var burstRandom: String
    lateinit var mBalloonImageResources: List<Pair<Int, Int>>
    private var countdownTimer: CountDownTimer? = null
    private var gameDuration: Long = 10000 // 60 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGame3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setBackgroundDrawableResource(R.drawable.background)
        mContentView = binding.root
        screenWidth = mContentView.width
        mScreenHeight = mContentView.height

        mBalloonImageResources = listOf(
            R.drawable.tanghuru_1 to 1,
            R.drawable.tanghuru_2 to 2,
            R.drawable.tanghuru_3 to 3,
            R.drawable.tanghuru_4 to 4,
            R.drawable.tanghuru_5 to 5,
            R.drawable.tanghuru_6 to 6,
            R.drawable.tanghuru_7 to 7,
            R.drawable.tanghuru_8 to 8
            // Add more image resources as needed
        )

        binding.startGame.setOnClickListener {
            startGame()
        }

        val observer: ViewTreeObserver = mContentView.viewTreeObserver
        if (observer.isAlive) {
            observer.addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {
                mContentView.viewTreeObserver.removeOnGlobalLayoutListener { this }
                mScreenHeight = mContentView.height
                screenWidth = mContentView.width
            })
        }
    }
    private fun startGame() {
        score = 0
        binding.tochTV.text = "Score: $score"

        countdownTimer?.cancel() // Cancel the existing timer if any
        countdownTimer = object : CountDownTimer(gameDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update the countdown display
                val secondsRemaining = millisUntilFinished / 1000
                binding.timerTextView.text = "Time: $secondsRemaining s"
            }

            override fun onFinish() {
                // Game over, perform required actions
                showGameOverDialog()
            }
        }

        GlobalScope.launch {
            balloonLauncher(2)
        }

        // Start the countdown timer
        countdownTimer?.start()
    }
    private fun showGameOverDialog() {
        runOnUiThread {
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Your score: $score\nDo you want to restart?")
                .setPositiveButton("Restart") { _, _ ->
                    startGame()
                }
                .setNegativeButton("Cancel") { _, _ ->
                    // Handle cancellation
                    finish()
                }
                .setCancelable(false)
                .create()

            alertDialog.show()
        }
    }

    private suspend fun balloonLauncher(balloonLimit: Int) {
        balloonsLaunched = 0
        while (balloonsLaunched < balloonLimit) {
            val random = Random(Date().time)
            val xPosition = random.nextInt(screenWidth!! - 150)
            withContext(Dispatchers.Main) {
                launchBalloon(xPosition.toFloat())
            }
            balloonsLaunched++
            val delay = random.nextInt(500) + 500
            delay(delay.toLong())
        }
    }

    private fun launchBalloon(x: Float) {
        val (imageResource, balloonScore) = mBalloonImageResources.random()
        val balloon = Balloon(this@GameActivity3, listOf(imageResource), balloonScore, 100)
        balloon.x = x
        balloon.y = mScreenHeight!!.toFloat() + balloon.height
        mContentView.addView(balloon)
        balloon.releaseBalloon(mScreenHeight!!, 3000)
    }

    override fun popBalloon(balloon: Balloon?, touched: Boolean, color: Int, balloonScore: Int) {
        mContentView.removeView(balloon)
        if (touched) {
            score += balloonScore
            binding.tochTV.text = "Score: $score"
            GlobalScope.launch {
                balloonLauncher(1)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        countdownTimer?.cancel() // Cancel the timer to avoid leaks
    }
}


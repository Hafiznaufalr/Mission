package com.raywenderlich.timefighter.hafiznaufalr

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var tapMeButton: ExtendedFloatingActionButton
    private lateinit var gameScoreTextView: TextView
    private lateinit var timeLeftTextView: TextView

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tapMeButton = findViewById(R.id.tapMeButton)
        gameScoreTextView = findViewById(R.id.gameScoreTextView)
        timeLeftTextView = findViewById(R.id.timeLifeTextView)
        setViewModel()
        observeScore()
        observeTime()

        tapMeButton.setOnClickListener { view ->
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            view.startAnimation(bounceAnimation)
            mainViewModel.incrementScore()
        }
    }

    private fun observeTime() {
        mainViewModel.time.observe(this, Observer { timeLeft ->
            timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
        })
    }

    private fun observeScore() {
        mainViewModel.textScore.observe(this, Observer {
            when (it.status) {
                Status.RUNNING -> {
                    val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink)
                    gameScoreTextView.startAnimation(blinkAnimation)

                    it.data.let { score ->
                        gameScoreTextView.text = getString(R.string.yourScore, score)
                    }
                }
                Status.DONE -> {
                    it.data.let { score ->
                        tapMeButton.isClickable = false
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle(getString(R.string.gameOverMessage, score))
                        builder.setPositiveButton("Okay") { dialog, _ -> dialog.dismiss() }
                        builder.setOnDismissListener {
                            tapMeButton.isClickable = true
                        }
                        builder.create().show()
                    }

                }
            }

        })

    }

    private fun setViewModel() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.actionAbout) {
            showInfo()
        }
        return true
    }

    private fun showInfo() {
        val dialogTitle = getString(R.string.aboutTitle, BuildConfig.VERSION_NAME)
        val dialogMessage = getString(R.string.aboutMessage)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy Called")
    }
}

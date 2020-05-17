package com.raywenderlich.timefighter.hafiznaufalr

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private lateinit var countDownTimer: CountDownTimer
    private var score = 0
    private var gameStarted = false
    private var initialCountDown: Long = 60000
    private var countDownInterval: Long = 1000

    val textScore = MutableLiveData<Resource<Int>>()
    val time = MutableLiveData<Long>()

    init {
        resetGame()
    }

    fun incrementScore() {
        if (!gameStarted) {
            startGame()
        }
        score += 1
        textScore.postValue(Resource.running(score))
    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    private fun resetGame() {
        score = 0
        textScore.postValue(Resource.running(score))

        val initialTimeLeft = initialCountDown / 1000
        time.postValue(initialTimeLeft)

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / 1000
                time.postValue(timeLeft)
            }

            override fun onFinish() {
                endGame()
            }
        }
        gameStarted = false
    }

    private fun endGame() {
        /**
         *  THE REASON WHY I ADD DELAY IN THIS FUNCTION
         *  BECAUSE I NEED LITTLE TIME FOR SCORE IN STATE 'DONE' FOR SHOWING DIALOG
         *  BECAUSE IF I'AM NOT ADD DELAY, THE SCORE WILL BE FAST CHANGED IN ZERO (BECAUSE CALLING FUN RESET GAME).
         */
        viewModelScope.launch {
            textScore.postValue(Resource.done(score))
            delay(100)
            resetGame()
        }

    }
}
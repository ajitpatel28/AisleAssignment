package com.ajit.aisleassignment.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajit.aisleassignment.data.models.OtpResponse
import com.ajit.aisleassignment.data.repository.Repository
import com.ajit.aisleassignment.utils.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class OtpViewModel(private val repository: Repository) : ViewModel() {
    private val _otpResponse = MutableLiveData<UiState<OtpResponse>>()
    val otpResponse: LiveData<UiState<OtpResponse>> = _otpResponse


    private val _timer = MutableLiveData<String>()
    val timer: LiveData<String> = _timer

    private var otpTimerJob: Job? = null


    fun verifyOtp(number: String, otp: String) {
        viewModelScope.launch {
            _otpResponse.value = UiState.Loading
            val response = repository.verifyOtp(number, otp)
            _otpResponse.value = response
        }
    }

    fun resendOtp() {
        startOtpTimer(60)
    }

    fun startOtpTimer(durationInSeconds: Int) {
        otpTimerJob?.cancel()
        otpTimerJob = viewModelScope.launch {
            var seconds = durationInSeconds
            while (seconds >= 0) {
                _timer.value = getTimerFormattedString(seconds)
                delay(1000)
                seconds--
            }
        }
    }

    private fun getTimerFormattedString(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
    }

}
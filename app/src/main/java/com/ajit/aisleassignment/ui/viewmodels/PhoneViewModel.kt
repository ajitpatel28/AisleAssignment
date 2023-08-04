package com.ajit.aisleassignment.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajit.aisleassignment.data.models.PhoneNumberResponse
import com.ajit.aisleassignment.data.repository.Repository
import com.ajit.aisleassignment.utils.UiState
import kotlinx.coroutines.launch


class PhoneViewModel(private val repository: Repository) : ViewModel() {
    private val _phoneNumberResponse = MutableLiveData<UiState<PhoneNumberResponse>>()
    val phoneNumberResponse: LiveData<UiState<PhoneNumberResponse>> = _phoneNumberResponse

    fun phoneNumberLogin(number: String) {
        viewModelScope.launch {
            _phoneNumberResponse.value = UiState.Loading
//            Log.e("view","$number")
            val response = repository.phoneNumberLogin(number)
            _phoneNumberResponse.value = response
        }
    }

    fun validatePhoneNumber(countryCode: String, mobileNumber: String): Boolean {
        val countryCodeRegex = "^\\+[1-9]\\d{0,2}\$".toRegex()
        val mobileNumberRegex = "^[1-9]\\d{9}\$".toRegex()

        val isValidCountryCode = countryCode.matches(countryCodeRegex)
        val isValidMobileNumber = mobileNumber.matches(mobileNumberRegex)

        val isValidMobileNumberLength = mobileNumber.length == 10

        return isValidCountryCode && isValidMobileNumber && isValidMobileNumberLength
    }

}

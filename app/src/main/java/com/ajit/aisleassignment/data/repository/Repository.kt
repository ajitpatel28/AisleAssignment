package com.ajit.aisleassignment.data.repository

import android.util.Log
import com.ajit.aisleassignment.data.models.*
import com.ajit.aisleassignment.data.remote.ApiService
import com.ajit.aisleassignment.utils.UiState
import retrofit2.Response


class Repository(private val apiService: ApiService) {

    suspend fun phoneNumberLogin(number: String): UiState<PhoneNumberResponse> {
        val request = PhoneNumberRequest(number)
        return executeApiCall {
            apiService.phoneNumberLogin(request)
        }
    }

    suspend fun verifyOtp(number: String, otp: String): UiState<OtpResponse> {
        val request = OtpRequest(number, otp)
        return executeApiCall {
            apiService.verifyOtp(request)
        }
    }

    suspend fun getNotes(authToken: String): UiState<NotesResponse> {
        return executeApiCall {
            apiService.getNotes(authToken)
        }
    }

    private suspend fun <T : Any> executeApiCall(apiCall: suspend () -> Response<T>): UiState<T> {
        try {
            val response = apiCall()
            return if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    UiState.Success(body)
                } else {
                    UiState.Failure("Response body is null.")
                }
            } else {
                UiState.Failure(response.errorBody()?.string())
            }
        } catch (e: Exception) {
            return UiState.Failure(e.message)
        }
    }
}
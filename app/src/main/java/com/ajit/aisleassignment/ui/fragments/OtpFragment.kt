package com.ajit.aisleassignment.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ajit.aisleassignment.databinding.FragmentOtpBinding
import com.ajit.aisleassignment.ui.viewmodels.OtpViewModel
import com.ajit.aisleassignment.utils.UiState
import com.ajit.aisleassignment.utils.hide
import com.ajit.aisleassignment.utils.show
import com.ajit.aisleassignment.utils.toast
import org.koin.android.ext.android.inject


class OtpFragment : Fragment() {


    private lateinit var binding : FragmentOtpBinding
    private val otpViewModel by inject<OtpViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = OtpFragmentArgs.fromBundle(requireArguments())
        val countryCode = args.countryCode
        val mobileNumber = args.mobileNumber
        val number = countryCode + mobileNumber

        binding.tvCountryCode.text = countryCode
        binding.tvMobileNumber.text = mobileNumber

        binding.btnEdit.setOnClickListener {
            val action = OtpFragmentDirections.actionOtpFragmentToPhoneNumberFragment()
            findNavController().navigate(action)
        }


        binding.btnContinue.setOnClickListener {
            val otp = binding.etOtp.text.toString()

//            Log.e("otp", "$otp")
            otpViewModel.verifyOtp( number,otp)
        }

        binding.btnResendOtp.setOnClickListener {
            otpViewModel.resendOtp()
        }

        otpViewModel.startOtpTimer(60)

        otpViewModel.timer.observe(viewLifecycleOwner) { timerValue ->
            binding.tvTimer.text = timerValue
            if (timerValue == "00:00") {
                binding.tvTimer.hide()
                binding.btnResendOtp.show()
            } else {
                binding.tvTimer.show()
                binding.btnResendOtp.hide()
            }
        }

        otpViewModel.otpResponse.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
//                    Log.e("otp", "${uiState.data}")
                    val token = uiState.data.token
                    val action = OtpFragmentDirections.actionOtpFragmentToNotesFragment(
                        token = token
                    )
                    findNavController().navigate(action)
                }
                is UiState.Failure -> {
                    toast("Please enter correct otp")
//                    Log.e("otp","${uiState.error}")
                }
            }
        }
    }
}
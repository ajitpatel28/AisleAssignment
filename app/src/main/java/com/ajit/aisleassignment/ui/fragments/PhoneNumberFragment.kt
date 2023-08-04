package com.ajit.aisleassignment.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ajit.aisleassignment.R
import com.ajit.aisleassignment.databinding.FragmentPhoneNumberBinding
import com.ajit.aisleassignment.ui.viewmodels.PhoneViewModel
import com.ajit.aisleassignment.utils.UiState
import com.ajit.aisleassignment.utils.hide
import com.ajit.aisleassignment.utils.show
import com.ajit.aisleassignment.utils.toast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhoneNumberFragment : Fragment() {


    private lateinit var binding: FragmentPhoneNumberBinding

    private val phoneViewModel by inject<PhoneViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhoneNumberBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.btnContinue.setOnClickListener {
            val countryCode = binding.etCountryCode.text.toString()
            val mobileNumber = binding.etPhoneNumber.text.toString()
            val number = countryCode + mobileNumber


            if (phoneViewModel.validatePhoneNumber(countryCode, mobileNumber)) {
//                Log.e("phone","$number")
                phoneViewModel.phoneNumberLogin(number)
            } else {
                toast("Please check you number and try again")
            }
        }

        phoneViewModel.phoneNumberResponse.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                    binding.btnContinue.hide()
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    binding.btnContinue.show()
                    if (uiState.data.status) {
                        val action =
                            PhoneNumberFragmentDirections.actionPhoneFragmentToOtpFragment(
                                countryCode = binding.etCountryCode.text.toString(),
                                mobileNumber = binding.etPhoneNumber.text.toString()
                            )
                        findNavController().navigate(action)
                    } else {
                        toast("Phone number status false. Please try with different number.")
                    }
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(uiState.error)
//                    Log.e("phone","${uiState.error}")
                }
                else -> {}
            }
        }
    }
}

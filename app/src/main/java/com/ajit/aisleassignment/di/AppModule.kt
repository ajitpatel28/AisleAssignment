package com.ajit.aisleassignment.di

import com.ajit.aisleassignment.data.remote.ApiClient
import com.ajit.aisleassignment.data.repository.Repository
import com.ajit.aisleassignment.ui.viewmodels.NotesViewModel
import com.ajit.aisleassignment.ui.viewmodels.OtpViewModel
import com.ajit.aisleassignment.ui.viewmodels.PhoneViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { ApiClient }
    single { get<ApiClient>().create() }

    single { Repository(get()) }

    viewModel { PhoneViewModel(get()) }
    viewModel { OtpViewModel(get()) }
    viewModel { NotesViewModel(get()) }
}

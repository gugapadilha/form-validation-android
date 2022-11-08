package com.guga.cleanarchformvalidation.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.guga.cleanarchformvalidation.domain.use_cases.ValidateEmail
import com.guga.cleanarchformvalidation.domain.use_cases.ValidatePassword
import com.guga.cleanarchformvalidation.domain.use_cases.ValidateRepeatedPassword
import com.guga.cleanarchformvalidation.domain.use_cases.ValidateTerms

class MainViewModel(
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateRepeatedPassword: ValidateRepeatedPassword = ValidateRepeatedPassword(),
    private val validateTerms : ValidateTerms = ValidateTerms()
) : ViewModel() {

    var state by mutableStateOf(RegistrationFormState())

    fun onEvent(event: RegistrationFormEvent){
        when(event) {
            is RegistrationFormEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }
            is RegistrationFormEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }
            is RegistrationFormEvent.RepeatedPasswordChanged -> {
                state = state.copy(password = event.repeatedPassword)
            }
            is RegistrationFormEvent.AcceptedTerms -> {
                state = state.copy(acceptedTerms = event.isAccepted )
            }
            is RegistrationFormEvent.Submit -> {

            }
        }
    }

}
package com.guga.cleanarchformvalidation.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guga.cleanarchformvalidation.domain.use_cases.ValidateEmail
import com.guga.cleanarchformvalidation.domain.use_cases.ValidatePassword
import com.guga.cleanarchformvalidation.domain.use_cases.ValidateRepeatedPassword
import com.guga.cleanarchformvalidation.domain.use_cases.ValidateTerms
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateRepeatedPassword: ValidateRepeatedPassword = ValidateRepeatedPassword(),
    private val validateTerms : ValidateTerms = ValidateTerms()
) : ViewModel() {

    var state by mutableStateOf(RegistrationFormState())

    private val validatinEventChannel = Channel<ValidationEvent>() // send event into this CHANNEL and the UI can collect this changes
    val validationEvents = validatinEventChannel.receiveAsFlow() //by simply collect from a flow

    fun onEvent(event: RegistrationFormEvent){ // sends event from UI to viewModel here
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
                submitData()
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)
        val repeatedPasswordResult = validateRepeatedPassword.execute(
            state.password, state.repeatedPassword
        )
        val termsResult = validateTerms.execute(state.acceptedTerms)
        val hasError = listOf(
            emailResult,
            passwordResult,
            repeatedPasswordResult,
            termsResult
        ).any { !it.successful}

        if (hasError){
            state = state.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                repeatedPasswordError = repeatedPasswordResult.errorMessage,
                termsError = termsResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            validatinEventChannel.send(ValidationEvent.Success)
        }
    }
    sealed class ValidationEvent {
        object Success: ValidationEvent()
    }

}
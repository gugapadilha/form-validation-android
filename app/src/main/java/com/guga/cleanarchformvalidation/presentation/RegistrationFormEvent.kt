package com.guga.cleanarchformvalidation.presentation

sealed class RegistrationFormEvent{
    data class EmailChanged(val email: String) : RegistrationFormEvent()
    data class PasswordChanged(val password: String) : RegistrationFormEvent()
    data class RepeatedPasswordChanged(val password: String) : RegistrationFormEvent()
    data class AcceptedTerms(val isAccepted: String) : RegistrationFormEvent()

    object Submit: RegistrationFormEvent()
}

package uk.ac.tees.mad.w9617329.screens.authentication.domain

data class RegisterState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)
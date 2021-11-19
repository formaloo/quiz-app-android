package co.idearun.auth.viewmodel

import androidx.collection.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.idearun.auth.model.LoginRes
import co.idearun.auth.model.Token
import co.idearun.auth.model.register.RegisterInfo
import co.idearun.common.base.BaseViewModel
import co.idearun.auth.model.register.RegisterRes
import co.idearun.auth.repository.AuthRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

class AuthViewModel(private val repository: AuthRepositoryImpl) : BaseViewModel() {

    private val _registerData = MutableLiveData<RegisterRes>()
    val registerData: LiveData<RegisterRes> = _registerData

    private val _loginData = MutableLiveData<LoginRes>()
    val loginData: LiveData<LoginRes> = _loginData

    private val _authorizeData = MutableLiveData<Token>()
    val authorizeData: LiveData<Token> = _authorizeData

    private val _isLoading = MediatorLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerUser(registerInfo: RegisterInfo) = viewModelScope.launch {
        _isLoading.value = true
        val req = ArrayMap<String, Any>()

        with(registerInfo) {
            req["first_name"] = name
            req["password"] = pass
            req["email"] = email
        }

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(), JSONObject(req).toString()
        )

        val result = withContext(Dispatchers.IO) { repository.registerUser(body) }
        result.either(::handleFailure, ::handleRegisterData)
    }

    fun loginUser(userName: String, password: String) = viewModelScope.launch {
        _isLoading.value = true
        val req = ArrayMap<String, Any>()
        req["password"] = password
        req["username"] = userName

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(), JSONObject(req).toString()
        )

        val result = withContext(Dispatchers.IO) { repository.loginUser(body) }
        result.either(::handleFailure, ::handleLoginData)
    }

    fun authorizeUser(token: String) = viewModelScope.launch {
        val result = withContext(Dispatchers.IO) { repository.authorizeUser(token) }
        result.either(::handleFailure, ::handleAuthorizeData)
    }

    private fun handleRegisterData(res: RegisterRes) {
        hideLoading()
        res.let {
            _registerData.value = it
        }
    }

    private fun handleLoginData(res: LoginRes) {
        hideLoading()
        res.let {
            _loginData.value = it
        }
    }

    private fun handleAuthorizeData(res: Token) {
        res.let {
            _authorizeData.value = it
        }
    }

    fun hideLoading() {
        _isLoading.value = false
    }

}
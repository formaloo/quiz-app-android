package com.formaloo.game.feature.viewmodel

import androidx.collection.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.formaloo.common.base.BaseViewModel
import com.formaloo.data.model.EditRomRes
import com.formaloo.data.model.LiveSubmits
import com.formaloo.data.model.Row
import com.formaloo.data.model.SubmitsResponse
import com.formaloo.data.model.form.Form
import com.formaloo.data.model.form.createForm.CreateFormRes
import com.formaloo.data.model.form.formList.FormListRes
import com.formaloo.data.model.live.LiveDashboardCode
import com.formaloo.data.model.live.LiveDashboardRes
import com.formaloo.data.model.submitForm.SubmitFormRes
import com.formaloo.data.repository.FormzRepo
import com.formaloo.game.feature.PlayerInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import timber.log.Timber

class FormViewModel(private val repository: FormzRepo) : BaseViewModel() {

    private var formSlug: String = ""
    private var formAddress: String = ""


    private val _isLoading = MediatorLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var _body = MutableLiveData<ArrayMap<String, String>>()
    var body: LiveData<ArrayMap<String, String>> = _body

    private val _form = MutableLiveData<Form>()
    val form: LiveData<Form> = _form

    private val _form1 = MutableLiveData<Form>()
    val form1: LiveData<Form> = _form1

    private val _submits = MutableLiveData<SubmitsResponse>()
    val submits: LiveData<SubmitsResponse> = _submits


    private val _liveSubmits = MutableLiveData<LiveSubmits>()
    val liveSubmits: LiveData<LiveSubmits> = _liveSubmits

    private val _submitForm = MutableLiveData<SubmitFormRes>()
    val submitForm: LiveData<SubmitFormRes> = _submitForm

    private val _liveForm = MutableLiveData<LiveDashboardCode>()
    val liveForm: LiveData<LiveDashboardCode> = _liveForm

    private val _editForm = MutableLiveData<Form>()
    val editForm: LiveData<Form> = _editForm

    private val _editRow = MutableLiveData<Row>()
    val editRow: LiveData<Row> = _editRow

    private val _disableForm = MutableLiveData<Form>()
    val disableForm: LiveData<Form> = _disableForm

    private val _formTag = MutableLiveData<FormListRes>()
    val formTag: LiveData<FormListRes> = _formTag


    fun getFormData() = viewModelScope.launch {
        showLoading()
        val result = withContext(Dispatchers.IO) { repository.getFormData(formAddress ?: "") }
        result.either(::handleFailure, ::handleFormData1)
    }

    fun copyForm(slug: String) = viewModelScope.launch {
        showLoading()
        val result = withContext(Dispatchers.IO) { repository.copyForm(slug) }
        result.either(::handleFailure, ::handleFormData)
    }

    fun createLive(slug: String) = viewModelScope.launch {
        showLoading()
        val result = withContext(Dispatchers.IO) { repository.createLive(slug) }
        result.either(::handleFailure, ::handleLiveFormData)
    }

    fun getFormDataWithLiveCode(liveCode: String) = viewModelScope.launch {
        showLoading()
        val result = withContext(Dispatchers.IO) { repository.getFormDataWithLiveCode(liveCode) }
        result.either(::handleFailure, ::handleLiveFormData)
    }

    fun editForm(slug: String, body: ArrayMap<String, Any>) = viewModelScope.launch {
        showLoading()

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(), JSONObject(body).toString()
        )
        val result = withContext(Dispatchers.IO) { repository.editForm(slug, requestBody) }
        result.either(::handleFailure, ::handleEditFormData)
    }

    fun disableForm(slug: String, activation: Boolean) = viewModelScope.launch {
        showLoading()

        val req = ArrayMap<String, Any>()
        req["active"] = activation

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(), JSONObject(req).toString()
        )

        val result = withContext(Dispatchers.IO) { repository.editForm(slug,  requestBody) }
        result.either(::handleFailure, ::handleDisableForm)
    }

    fun submitFormData(slug: String, body: RequestBody) = viewModelScope.launch {
        val result = withContext(Dispatchers.IO) { repository.submitFormData(slug, body) }
        result.either(::handleFailure, ::handleSubmitFormData)
    }

    fun submitFormData(slug: String) = viewModelScope.launch {
        val result = withContext(Dispatchers.IO) { repository.submitFormData(slug, createBody()) }
        result.either(::handleFailure, ::handleSubmitFormData)
    }

    fun createBody(): RequestBody {
        body.value?.put(PlayerInfo.playerNameSlug, PlayerInfo.playerName)
        return RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(body.value as Map<*, *>).toString()
        )
    }

    fun getFormSubmits(slug: String) = viewModelScope.launch {
        showLoading()
        val result = withContext(Dispatchers.IO) { repository.getFormSubmits(slug) }
        result.either(::handleFailure, ::handleSubmitsData)
    }

    fun getSubmitsRow(liveDashboardAddress: String) = viewModelScope.launch {
        showLoading()
        val result = withContext(Dispatchers.IO) { repository.getSubmitsRow(liveDashboardAddress) }
        result.either(::handleFailure, ::handleSubmitsRowData)
    }

    fun getLiveSubmits(liveDashboardAddress: String) = viewModelScope.launch {
        showLoading()
        val result = withContext(Dispatchers.IO) { repository.getLiveSubmits(liveDashboardAddress) }
        result.either(::handleFailure, ::handleLiveSubmitsData)
    }

    fun editRow(slug: String, body: ArrayMap<String, Any>) = viewModelScope.launch {
        showLoading()

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(body as Map<*, *>).toString()
        )
        val result =
            withContext(Dispatchers.IO) { repository.editRow(slug, requestBody) }
        result.either(::handleFailure, ::handleEditRowData)
    }

    private fun handleLiveSubmitsData(res: LiveSubmits) {
        hideLoading()
        res.let {
            _liveSubmits.value = res
        }
    }

    private fun handleSubmitsData(res: SubmitsResponse) {
        hideLoading()
        res.let {
            _submits.value = res
        }
    }

    private fun handleSubmitsRowData(res: SubmitsResponse) {
        hideLoading()
        res.let {
            _submits.value = res
        }
    }

    private fun handleSubmitFormData(res: SubmitFormRes) {
        hideLoading()
        res.let {
            _submitForm.value = it
        }
    }

    private fun handleLiveFormData(res: LiveDashboardRes) {
        hideLoading()
        res.data?.liveDashboardCode?.let {
            _liveForm.value = it
        }
    }

    private fun handleEditFormData(res: CreateFormRes) {
        hideLoading()
        res.data?.form?.let {
            _editForm.value = it
        }
    }

    private fun handleEditRowData(res: EditRomRes) {
        hideLoading()
        res.data?.row?.let {
            _editRow.value = it
        }
    }

    private fun handleDisableForm(res: CreateFormRes) {
        hideLoading()
        res.data?.form?.let {
            _disableForm.value = it
        }
    }

    private fun handleFormData(res: CreateFormRes) {
        hideLoading()
        res.data?.form?.let {
            _form.value = it
        }
    }

    private fun handleFormData1(res: CreateFormRes) {
        hideLoading()
        res.data?.form?.let {
            _form1.value = it
        }
    }

    fun getFormTag(page: Int) = viewModelScope.launch {
        showLoading()
        val result = withContext(Dispatchers.IO) { repository.getFormTag(page) }
        result.either(::handleFailure, ::handleFormTagData)
    }

    private fun handleFormTagData(res: FormListRes) {
        hideLoading()
        res.let {
            _formTag.value = it
        }
    }

    fun initLessonSlug(slug: String) {
        this.formSlug = slug
    }

    fun initLessonAddress(slug: String) {
        this.formAddress = slug
    }


    fun hideLoading() {
        _isLoading.value = false
    }

    fun showLoading() {
        _isLoading.value = true
    }

}
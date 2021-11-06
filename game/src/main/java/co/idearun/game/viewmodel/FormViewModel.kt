package co.idearun.game.viewmodel

import android.util.Log
import androidx.collection.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.idearun.common.base.BaseViewModel
import co.idearun.data.model.SubmitsResponse
import co.idearun.data.model.form.Form
import co.idearun.data.model.form.createForm.CreateFormRes
import co.idearun.data.model.form.formList.FormListRes
import co.idearun.data.model.live.LiveDashboardCode
import co.idearun.data.model.live.LiveDashboardRes
import co.idearun.data.model.submitForm.SubmitFormRes
import co.idearun.data.repository.FormzRepo
import kotlinx.android.synthetic.main.fragment_formeditor.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import timber.log.Timber

class FormViewModel(private val repository: FormzRepo) : BaseViewModel() {

    private var formSlug: String = ""
    private var formAddress: String = ""

    var userForm = MutableLiveData<LiveDashboardCode>()
    var userName = MutableLiveData<String>()

    private val _form = MutableLiveData<Form>()
    val form: LiveData<Form> = _form

    private val _form1 = MutableLiveData<Form>()
    val form1: LiveData<Form> = _form1

    private val _submits = MutableLiveData<SubmitsResponse>()
    val submits: LiveData<SubmitsResponse> = _submits

    private val _liveForm = MutableLiveData<LiveDashboardCode>()
    val liveForm: LiveData<LiveDashboardCode> = _liveForm

    private val _editForm = MutableLiveData<Form>()
    val editForm: LiveData<Form> = _editForm

    private val _disableForm = MutableLiveData<Form>()
    val disableForm: LiveData<Form> = _disableForm

    private val _formTag = MutableLiveData<FormListRes>()
    val formTag: LiveData<FormListRes> = _formTag

    private val _pagingData = MutableLiveData<PagingData<Form>>().apply { value = null }
    val pagingData: LiveData<PagingData<Form>> = _pagingData

    private val _formMap = MutableLiveData<ArrayList<HashMap<Int, Form>>>().apply { value = null }
    val formMap: LiveData<ArrayList<HashMap<Int, Form>>> = _formMap

    fun fetchLessonList(force: Boolean): Flow<PagingData<Form>> {
        return repository.fetchForms(force).cachedIn(viewModelScope)
    }

    fun retrieveDBLessonList() = viewModelScope.launch {
        val result = withContext(Dispatchers.IO) { repository.getFormListFromDB() }
        sortLessonList(result)

    }

    private fun sortLessonList(result: List<Form>) {
        val TYPE_HEADER = 0
        val TYPE_ITEM = 1
        val OTHER = "other"
        val forms = ArrayList(result)
        val formsList = ArrayList<HashMap<Int, Form>>()
        var lastCatSlug = OTHER

        forms.sortByDescending {
            it.category?.slug
        }
        for (i in 0 until forms.size) {
            val form = forms[i]
            val formcatSlug = form.category?.slug ?: OTHER

            val formsMap = HashMap<Int, Form>()


            if (i == 0) {
                formsMap[TYPE_HEADER] = form
            } else {
                if (lastCatSlug == formcatSlug) {
                    formsMap[TYPE_ITEM] = form
                } else {
                    formsMap[TYPE_HEADER] = form
                }
            }
            lastCatSlug = formcatSlug

            formsList.add(formsMap)
        }
        _formMap.value = formsList

    }

    fun retrieveLessonFromDB() = viewModelScope.launch {
        val result = withContext(Dispatchers.IO) { repository.getFormFromDB(formSlug ?: "") }
        _form.value = result

    }

    fun getFormData() = viewModelScope.launch {
        val result = withContext(Dispatchers.IO) { repository.getFormData(formAddress ?: "") }
        result.either(::handleFailure, ::handleFormData1)
    }

    fun copyForm(slug: String, token: String) = viewModelScope.launch {
        val result = withContext(Dispatchers.IO) { repository.copyForm(slug, token) }
        result.either(::handleFailure, ::handleFormData)
    }

    fun createLive(slug: String, token: String) = viewModelScope.launch {
        val result = withContext(Dispatchers.IO) { repository.createLive(slug, token) }
        result.either(::handleFailure, ::handleLiveFormData)
    }

    fun getFormDataWithLiveCode(token: String, body: String) = viewModelScope.launch {
        val result = withContext(Dispatchers.IO) { repository.getFormDataWithLiveCode(token, body) }
        result.either(::handleFailure, ::handleLiveFormData)
    }

    fun editForm(slug: String, token: String, body: ArrayMap<String, Any>) = viewModelScope.launch {
        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(), JSONObject(body).toString()
        )
        val result = withContext(Dispatchers.IO) { repository.editForm(slug, token, requestBody) }
        result.either(::handleFailure, ::handleEditFormData)
    }

    fun disableForm(slug: String, token: String, activation: Boolean) = viewModelScope.launch {
        val req = ArrayMap<String, Any>()
        req["active"] = activation

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(), JSONObject(req).toString()
        )
        val result = withContext(Dispatchers.IO) { repository.editForm(slug, token, requestBody) }
        result.either(::handleFailure, ::handleDisableForm)
    }

    fun submitFormData(slug: String, body: RequestBody) = viewModelScope.launch {
        val result = withContext(Dispatchers.IO) { repository.submitFormData(slug, body) }
        result.either(::handleFailure, ::handleLiveFormData)
    }

    fun getFormSubmits(slug: String, token: String) = viewModelScope.launch {
        val result = withContext(Dispatchers.IO) { repository.getFormSubmits(slug, token) }
        result.either(::handleFailure, ::handleFormSubmitsData)
    }

    private fun handleFormSubmitsData(res: SubmitsResponse) {
        Timber.i("players ${res.status}")
        res?.let {
            _submits.value = res
        }
    }

    private fun handleLiveFormData(res: SubmitFormRes) {
        Timber.i("players ${res.status}")
        /*   res?.data?.liveDashboardCode?.let {
               _liveForm.value = it
           }*/
    }

    private fun handleLiveFormData(res: LiveDashboardRes) {
        res?.data?.liveDashboardCode?.let {
            _liveForm.value = it
        }
    }

    private fun handleEditFormData(res: CreateFormRes) {
        res?.data?.form?.let {
            _editForm.value = it
        }
    }

    private fun handleDisableForm(res: CreateFormRes) {
        res?.data?.form?.let {
            _editForm.value = it
        }
    }

    private fun handleFormData(res: CreateFormRes) {
        res?.data?.form?.let {
            _form.value = it
        }
    }

    private fun handleFormData1(res: CreateFormRes) {
        res?.data?.form?.let {
            _form1.value = it
        }
    }

    fun getFormTag(page: Int) = viewModelScope.launch {
        val result = withContext(Dispatchers.IO) { repository.getFormTag(page) }
        result.either(::handleFailure, ::handleFormTagData)


    }

    private fun handleFormTagData(res: FormListRes) {
        res?.let {
            _formTag.value = it
        }
    }

    fun initLessonSlug(slug: String) {
        this.formSlug = slug
    }

    fun initLessonAddress(slug: String) {
        Timber.i("TAG init")
        this.formAddress = slug
    }


    fun getLessonsList(force: Boolean) = viewModelScope.launch {
        fetchLessonList(force).collectLatest { pagingData ->
            _pagingData.value = pagingData
        }

    }

}
package co.idearun.learningapp.feature.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.idearun.learningapp.common.base.BaseViewModel
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.data.model.form.createForm.CreateFormRes
import co.idearun.learningapp.data.repository.FormzRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class FormViewModel(private val repository: FormzRepo) : BaseViewModel() {

    private var formSlug: String = ""

    private val _form = MutableLiveData<Form>().apply { value = null }
    val form: LiveData<Form> = _form

    private val _isLoading = MutableLiveData<Boolean>().apply { value = null }
    val isLoading: LiveData<Boolean> = _isLoading

    private val _pagingData = MutableLiveData<PagingData<Form>>().apply { value = null }
    val pagingData: LiveData<PagingData<Form>> = _pagingData

    private val _formList = MutableLiveData<ArrayList<Form>>().apply { value = null }
    val formList: LiveData<ArrayList<Form>> = _formList

    private val _formMap = MutableLiveData<ArrayList<HashMap<Int, Form>>>().apply { value = null }
    val formMap: LiveData<ArrayList<HashMap<Int, Form>>> = _formMap

    fun fetchFormList(force: Boolean): Flow<PagingData<Form>> {
        return repository.fetchForms(force).cachedIn(viewModelScope)
    }

    fun retrieveDBFormList() = launch {
        val result = withContext(Dispatchers.IO) { repository.getFormListFromDB() }
        sortFormList(result)

    }

    private fun sortFormList(result: List<Form>) {
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
    fun retrieveForm() = launch {
        val result = async(Dispatchers.IO) { repository.getFormData(formSlug) }.await()
        result.either(::handleFailure, ::handleFormData)

    }

    fun retrieveFormFromDB() = launch {
        val result = withContext(Dispatchers.IO) { repository.getFormFromDB(formSlug ?: "") }
        _form.value = result

    }

    private fun handleFormData(res: CreateFormRes) {
        res?.let { it ->
            it.data?.let {
                _form.value = it.form
                it.form?.fields_list?.let {
                }
            }


        }

    }

    fun initFormSlug(slug: String) {
        this.formSlug = slug
    }

    fun stopLoading() {
        _isLoading.value=false
    }

    fun getFormList(force: Boolean) =launch{
        _isLoading.value=true
        fetchFormList(force).collectLatest { pagingData ->
            _pagingData.value=pagingData
            _isLoading.value=false

        }

    }

}
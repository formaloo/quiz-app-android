package co.idearun.learningapp.feature.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.idearun.learningapp.common.base.BaseViewModel
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.data.repository.FormzRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class FormViewModel(private val repository: FormzRepo) : BaseViewModel() {

    private var formSlug: String = ""

    private val _form = MutableLiveData<Form>().apply { value = null }
    val form: LiveData<Form> = _form

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

            Timber.e("%s %s", formcatSlug, lastCatSlug)

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

            Timber.e("%s%s", formcatSlug, formsMap.keys)
            formsList.add(formsMap)
        }
        _formMap.value = formsList


    }

    fun initFormSlug(slug: String) {
        this.formSlug = slug
    }

}
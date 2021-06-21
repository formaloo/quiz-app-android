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

class FormViewModel(private val repository: FormzRepo) : BaseViewModel() {

    private var formSlug: String = ""

    private val _form = MutableLiveData<Form>().apply { value = null }
    val form: LiveData<Form> = _form

    private val _formList = MutableLiveData<ArrayList<Form>>().apply { value = null }
    val formList: LiveData<ArrayList<Form>> = _formList

    fun fetchFormList(force: Boolean): Flow<PagingData<Form>> {
        return repository.fetchForms(force).cachedIn(viewModelScope)
    }

    fun retrieveDBFormList() = launch {
        val result = withContext(Dispatchers.IO) { repository.getFormListFromDB() }
        sortFormList(result)

    }

    private fun sortFormList(result: List<Form>) {
        val forms = ArrayList(result)
        forms.sortBy {
            it.category?.slug
        }
        _formList.value = forms


    }

    fun initFormSlug(slug: String) {
        this.formSlug = slug
    }

}
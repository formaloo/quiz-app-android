package co.idearun.learningapp.feature.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.idearun.learningapp.common.base.BaseViewModel
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.data.repository.FormzRepo
import kotlinx.coroutines.flow.Flow

class FormViewModel(private val repository: FormzRepo) : BaseViewModel() {

    private var formSlug: String = ""

    private val _form = MutableLiveData<Form>().apply { value = null }
    val form: LiveData<Form> = _form

    fun fetchFormList(force: Boolean): Flow<PagingData<Form>> {
        return repository.fetchForms(force).cachedIn(viewModelScope)
    }

    fun initFormSlug(slug: String) {
        this.formSlug = slug
    }

}
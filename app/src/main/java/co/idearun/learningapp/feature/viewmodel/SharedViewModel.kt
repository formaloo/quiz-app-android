package co.idearun.learningapp.feature.viewmodel

import co.idearun.learningapp.common.base.BaseViewModel
import co.idearun.learningapp.data.repository.sharedRepo.SharedRepository

class SharedViewModel(private val repository: SharedRepository) : BaseViewModel() {

    fun saveFormProgress(formProgress: HashMap<String?, Int?>) {
        repository.saveFormProgress(formProgress)

    }

    fun retrieveFormProgress(): HashMap<String?, Int?> {
        return repository.retrieveFormProgress()

    }

    fun saveLastForm(formSlug: String) {
        repository.saveLastForm(formSlug)
    }

    fun getLastForm(): String? {
        return repository.getLastForm()
    }
}
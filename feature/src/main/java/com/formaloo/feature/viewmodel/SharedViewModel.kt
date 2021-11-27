package com.formaloo.feature.viewmodel

import com.formaloo.common.base.BaseViewModel
import com.formaloo.data.repository.sharedRepo.SharedRepository

class SharedViewModel(private val repository: SharedRepository) : BaseViewModel() {

    fun saveLessonProgress(progress: HashMap<String?, Int?>) {
        repository.saveLessonProgress(progress)

    }

    fun retrieveLessonProgress(): HashMap<String?, Int?> {
        return repository.retrieveLessonProgress()

    }

    fun saveLastLesson(formSlug: String) {
        repository.saveLastLesson(formSlug)
    }

    fun getLastLesson(): String? {
        return repository.getLastLesson()
    }

}
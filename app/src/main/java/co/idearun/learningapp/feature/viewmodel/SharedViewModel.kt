package co.idearun.learningapp.feature.viewmodel

import co.idearun.learningapp.common.base.BaseViewModel
import co.idearun.learningapp.data.repository.sharedRepo.SharedRepository

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

    fun saveDoneLessonList(formSlug: MutableSet<String>) {
        repository.saveDoneLessonList(formSlug)
    }

    fun getDoneLessonList(): MutableSet<String>? {
        return repository.getDoneLessonList()
    }
}
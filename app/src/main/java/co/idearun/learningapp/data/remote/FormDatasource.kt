package co.idearun.learningapp.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody


/**
 * Implementation of [UserService] interface
 */

class FormDatasource(private val service: FormService) {
    suspend fun getForms(page: Int) = service.getForms(page, 20)
    fun getCatList() = service.getCategories()
    fun getFormData(formSlug: String?) = service.getFormDetail(formSlug)
    fun search(searchStr: String) = service.search(searchStr)
    fun searchForms(searchStr: String) = service.searchForms(searchStr)
    fun submitForm(
        slug: String,
        req: HashMap<String, RequestBody>,
        files: List<MultipartBody.Part>?
    ) = service.submitForm(slug, req, files)

}
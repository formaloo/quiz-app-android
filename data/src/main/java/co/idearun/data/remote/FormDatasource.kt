package co.idearun.data.remote

import co.idearun.common.BuildConfig.FORM_TAG
import okhttp3.MultipartBody
import okhttp3.RequestBody


/**
 * Implementation of [UserService] interface
 */

class FormDatasource(private val service: FormService) {
    suspend fun getForms(page: Int) = service.getForms(FORM_TAG, page, 20)
    fun getFormsWithTag(page: Int) = service.getFormsWithTag("game", page, 20)
    fun getCatList() = service.getCategories()
    fun getFormData(formAddress: String?) = service.getFormDetail(formAddress)
    fun search(searchStr: String) = service.search(searchStr)
    fun searchForms(searchStr: String) = service.searchForms(searchStr)
    fun submitForm(
        slug: String,
        req: HashMap<String, RequestBody>,
        files: List<MultipartBody.Part>?
    ) = service.submitForm(slug, req, files)

    fun copyForm(slug: String, token: String) = service.copyForm(slug, token)
    fun createLive(slug: String, token: String) = service.createLive(slug, token)
    fun getFormDataWithLiveCode(body: RequestBody) =
        service.getFormDataWithLiveCode(body)

    fun editForm(slug: String, token: String, body: RequestBody) =
        service.editForm(slug, token, body)

    fun submitFormData(slug: String, body: RequestBody) =
        service.submitFormData(slug, body)

    fun getFormSubmits(slug: String, token: String) =
        service.getFormSubmits(slug, token)

    fun getSubmitsRow(liveDashboardAddress: String) =
        service.getSubmitsRow(liveDashboardAddress)

    fun getLiveSubmits(liveDashboardAddress: String) =
        service.getLiveSubmits(liveDashboardAddress)

    fun editRow(slug: String, token: String, body: RequestBody) =
        service.editRow(slug, token, body)

}

package com.formaloo.data.remote

import com.formaloo.common.BuildConfig.FORM_TAG
import com.formaloo.common.TokenContainer
import com.formaloo.common.extension.toJWT
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

    fun copyForm(slug: String) = service.copyForm(slug, TokenContainer.authorizationToken!!.toJWT())
    fun createLive(slug: String) =
        service.createLive(slug, TokenContainer.authorizationToken!!.toJWT())

    fun getFormDataWithLiveCode(body: RequestBody) =
        service.getFormDataWithLiveCode(body)

    fun editForm(slug: String, body: RequestBody) =
        service.editForm(slug, body, TokenContainer.authorizationToken!!.toJWT())

    fun submitFormData(slug: String, body: RequestBody?) =
        service.submitFormData(slug, body)

    fun getFormSubmits(slug: String) =
        service.getFormSubmits(slug, TokenContainer.authorizationToken!!.toJWT())

    fun getSubmitsRow(liveDashboardAddress: String) =
        service.getSubmitsRow(liveDashboardAddress)

    fun getLiveSubmits(liveDashboardAddress: String) =
        service.getLiveSubmits(liveDashboardAddress)

    fun editRow(slug: String, body: RequestBody) =
        service.editRow(slug, body, TokenContainer.authorizationToken!!.toJWT())

}

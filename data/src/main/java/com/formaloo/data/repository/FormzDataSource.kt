package com.formaloo.data.repository

import com.formaloo.common.exception.Failure
import com.formaloo.common.functional.Either
import com.formaloo.data.model.EditRomRes
import com.formaloo.data.model.LiveSubmits
import com.formaloo.data.model.SubmitsResponse
import com.formaloo.data.model.cat.catList.CatListRes
import com.formaloo.data.model.form.Form
import com.formaloo.data.model.form.SubmitEntity
import com.formaloo.data.model.form.createForm.CreateFormRes
import com.formaloo.data.model.form.formList.FormListRes
import com.formaloo.data.model.live.LiveDashboardRes
import com.formaloo.data.model.search.SearchRes
import com.formaloo.data.model.submitForm.SubmitFormRes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.HashMap

interface FormzDataSource {
    suspend fun getCatList(): Either<Failure, CatListRes>
    suspend fun getFormData(formAddress: String?): Either<Failure, CreateFormRes>
    suspend fun search(searchStr: String): Either<Failure, SearchRes>
    suspend fun searchForms(searchStr: String): Either<Failure, FormListRes>
    suspend fun getForm(formAddress: String?): CreateFormRes?

    suspend fun getFormTag(page: Int): Either<Failure, FormListRes>
    suspend fun copyForm(slug: String, token: String): Either<Failure, CreateFormRes>
    suspend fun createLive(slug: String, token: String): Either<Failure, LiveDashboardRes>
    suspend fun getFormDataWithLiveCode(body: String): Either<Failure, LiveDashboardRes>
    suspend fun editForm(slug: String,token: String, body: RequestBody): Either<Failure, CreateFormRes>
    suspend fun submitFormData(slug: String, body: RequestBody?): Either<Failure, SubmitFormRes>
    suspend fun getFormSubmits(slug: String, token: String ): Either<Failure, SubmitsResponse>
    suspend fun getSubmitsRow(liveDashboardAddress: String): Either<Failure, SubmitsResponse>
    suspend fun getLiveSubmits(liveDashboardAddress: String): Either<Failure, LiveSubmits>
    suspend fun editRow(slug: String,token: String, body: RequestBody): Either<Failure, EditRomRes>

    suspend fun getFormFromDB(slug: String): Form?
    suspend fun getFormListFromDB(): List<Form>
    suspend fun saveSubmit(submitEntity: SubmitEntity)
    suspend fun getSubmitEntity(slug: String): SubmitEntity
    suspend fun sendSavedSubmitToServer()
    suspend fun submitForm(
        submitEntity: SubmitEntity,
        slug: String,
        req: HashMap<String, RequestBody>,
        files: List<MultipartBody.Part>?
    )

    suspend fun getSubmitEntityList(): List<SubmitEntity>
}
package co.idearun.data.repository

import co.idearun.common.exception.Failure
import co.idearun.common.functional.Either
import co.idearun.data.model.cat.catList.CatListRes
import co.idearun.data.model.form.Form
import co.idearun.data.model.form.SubmitEntity
import co.idearun.data.model.form.createForm.CreateFormRes
import co.idearun.data.model.form.formList.FormListRes
import co.idearun.data.model.live.LiveDashboardRes
import co.idearun.data.model.search.SearchRes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.HashMap
import javax.sql.StatementEvent

interface FormzDataSource {
    suspend fun getCatList(): Either<Failure, CatListRes>
    suspend fun getFormData(formAddress: String?): Either<Failure, CreateFormRes>
    suspend fun search(searchStr: String): Either<Failure, SearchRes>
    suspend fun searchForms(searchStr: String): Either<Failure, FormListRes>
    suspend fun getForm(formAddress: String?): CreateFormRes?

    suspend fun getFormTag(page: Int): Either<Failure, FormListRes>
    suspend fun copyForm(slug: String, token: String): Either<Failure, CreateFormRes>
    suspend fun createLive(slug: String, token: String): Either<Failure, LiveDashboardRes>
    suspend fun editForm(slug: String,token: String, body: RequestBody): Either<Failure, CreateFormRes>


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
package co.idearun.learningapp.data.repository

import co.idearun.learningapp.common.exception.Failure
import co.idearun.learningapp.common.functional.Either
import co.idearun.learningapp.data.model.cat.catList.CatListRes
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.data.model.form.SubmitEntity
import co.idearun.learningapp.data.model.form.createForm.CreateFormRes
import co.idearun.learningapp.data.model.form.formList.FormListRes
import co.idearun.learningapp.data.model.search.SearchRes
import co.idearun.learningapp.data.model.submitForm.SubmitFormRes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.HashMap

interface FormzDataSource {
    suspend fun getCatList(): Either<Failure, CatListRes>
    suspend fun getFormData(formAddress: String?): Either<Failure, CreateFormRes>
    suspend fun search(searchStr: String): Either<Failure, SearchRes>
    suspend fun searchForms(searchStr: String): Either<Failure, FormListRes>

    suspend fun getForm(formAddress: String?): CreateFormRes?
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
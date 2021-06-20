package co.idearun.learningapp.data.repository

import co.idearun.learningapp.common.exception.Failure
import co.idearun.learningapp.common.functional.Either
import co.idearun.learningapp.data.model.cat.catList.CatListRes
import co.idearun.learningapp.data.model.form.createForm.CreateFormRes
import co.idearun.learningapp.data.model.form.formList.FormListRes
import co.idearun.learningapp.data.model.search.SearchRes
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface FormzDataSource {
    suspend fun getCatList(): Either<Failure, CatListRes>
    suspend fun getFormData(formSlug: String?): Either<Failure, CreateFormRes>
    suspend fun search(searchStr: String): Either<Failure, SearchRes>
    suspend fun searchForms(searchStr: String): Either<Failure, FormListRes>
    suspend fun submitForm(
        slug: String,
        req: HashMap<String, RequestBody>,
        files: List<MultipartBody.Part>?
    )
    suspend fun getForm(formSlug: String?): CreateFormRes?
}
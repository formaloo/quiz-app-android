package com.formaloo.data.repository

import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.paging.*
import com.formaloo.common.Constants
import com.formaloo.common.exception.Failure
import com.formaloo.common.exception.ViewFailure
import com.formaloo.common.functional.Either
import com.formaloo.data.local.FormsKeys
import com.formaloo.data.local.dao.FormDao
import com.formaloo.data.local.dao.FormKeysDao
import com.formaloo.data.local.dao.SubmitDao
import com.formaloo.data.model.EditRomRes
import com.formaloo.data.model.LiveSubmits
import com.formaloo.data.model.SubmitsResponse
import com.formaloo.data.model.cat.catList.CatListRes
import com.formaloo.data.model.form.Fields
import com.formaloo.data.model.form.Form
import com.formaloo.data.model.form.SubmitEntity
import com.formaloo.data.model.form.createForm.CreateFormRes
import com.formaloo.data.model.form.formList.FormListRes
import com.formaloo.data.model.live.LiveDashboardRes
import com.formaloo.data.model.search.SearchRes
import com.formaloo.data.model.submitForm.SubmitFormRes
import com.formaloo.data.remote.FormDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.URLEncoder
import java.net.UnknownHostException
import java.util.*
import kotlin.collections.HashMap

const val TAG = "FormzRepo"

class FormzRepo(
    private val source: FormDatasource,
    private val formsDao: FormDao,
    private val formsKeysDao: FormKeysDao,
    private val submitDao: SubmitDao
) : FormzDataSource {

    override suspend fun sendSavedSubmitToServer() {
        val submitEntityList = submitDao.getSubmitEntityList()
        submitEntityList.map {
            val newRow = it.newRow
            val formSLug = it.formSlug
            val formReq = createFormReq(it.formReq)
            val files = createFilesReq(it.files)
            if (newRow == true && formSLug != null) {
                submitForm(it, formSLug, formReq, files)

            }

        }
    }


    override suspend fun saveSubmit(submitEntity: SubmitEntity) {
        submitDao.save(submitEntity)
    }

    override suspend fun getSubmitEntity(slug: String): SubmitEntity {
        return submitDao.getSubmitEntity(slug)
    }

    override suspend fun getSubmitEntityList(): List<SubmitEntity> {
        return submitDao.getSubmitEntityList()
    }

    override suspend fun getFormFromDB(slug: String): Form? {
        return formsDao.getForm(slug)

    }

    override suspend fun getFormListFromDB(): List<Form> {
        return formsDao.getFormList()

    }

    override suspend fun submitForm(
        submitEntity: SubmitEntity,
        slug: String,
        req: HashMap<String, RequestBody>,
        files: List<MultipartBody.Part>?
    ) {
        val call = source.submitForm(slug, req, files)
        call.enqueue(object : Callback<SubmitFormRes> {
            override fun onResponse(call: Call<SubmitFormRes>, response: Response<SubmitFormRes>) {
                Log.e(TAG, "submitForm:onResponse " + response.raw())

                val code = response.code()
                if (code == 200 || code == 201) {
                    removeSentSubmitFromDB(submitEntity)

                } else {
//                    updateSubmitEntityFromDB(submitEntity)

                    val jObjError = JSONObject(response.errorBody()?.string())
                    Log.e("request", "submitForm jObjError-> $jObjError")

                }

            }

            override fun onFailure(call: Call<SubmitFormRes>, t: Throwable) {
                Log.e(TAG, "submitForm:onFailure " + t.message)

            }

        })

    }

    private fun removeSentSubmitFromDB(submitEntity: SubmitEntity) {
        GlobalScope.launch(Dispatchers.Main) {
            submitDao.deleteSubmitEntity(submitEntity.uniqueId)

        }

    }

    fun createFilesReq(filesMap: HashMap<String, Fields>): ArrayList<MultipartBody.Part> {
        val files: ArrayList<MultipartBody.Part> = arrayListOf()

        filesMap.keys.forEach {
            files.add(prepareFilePart(filesMap[it], it))
        }

        return files
    }

    private fun prepareFilePart(
        field: Fields?,
        filePath: String
    ): MultipartBody.Part {
        val file = File(filePath)

        val fileExt = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString())

        val requestBody =
            if (field?.file_type == Constants.image || field?.type == Constants.SIGNATURE) {
                RequestBody.create("image/png".toMediaTypeOrNull(), file)
            } else {
                RequestBody.create("*/$fileExt".toMediaTypeOrNull(), file)
            }

        return MultipartBody.Part.createFormData(
            field?.slug!!, URLEncoder.encode(file.name, "utf-8"), requestBody
        )

    }

    fun createFormReq(reqMap: HashMap<String, String>): HashMap<String, RequestBody> {
        val jsonParams: HashMap<String, RequestBody> = HashMap()
        jsonParams[""] = createPartFromString("")
        reqMap.keys.forEach { slug ->
            val data = createPartFromString(reqMap[slug] ?: "")
            jsonParams[slug] = data
        }

        return jsonParams
    }

    private fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(MultipartBody.FORM, descriptionString)
    }

    override suspend fun search(searchStr: String): Either<Failure, SearchRes> {
        val call = source.search(searchStr)
        return try {
            request(call, { it.toSearchRes() }, SearchRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }


    }


    override suspend fun searchForms(searchStr: String): Either<Failure, FormListRes> {
        val call = source.searchForms(searchStr)
        return try {
            request(call, { it.toFormListRes() }, FormListRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }
    }


    @OptIn(ExperimentalPagingApi::class)
    fun fetchForms(force: Boolean): Flow<PagingData<Form>> {
        return Pager(
            PagingConfig(pageSize = 40, enablePlaceholders = false, prefetchDistance = 3),
            remoteMediator = object : RemoteMediator<Int, Form>() {

                override suspend fun load(
                    loadType: LoadType,
                    state: PagingState<Int, Form>
                ): MediatorResult {
                    return try {
                        val loadKey = when (loadType) {
                            LoadType.REFRESH -> null
                            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                            LoadType.APPEND -> {
                                state.lastItemOrNull()
                                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                                getRedditKeys()
                            }
                        }

                        val endOfPaginationReached = if (force) {
                            val response = source.getForms((loadKey?.current_page ?: 0) + 1)

                            Timber.d("response $response")
                            val formsData = response.body()?.data
                            val formList = formsData?.forms
                            if (formList != null) {

                                withContext(Dispatchers.IO) {
                                    formsKeysDao.saveFormsKeys(
                                        FormsKeys(
                                            0,
                                            formsData.current_page ?: 1
                                        )
                                    )
                                    formsDao.save(formList)

                                    val list = arrayListOf<Form>()
                                    formList.map {
                                        withContext(Dispatchers.Default) { getForm(it.address) }?.apply {
                                            data?.form?.let {
                                                list.add(it)
                                            }
                                        }

                                    }
                                    formsDao.save(list)

                                }
                            }

                            formsData?.current_page == formsData?.page_count
                        } else {

                            true
                        }

                        MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

                    } catch (exception: IOException) {
                        MediatorResult.Error(exception)
                    } catch (exception: HttpException) {
                        MediatorResult.Error(exception)
                    }

                }

                private suspend fun getRedditKeys(): FormsKeys? {
                    return formsKeysDao.getFormsKeys().firstOrNull()

                }
            },
            pagingSourceFactory = { formsDao.getForms() }
        ).flow
    }


    override suspend fun getFormData(formAddress: String?): Either<Failure, CreateFormRes> {
        val call = source.getFormData(formAddress)
        return try {
            request(call, { it.toCreateFormRes() }, CreateFormRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }
    }

    override suspend fun getForm(formAddress: String?): CreateFormRes? {
        val call = source.getFormData(formAddress).execute()
        return call.body()

    }

    override suspend fun getFormTag(page: Int): Either<Failure, FormListRes> {
        val call = source.getFormsWithTag(page)
        return try {
            request(call, { it.toFormListRes() }, FormListRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }
    }

    override suspend fun copyForm(slug: String): Either<Failure, CreateFormRes> {
        val call = source.copyForm(slug)
        return try {
            request(call, { it.toCreateFormRes() }, CreateFormRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }
    }

    override suspend fun createLive(
        slug: String
    ): Either<Failure, LiveDashboardRes> {
        val call = source.createLive(slug)
        return try {
            request(call, { it.toLiveDashboardRes() }, LiveDashboardRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }
    }

    override suspend fun getFormDataWithLiveCode(
        body: String
    ): Either<Failure, LiveDashboardRes> {
        val jsonBody = HashMap<String, Any>()
        jsonBody.put("code", body)

        val bodyM = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(jsonBody as Map<*, *>).toString()
        )

        val call = source.getFormDataWithLiveCode(bodyM)
        return try {
            request(call, { it.toLiveDashboardRes() }, LiveDashboardRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }
    }

    override suspend fun editForm(
        slug: String,
        body: RequestBody
    ): Either<Failure, CreateFormRes> {
        val call = source.editForm(slug, body)
        return try {
            request(call, { it.toCreateFormRes() }, CreateFormRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }
    }

    override suspend fun submitFormData(
        slug: String,
        body: RequestBody?
    ): Either<Failure, SubmitFormRes> {
        val call = source.submitFormData(slug, body)
        return try {
            request(call, { it.toSubmitFormRes() }, SubmitFormRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }
    }

    override suspend fun getFormSubmits(
        slug: String
    ): Either<Failure, SubmitsResponse> {
        val call = source.getFormSubmits(slug)
        return try {
            request(call, { it.toSubmitsResponse() }, SubmitsResponse.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }
    }

    override suspend fun getSubmitsRow(liveDashboardAddress: String): Either<Failure, SubmitsResponse> {
        val call = source.getSubmitsRow(liveDashboardAddress)
        return try {
            request(call, { it.toSubmitsResponse() }, SubmitsResponse.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }
    }

    override suspend fun getLiveSubmits(liveDashboardAddress: String): Either<Failure, LiveSubmits> {
        val call = source.getLiveSubmits(liveDashboardAddress)
        return try {
            request(call, { it.toLiveSubmits() }, LiveSubmits.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }
    }

    override suspend fun editRow(
        slug: String,
        body: RequestBody
    ): Either<Failure, EditRomRes> {
        val call = source.editRow(slug, body)
        return try {
            request(call, { it.toEditRomRes() }, EditRomRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }
    }

    override suspend fun getCatList(): Either<Failure, CatListRes> {

        val call = source.getCatList()
        return try {
            request(call, { it.toCatListRes() }, CatListRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }


    }


    private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
        return try {
            val response = call.execute()
            var jObjError: JSONObject? = null
            Log.e(TAG, "request: " + response.raw())
            Log.e(TAG, "request: " + response.body())
            try {
                jObjError = JSONObject(response.errorBody()?.string())
                Log.e("request", "Repo responseErrorBody jObjError-> $jObjError")

            } catch (e: Exception) {

            }

            when (response.code()) {
                200 -> Either.Right(transform((response.body() ?: default)))
                201 -> Either.Right(transform((response.body() ?: default)))
                400 -> Either.Left(ViewFailure.responseError("$jObjError"))
                401 -> Either.Left(Failure.UNAUTHORIZED_Error)
                500 -> Either.Left(Failure.ServerError)
                else -> Either.Left(ViewFailure.responseError("$jObjError"))
            }

        } catch (exception: Throwable) {
            if (exception is UnknownHostException || exception is SocketTimeoutException) {
                Either.Left(Failure.NetworkConnection)

            } else {
                Either.Left(ViewFailure.responseError("exception++>  $exception"))

            }
        }

    }


}
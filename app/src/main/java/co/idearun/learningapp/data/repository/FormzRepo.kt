package co.idearun.learningapp.data.repository

import android.util.Log
import androidx.paging.*
import co.idearun.learningapp.common.exception.Failure
import co.idearun.learningapp.common.exception.ViewFailure
import co.idearun.learningapp.common.functional.Either
import co.idearun.learningapp.data.local.FormsKeys
import co.idearun.learningapp.data.local.dao.FormDao
import co.idearun.learningapp.data.local.dao.FormKeysDao
import co.idearun.learningapp.data.model.cat.catList.CatListRes
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.data.model.form.createForm.CreateFormRes
import co.idearun.learningapp.data.model.form.formList.FormListRes
import co.idearun.learningapp.data.model.search.SearchRes
import co.idearun.learningapp.data.model.submitForm.SubmitFormRes
import co.idearun.learningapp.data.remote.FormDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

const val TAG = "FormzRepo"

class FormzRepo(
    private val source: FormDatasource,
    private val formsDao: FormDao,
    private val formsKeysDao: FormKeysDao,
) : FormzDataSource {


    override suspend fun submitForm(
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

                } else {

                    val jObjError = JSONObject(response.errorBody()?.string())
                    Log.e("request", "submitForm jObjError-> $jObjError")

                }

            }

            override fun onFailure(call: Call<SubmitFormRes>, t: Throwable) {
                Log.e(TAG, "submitForm:onFailure " + t.message)

            }

        })

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

                                    formList.map {
                                        async { getForm(it.slug) }
                                    }.awaitAll().let {
                                        val forms = arrayListOf<Form>()
                                        it.forEach {
                                            it?.data?.form?.let {
                                                forms.add(it)
                                            }
                                        }
                                        formsDao.save(forms)

                                    }
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


    override suspend fun getFormData(formSlug: String?): Either<Failure, CreateFormRes> {
        val call = source.getFormData(formSlug)
        return try {
            request(call, { it.toCreateFormRes() }, CreateFormRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }
    }

    override suspend fun getForm(formSlug: String?): CreateFormRes? {
        val call = source.getFormData(formSlug).execute()
        return call.body()

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
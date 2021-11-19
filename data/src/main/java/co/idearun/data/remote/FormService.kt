package co.idearun.data.remote

import co.idearun.data.model.LiveSubmits
import co.idearun.data.model.SubmitsResponse
import co.idearun.data.model.cat.catList.CatListRes
import co.idearun.data.model.form.createForm.CreateFormRes
import co.idearun.data.model.form.formList.FormListRes
import co.idearun.data.model.live.LiveDashboardRes
import co.idearun.data.model.search.SearchRes
import co.idearun.data.model.submitForm.SubmitFormRes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface FormService {

    companion object {
        private const val VERSION1 = "v1/"
        private const val VERSION10 = "v1.0/"
        private const val VERSION2 = "v2/"
        private const val VERSION3 = "v3/"
        private const val VERSION31 = "v3.1/"

        private const val FORMS = "${VERSION3}form-displays/tag/{tag}/?pagination=0"
        private const val FORM_DETAIL = "${VERSION3}form-displays/address/{address}/"
        private const val SUBMIT_FORM = "${VERSION3}form-displays/slug/{slug}/submit/"
        private const val CAT_LIST = "${VERSION2}forms/category/list/"
        private const val SEARCH_FORMS = "${VERSION2}forms/list/?"
        private const val SEARCH = "${VERSION2}forms/search/?"
        private const val COPY_FORM = "${VERSION31}forms/{slug}/copy/"
        private const val CREATE_LIVE = "${VERSION3}forms/{slug}/live-dashboard-code/"
        private const val GET_FORMDATA_WITH_LIVECODE = "${VERSION10}live-dashboards/"
        private const val EDIT_FORM = "${VERSION3}forms/{slug}/"
        private const val FORM_SUBMITS = "${VERSION1}forms/form/{slug}/submits/"
        private const val LIVE_SUBMITS_ROW = "${VERSION3}live-dashboards/{liveDashboardAddress}/rows/"
        private const val LIVE_SUBMITS = "${VERSION3}live-dashboards/{liveDashboardAddress}/"

    }

    @GET(FORMS)
    suspend fun getForms(
        @Path("tag") tag: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): Response<FormListRes>

    @GET(FORMS)
    fun getFormsWithTag(
        @Path("tag") tag: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): Call<FormListRes>


    @GET(SEARCH)
    fun search(@Query("search") search: String?): Call<SearchRes>

    @GET(CAT_LIST)
    fun getCategories(): Call<CatListRes>


    @GET(SEARCH_FORMS)
    fun searchForms(@Query("search") query: String): Call<FormListRes>


    @GET(FORM_DETAIL)
    fun getFormDetail(@Path("address") address: String?): Call<CreateFormRes>

    @POST(COPY_FORM)
    fun copyForm(
        @Path("slug") slug: String,
        @Header("Authorization") token: String
    ): Call<CreateFormRes>

    @Multipart
    @POST(SUBMIT_FORM)
    fun submitForm(
        @Path("slug") slug: String,
        @PartMap req: HashMap<String, RequestBody>,
        @Part files: List<MultipartBody.Part>?
    ): Call<SubmitFormRes>

    @POST(CREATE_LIVE)
    fun createLive(
        @Path("slug") slug: String,
        @Header("Authorization") token: String
    ): Call<LiveDashboardRes>

    @PATCH(EDIT_FORM)
    fun editForm(
        @Path("slug") slug: String,
        @Header("Authorization") token: String,
        @Body body: RequestBody?
    ): Call<CreateFormRes>

    @POST(GET_FORMDATA_WITH_LIVECODE)
    fun getFormDataWithLiveCode(
        @Body body: RequestBody?
    ): Call<LiveDashboardRes>


    @POST(SUBMIT_FORM)
    fun submitFormData(
        @Path("slug") slug: String,
        @Body body: RequestBody
    ): Call<SubmitFormRes>


    @GET(FORM_SUBMITS)
    fun getFormSubmits(
        @Path("slug") slug: String,
        @Header("Authorization") token: String
    ): Call<SubmitsResponse>


    @GET(LIVE_SUBMITS_ROW)
    fun getSubmitsRow(
        @Path("liveDashboardAddress") liveDashboardCode : String,
    ): Call<SubmitsResponse>


    @GET(LIVE_SUBMITS)
    fun getLiveSubmits(
        @Path("liveDashboardAddress") liveDashboardAddress : String,
    ): Call<LiveSubmits>


}
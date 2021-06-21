package co.idearun.learningapp.feature.viewmodel

import android.util.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.idearun.learningapp.common.BaseMethod
import co.idearun.learningapp.common.base.BaseViewModel
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.data.model.form.SubmitEntity
import co.idearun.learningapp.data.model.form.createForm.CreateFormData
import co.idearun.learningapp.data.model.form.createForm.CreateFormRes
import co.idearun.learningapp.data.repository.FormzRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import kotlin.random.Random

class UIViewModel(private val repository: FormzRepo) : BaseViewModel() {

    private var formSlug: String? = null
    private var rowSlug: String? = null
    private var progressNumber: Int? = null
    private var formReqList = HashMap<String, String>()
    private var fileList = HashMap<String, Fields>()

    private var files: ArrayList<MultipartBody.Part> = arrayListOf()
    private val _formData = MutableLiveData<CreateFormData>().apply { value = null }
    val formData: LiveData<CreateFormData> = _formData
    private val _form = MutableLiveData<Form>().apply { value = null }
    val form: LiveData<Form> = _form
    private val _submitedData = MutableLiveData<Boolean>().apply { value = null }
    val submitedData: LiveData<Boolean> = _submitedData
    private val _submitEntity = MutableLiveData<SubmitEntity>().apply { value = null }
    val submitEntity: LiveData<SubmitEntity> = _submitEntity
    private val _isLoading = MutableLiveData<Boolean>().apply { value = null }
    val isLoading: LiveData<Boolean> = _isLoading
    private val _fields = MutableLiveData<ArrayList<Fields>>().apply { value = null }
    val fields: LiveData<ArrayList<Fields>> = _fields
    private val _errorField = MutableLiveData<Fields>().apply { value = null }
    val errorField: LiveData<Fields> = _errorField
    private val _fieldFielName = MutableLiveData<Fields>().apply { value = null }
    val fieldFielName: LiveData<Fields> = _fieldFielName
    private val _selectedTime = MutableLiveData<String>().apply { value = null }
    val selectedTime: LiveData<String> = _selectedTime
    private val _selectedDate = MutableLiveData<String>().apply { value = null }
    val selectedDate: LiveData<String> = _selectedDate
    private val _npsPos = MutableLiveData<Int>().apply { value = null }
    val npsPos: LiveData<Int> = _npsPos

    private var requiredField = arrayListOf<Fields>()

    fun retrieveForm() = launch {
        showLoading()
        val result = async(Dispatchers.IO) { repository.getFormData(formSlug) }.await()
        result.either(::handleFailure, ::handleFormData)

    }

    fun retrieveFormFromDB() = launch {
        val result = withContext(Dispatchers.IO) { repository.getFormFromDB(formSlug ?: "") }
        _form.value = result

    }

    private fun handleFormData(res: CreateFormRes) {
        res?.let { it ->
            it.data?.let {
                Timber.e("calc ${it.form?.fields_list}")
                _formData.value = it
                it.form?.fields_list?.let {
                    _fields.value = it
                    hideLoading()
                }
            }

            hideLoading()

        }

    }


    fun initFormSlug(slug: String) {
        formSlug = slug
    }

    fun initRowSlug(slug: String) {
        rowSlug = slug
    }

    fun addKeyValueToReq(slug: String, value: Any) {
        formReqList[slug] = value.toString()
    }

    fun addFileToReq(field: Fields?, value: String) {
        field?.let {
            fileList[value] = field
            initFileName(field.slug, value)
        }

    }

    fun initFileName(slug: String?, filePath: String) {
        val file = File(filePath)
        val fielField = Fields()
        fielField.slug = slug
        fielField.title = file.name
        _fieldFielName.value = fielField

    }

    fun removeFile(slug: String?) {
        initFileName(null, "")
        val fileList = files

        fileList.forEach { mp ->
            Timber.e("removeFile ${mp.body}")

            mp.headers?.let { headers ->
                Timber.e("headers $headers")
                if (slug != null && headers.toString().contains(slug)) {
                    files.remove(mp)
                }

            }
        }
    }

    fun showLoading() {

        _isLoading.value = true
    }

    fun hideLoading() {
        _isLoading.value = false

    }

    fun initSelectedTime(time: String) {
        Timber.e("initSelectedTime $time")
        _selectedTime.value = time
    }

    fun initSelectedDate(date: String) {
        Timber.e("initSelectedDate $date")
        _selectedDate.value = date
    }

    fun getSubmitEntity() = launch {
        val entity = repository.getSubmitEntity(formSlug ?: "")
        entity?.let {
            if (entity.newRow == true) {
                progressNumber = 0
            } else {
                formReqList = entity.formReq
                fileList = entity.files
                progressNumber = entity.progressNumber
            }
            Timber.e("getSubmitEntity $progressNumber ")

            _submitEntity.value = entity
        }

    }


    fun saveEditSubmitToDB(newRow: Boolean, visibleItemPosition: Int) = launch {
        progressNumber=visibleItemPosition
        Timber.e("if $progressNumber ")

        val entity = if (progressNumber == 0) {
            SubmitEntity(
                0,
                Random.nextInt(),
                false,
                newRow,
                rowSlug,
                formSlug,
                formReqList,
                fileList,
                progressNumber
            )

        } else if (submitEntity.value != null) {
            val value = submitEntity.value!!
            value.files = fileList
            value.formReq = formReqList
            value.progressNumber = progressNumber
            value
        } else {
            null
        }

        if (entity != null) {
            repository.saveSubmit(entity)

        }


    }

    fun errorFind(it: JSONObject, baseMethod: BaseMethod) {
        it.keys().forEach { key ->
            val err = baseMethod.retrieveErr(it, key)
            val errField = Fields()
            errField.slug = key
            errField.title = err
            _errorField.value = errField
        }


    }

    fun setErrorToField(it: Fields, msg: String) {
        val errField = Fields()
        errField.slug = it.slug
        errField.title = msg
        _errorField.value = errField

    }

    fun hideErrorField() {
        val fields = Fields()
        fields.slug = null
        fields.title = ""

        _errorField.value = fields
    }


    fun npsBtnClicked(field: Fields, pos: Int) {
        _npsPos.value = pos
        addKeyValueToReq(field.slug!!, pos)
    }

    fun reuiredField(it: Fields) {
        requiredField.add(it)
    }

}
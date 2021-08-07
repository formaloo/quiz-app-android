package co.idearun.learningapp.feature.lesson

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.webkit.URLUtil
import androidx.core.content.ContextCompat
import androidx.work.*
import co.idearun.learningapp.common.BaseMethod
import co.idearun.learningapp.common.Constants
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.feature.viewmodel.UIViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.io.File
import java.util.*
import co.idearun.learningapp.R
import co.idearun.learningapp.common.FileUtils
import co.idearun.learningapp.feature.BaseActivity
import co.idearun.learningapp.feature.lesson.listener.FieldsListener
import co.idearun.learningapp.feature.viewmodel.FormViewModel

open class LessonBaseActivity : BaseActivity(), FieldsListener, KoinComponent {

    private lateinit var enDatePicker: DatePickerDialog
    val viewModel: UIViewModel by viewModel()
     val formVM: FormViewModel by viewModel()

    var _field: Fields? = null
    val baseMethod: BaseMethod by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            Constants.PICKFILE_RESULT_CODE -> if (resultCode != RESULT_CANCELED) {

                intent?.let {
                    it.data?.let {
                        resultIsReady(it)

                    }
                }

            }
        }
    }

    fun openFilePickerInten(type: String, action: String) {
        if (baseMethod.hasPermissions(
                applicationContext,
                *Constants.PERMISSIONS_EXTERNAL_STORAGE
            )
        ) {
            baseMethod.openFilePickerInten(this, type, action)

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(Constants.PERMISSIONS_EXTERNAL_STORAGE, Constants.PERMISSION_ALL)
        }
    }

    fun updateTheme(form: Form?) {
        val hexColor = baseMethod.getHexHashtagColorFromRgbStr(form?.background_color)

        if (hexColor != null || form?.background_image != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
                window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent);
            };

        }

        if (form?.background_image == null && hexColor != null) {
            window.setBackgroundDrawable(ColorDrawable(Color.parseColor(hexColor)))

        } else {
            baseMethod.loadImage(form?.background_image, window, hexColor)

        }


    }

    fun selectedDateIsReady(
        strDateToShow: String,
        strDateToSend: String,
        field: Fields?
    ) {
        if (checkDateLimit(strDateToSend, field)) {
            viewModel.initSelectedDate(strDateToShow)
            viewModel.addKeyValueToReq(field?.slug!!, strDateToSend)
            viewModel.setErrorToField(Fields(), "")

        } else {
            viewModel.setErrorToField(
                field ?: Fields(),
                "${getString(co.idearun.learningapp.R.string.from_date)} : ${
                    baseMethod.getDateOnLocale(
                        field?.from_date ?: ""
                    ) ?: "-"
                }" +
                        "  " +
                        "${getString(co.idearun.learningapp.R.string.to_date)} : ${
                            baseMethod.getDateOnLocale(
                                field?.to_date ?: ""
                            ) ?: "-"
                        }"
            )
        }

    }

    fun checkDateLimit(strDateToSend: String, fields: Fields?): Boolean {
        val dateStr = baseMethod.converStrToDate(strDateToSend)
        val afterDate =
            fields?.from_date == null || dateStr?.after(baseMethod.converStrToDate(fields.from_date!!)) ?: false
        val beforeDate =
            fields?.to_date == null || dateStr?.before(baseMethod.converStrToDate(fields.to_date!!)) ?: false
        return afterDate && beforeDate

    }


    fun openEnDatePicker(field: Fields?) {

        val cal = Calendar.getInstance()
        cal.time = Date()
        baseMethod.converStrToDate("1900-00-01")?.let { date ->
            cal.time = date
        }

        field?.from_date?.let {
            baseMethod.converStrToDate(it)?.let { date ->
                cal.time = date
            }

        }

        enDatePicker = DatePickerDialog(
            this, { p0, year, month, day ->
                val monthInt = month + 1
                val monthStr = if (monthInt.toString().length < 2) {
                    "0$monthInt"
                } else {
                    "$monthInt"
                }

                val dayStr = if (day.toString().length < 2) {
                    "0$day"
                } else {
                    "$day"
                }

                val strDate = "$year-$monthStr-$dayStr"

                selectedDateIsReady(strDate, strDate, field)

            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        enDatePicker.setOnShowListener {
            enDatePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                ?.setTextColor(ContextCompat.getColor(this, R.color.gray2))
            enDatePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
                ?.setTextColor(ContextCompat.getColor(this, R.color.teal_700))
            enDatePicker.getButton(DatePickerDialog.BUTTON_NEUTRAL)
                ?.setTextColor(ContextCompat.getColor(this, R.color.teal_700))

        }

        enDatePicker.show()
    }

    fun closeEnDatePicker() {
        enDatePicker.dismiss()
    }

    fun resultIsReady(it: Uri) {
        FileUtils(this).getPath(it)?.let { path ->
            Timber.e("Path $path");

            if (checkFileSize(_field, path)) {
                viewModel.setErrorToField(
                    _field!!,
                    "${getString(R.string.file_is_bigger_than_required_size)} : ${_field?.max_size}"
                )
            } else {
                viewModel.addFileToReq(_field, path)

            }
        }

    }

    fun checkFileSize(_field: Fields?, path: String): Boolean {
        val file = File(path)
        val file_size: Int = java.lang.String.valueOf(file.length() / 1024).toInt()
        val maxSize = _field?.max_size

        return maxSize != null && file_size > maxSize.toInt()
    }

    override fun openDatePicker(field: Fields?) {
        openEnDatePicker(field)

    }

    override fun removeDatePicker() {
        closeEnDatePicker()

    }

    override fun openFilePicker(field: Fields?, type: String, action: String) {
        Timber.e("openFilePicker")
        _field = field
        openFilePickerInten(type, action)
    }

    override fun openUrl(v: String) {
        if (v.isNotEmpty() && URLUtil.isValidUrl(v)) {
            baseMethod.openUri(this@LessonBaseActivity, v)
        }
    }

}

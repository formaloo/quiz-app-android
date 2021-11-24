package co.idearun.game.feature.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.ArrayMap
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import co.idearun.data.model.FieldData
import co.idearun.game.base.BaseFragment
import co.idearun.game.adapter.FormFieldsAdapter
import co.idearun.game.R
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_form.*
import kotlinx.android.synthetic.main.fragment_player_name.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import splitties.alertdialog.appcompat.*
import timber.log.Timber

class PlayerFormFragment : BaseFragment() {

    lateinit var adapter: FormFieldsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_form, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val formVm: FormViewModel by activityViewModels()
        adapter = FormFieldsAdapter(formVm)
        parentRecyclerView.adapter = adapter



        formVm.initLessonAddress(formVm.userForm.value?.form?.address!!)
        formVm.getFormData()



        formVm.form1.observe(this, {
            Timber.i("TAG get form $it")
            Timber.i("TAG get form ${it.fields_list?.size}")
            val fields = it.fields_list
            adapter.submitList(it.fields_list)

            fields?.forEach {
                Timber.i("TAG field title ${it.title} = ${it.slug}")
            }
        })

        submitFormBtn.setOnClickListener {
            formVm.submitFormData(formVm.userForm.value?.form?.slug!!)
        }

       /*
        val body = ArrayMap<String, Any>()
        submitFormBtn.setOnClickListener {

            adapter.fieldSlugList.forEachIndexed { index, fields ->
                val view = parentRecyclerView.getChildAt(index)
                val viewHolder = adapter.FormFieldsViewHolder(view)

                if (!viewHolder.fieldsEdt.text.toString().isBlank()) {
                    val value = viewHolder.fieldsEdt.text.toString()
                    val slugField = fields.slug!!
                    body[slugField] = value

                    Timber.i(fields.title + " = " + slugField + " = " + value)
                }
                body["name_field"] = formVm.userName.value
            }.also {
                val bodyM = RequestBody.create(
                    "application/json; charset=utf-8".toMediaTypeOrNull(),
                    JSONObject(body).toString()
                )

                formVm.submitFormData(formVm.userForm.value?.form?.slug!!, bodyM)
            }
        }
*/
        formVm.submitForm.observe(this, {
            openAlertWithNavigation("your form submit!")
        })

        formVm.failure.observe(this, {
            formVm.hideLoading()
            if (it.msgRes?.contains("404")!!) {
                openAlert("you late, Game is over")
            } else {
                checkFailureStatus(it)
            }
        })


        formVm.isLoading.observe(this, {
            if (it) loading.visibility = View.VISIBLE else loading.visibility = View.GONE
        })

    }

    fun openAlertWithNavigation(msg: String) {
        requireContext().alertDialog {
            message = msg
            okButton { findNavController().navigate(R.id.action_playerFormFragment_to_playerResultFragment)}
        }.onShow {
            positiveButton.setTextColor(resources.getColor(android.R.color.holo_blue_dark))
        }.show()
    }
}
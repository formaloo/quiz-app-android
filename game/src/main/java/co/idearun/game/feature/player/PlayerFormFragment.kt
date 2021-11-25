package co.idearun.game.feature.player

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import co.idearun.game.PlayerInfo
import co.idearun.game.base.BaseFragment
import co.idearun.game.adapter.FormFieldsAdapter
import co.idearun.game.R
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_form.*
import kotlinx.android.synthetic.main.fragment_form.loading
import kotlinx.android.synthetic.main.fragment_form.parentRecyclerView
import org.koin.android.viewmodel.ext.android.viewModel
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

        val formVm: FormViewModel by viewModel()
        adapter = FormFieldsAdapter(formVm)
        parentRecyclerView.adapter = adapter



        formVm.initLessonAddress(PlayerInfo.playerFormInfo?.form?.address!!)
        formVm.getFormData()
        

        formVm.form1.observe(this, {
            formName.text = it.title
            formDesc.text = Html.fromHtml(it.description)

            val fields = it.fields_list
            it.fields_list?.removeIf {
                it.type.equals("hidden")
            }
            adapter.submitList(it.fields_list)

            fields?.forEach {
                Timber.i("TAG field title ${it.title} = ${it.slug}")
            }
        })

        submitFormBtn.setOnClickListener {
            if (!formVm.body.value.isNullOrEmpty()) {
                formVm.submitFormData(PlayerInfo.playerFormInfo?.form?.slug!!)
            } else openAlert("your form is empty! Please Fill")
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
            openAlertWithNavigation("your form submit!",R.id.action_playerFormFragment_to_playerResultFragment)
        })

        formVm.failure.observe(this, {
            formVm.hideLoading()
            if (it.msgRes?.contains("404")!!) {
                openAlertWithNavigation("you late, Game is over", R.id.action_playerFormFragment_to_mainFragment )
            } else {
                checkFailureStatus(it)
            }
        })


        formVm.isLoading.observe(this, {
            if (it) loading.visibility = View.VISIBLE else loading.visibility = View.GONE
        })

    }

    fun openAlertWithNavigation(msg: String, navigateId: Int) {
        requireContext().alertDialog {
            message = msg
            setCancelable(false)
            okButton { findNavController().navigate(navigateId) }
        }.onShow {
            positiveButton.setTextColor(resources.getColor(android.R.color.holo_blue_dark))
        }.show()
    }
}

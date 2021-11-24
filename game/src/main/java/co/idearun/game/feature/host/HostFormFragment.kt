package co.idearun.game.feature.host

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import co.idearun.common.TokenContainer
import co.idearun.game.R
import co.idearun.game.adapter.FormFieldsAdapter
import co.idearun.game.base.BaseFragment
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_form_host.*
import org.koin.android.viewmodel.ext.android.viewModel
import splitties.alertdialog.appcompat.*
import timber.log.Timber

class HostFormFragment : BaseFragment() {

    lateinit var adapter: FormFieldsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_form_host, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val formVm: FormViewModel by viewModel()
        adapter = FormFieldsAdapter(formVm)
        parentRecyclerView.adapter = adapter

        val liveCode = arguments?.getString("liveCode")
        val liveDashboardAddress = arguments?.getString("liveDashboardAddress")
        var slug: String? = null

        formVm.getFormDataWithLiveCode(liveCode!!)

        formVm.liveForm.observe(this, {
            val address = it?.form?.address!!
            slug = it.form?.slug!!
            formVm.initLessonAddress(address)
            formVm.getFormData()
        })





        formVm.form1.observe(this, {
            formName.text = it.title
            formDesc.text = it.description
            Timber.i("TAG get form $it")
            Timber.i("TAG get form ${it.fields_list?.size}")
            val fields = it.fields_list
            adapter.submitList(it.fields_list)

            fields?.forEach {
                Timber.i("TAG field title ${it.title} = ${it.slug}")
            }
        })


/*        val body = ArrayMap<String, Any>()
        submitFormBtn.setOnClickListener {

            adapter.fieldSlugList.forEachIndexed { index, fields ->
                val view = parentRecyclerView.getChildAt(index)
                val viewHolder = adapter.FormFieldsViewHolder(view)

                if (!viewHolder.fieldsEdt.text.toString().isBlank()) {
                    Timber.i("test nulla")
                    val value = viewHolder.fieldsEdt.text.toString()
                    val slugField = fields.slug!!
                    body[slugField] = value
                }

                // Timber.i(fields.title + " = " + slug + " = " + value)
            }.also {
                val bodyM = RequestBody.create(
                    "application/json; charset=utf-8".toMediaTypeOrNull(),
                    JSONObject(body).toString()
                )

                formVm.submitFormData(slug!!, bodyM)
            }

        }*/

        submitFormBtn.setOnClickListener {
            if (!formVm.body.value.isNullOrEmpty())
                formVm.submitFormData(slug!!)
            else openAlert("your form is empty! Please Fill")
        }

        formVm.submitForm.observe(this, {
            openAlert("your form submit!")
        })

        endGameBtn.setOnClickListener {
            requireContext().alertDialog {
                message =
                    "Tell your friends to submit their answers NOW! Once you hit this, anyone who didn't submit their answers will lose! Are you sure?"
                okButton {
                    formVm.disableForm(
                        slug!!,
                        "JWT ${TokenContainer.authorizationToken}",
                        false
                    )
                }
                cancelButton()
            }.onShow {
                positiveButton.setTextColor(resources.getColor(android.R.color.holo_blue_dark))
            }.show()

        }

        formVm.disableForm.observe(this, {
            val args = Bundle()
            args.putString("slug", slug)
            args.putString("liveDashboardAddress", liveDashboardAddress)
            findNavController().navigate(R.id.action_hostFormFragment_to_resultFragment, args)
        })

        formVm.failure.observe(this, {
            formVm.hideLoading()
            checkFailureStatus(it)
        })

        formVm.isLoading.observe(this, {
            if (it) loading.visibility = View.VISIBLE else loading.visibility = View.GONE
        })


    }
}

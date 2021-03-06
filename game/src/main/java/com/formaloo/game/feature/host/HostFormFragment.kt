package com.formaloo.game.feature.host

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.formaloo.common.Constants
import com.formaloo.game.feature.PlayerInfo
import com.formaloo.game.R
import com.formaloo.game.feature.adapter.FormFieldsAdapter
import com.formaloo.game.base.BaseFragment
import com.formaloo.game.feature.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_form_host.*
import kotlinx.android.synthetic.main.fragment_form_host.loading
import kotlinx.android.synthetic.main.fragment_form_host.parentRecyclerView
import org.koin.android.viewmodel.ext.android.viewModel
import splitties.alertdialog.appcompat.*

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
        var slug: String? = null
        var address: String? = null


        formVm.getFormDataWithLiveCode(liveCode!!)
        formVm.liveForm.observe(this, {
            address = it?.form?.address!!
            slug = it.form?.slug!!

            formVm.initLessonAddress(address!!)
            formVm.getFormData()
        })


        formVm.form1.observe(this, {
            formName.text = it.title
            formDesc.text = Html.fromHtml(it.description)

            val hiddenFieldSlug = it.fields_list?.find { it.type.equals(Constants.HIDDEN) }
            PlayerInfo.updatePlayerNameSlug(hiddenFieldSlug?.slug)

            val fieldsList = it.fields_list
            fieldsList?.removeIf {
                it.type.equals(Constants.HIDDEN)
            }
            adapter.submitList(fieldsList)
        })

        submitFormBtn.setOnClickListener {
            if (!formVm.body.value.isNullOrEmpty())
                formVm.submitFormData(slug!!)
            else openAlert(getString(R.string.empty_form_fields))
        }

        formVm.submitForm.observe(this, {
            openAlert(getString(R.string.form_submited))
            submitFormBtn.isEnabled = false
        })

        endGameBtn.setOnClickListener {
            requireContext().alertDialog {
                message =
                    getString(R.string.disable_from_msg)
                okButton {
                    formVm.disableForm(slug!!, false)
                }
                cancelButton()
            }.onShow {
                positiveButton.setTextColor(resources.getColor(android.R.color.holo_blue_dark))
            }.show()

        }

        formVm.disableForm.observe(this, {
            val args = Bundle()
            args.putString("slug", slug)
            args.putString("liveCode", liveCode)
            findNavController().navigate(R.id.action_hostFormFragment_to_resultFragment, args)
        })


        // handle failure
        formVm.failure.observe(this, {
            formVm.hideLoading()
            checkFailureStatus(it)
        })

        // handle loading
        formVm.isLoading.observe(this, {
            if (it) loading.visibility = View.VISIBLE else loading.visibility = View.GONE
        })

        // disable backpress button
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })
    }
}

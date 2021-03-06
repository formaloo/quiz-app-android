package com.formaloo.game.feature.player

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.formaloo.game.feature.PlayerInfo
import com.formaloo.game.base.BaseFragment
import com.formaloo.game.feature.adapter.FormFieldsAdapter
import com.formaloo.game.R
import com.formaloo.game.feature.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_form.*
import kotlinx.android.synthetic.main.fragment_form.loading
import kotlinx.android.synthetic.main.fragment_form.parentRecyclerView
import org.koin.android.viewmodel.ext.android.viewModel
import splitties.alertdialog.appcompat.*

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



        /*** get form detail and fields to show and play game
        * we don't show hidden fields Therefore remove from list and save hidden field data
        * in [PlayerInfo] object */
        formVm.initLessonAddress(PlayerInfo.playerFormInfo?.form?.address!!)
        formVm.getFormData()
        formVm.form1.observe(this, {
            formName.text = it.title
            formDesc.text = Html.fromHtml(it.description)

            val hiddenFieldSlug = it.fields_list?.find { it.type == "hidden" }
            PlayerInfo.updatePlayerNameSlug(hiddenFieldSlug?.slug)

            val fieldsList = it.fields_list
            fieldsList?.removeIf {
                it.type.equals("hidden")
            }

            adapter.submitList(fieldsList)
        })


        /*** submit form! body handle in [PlayerChildAdapter]
         */
        submitFormBtn.setOnClickListener {
            if (!formVm.body.value.isNullOrEmpty()) {
                formVm.submitFormData(PlayerInfo.playerFormInfo?.form?.slug!!)
            } else openAlert(getString(R.string.empty_form_data))
        }

        formVm.submitForm.observe(this, {
            openAlertWithNavigation(
                getString(R.string.your_from_submit),
                R.id.action_playerFormFragment_to_playerResultFragment
            )
        })


        // handle failure
        formVm.failure.observe(this, {
            formVm.hideLoading()
            if (it.msgRes?.contains("404")!!) {
                openAlertWithNavigation(
                    getString(R.string.you_late),
                    R.id.action_playerFormFragment_to_mainFragment
                )
            } else {
                checkFailureStatus(it)
            }
        })

        // handle loading
        formVm.isLoading.observe(this, {
            if (it) loading.visibility = View.VISIBLE else loading.visibility = View.GONE
        })

    }

    // custom dialog alert
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

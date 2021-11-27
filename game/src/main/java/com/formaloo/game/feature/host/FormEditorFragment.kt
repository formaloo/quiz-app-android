package com.formaloo.game.feature.host

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.collection.ArrayMap
import androidx.navigation.fragment.findNavController
import com.formaloo.common.TokenContainer
import com.formaloo.game.feature.PlayerInfo
import com.formaloo.game.R
import com.formaloo.game.feature.adapter.FormFieldsAdapter
import com.formaloo.game.base.BaseFragment
import com.formaloo.game.feature.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_formeditor.*
import org.koin.android.viewmodel.ext.android.viewModel

class FormEditorFragment : BaseFragment() {

    lateinit var adapter: FormFieldsAdapter

    // inflate layout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_formeditor, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val formVm: FormViewModel by viewModel()

        // setup list
        adapter = FormFieldsAdapter(formVm)
        adapter.disableField = true
        parentRecyclerView.adapter = adapter

        // get arguments from prev fragment
        val formAddress = arguments?.getString("formAddress")
        val formSlug = arguments?.getString("formSlug")

        // get from data from server
        formVm.initLessonAddress(formAddress!!)
        formVm.getFormData()

        formVm.form1.observe(this, {
            // set form title and description
            formTitleEdt.setText(it.title)
            formDescriptionEdt.setText(Html.fromHtml(it.description))

            // delete player name field from fields preview
            val fieldsList = it.fields_list
            fieldsList?.removeIf {
                it.type.equals("hidden")
            }
            adapter.submitList(it.fields_list)
        })


        /* when click on start
        * your form title, description send to formaloo server and update
        *
        * public rows allow to access make your form to formaloo live
        * host name save to PlayerInfo object and submit with fields data */
        setBtn.setOnClickListener {
            val hostName = hostNameEdt.text.toString()

            if (!hostName.isBlank()) {
                PlayerInfo.updatePlayerName(hostNameEdt.text.toString())

                val req = ArrayMap<String, Any>()
                req["title"] = formTitleEdt.text.toString()
                req["description"] = formDescriptionEdt.text.toString()
                req["public_rows"] = true

                formVm.editForm(formSlug!!, req)
            } else {
                openAlert(getString(R.string.empty_name_msg))
            }
        }

        /* when form title, description, createLive request called
        * createLive make your form to formaloo Live*/
        formVm.editForm.observe(this, {
            formVm.createLive(it.slug)
        })

        /* now your live is ready!
        * we send live code and live dashboard address to share fragment*/
        formVm.liveForm.observe(this, {
            val args = Bundle()
            args.putString("liveCode", it.code)
            args.putString("liveDashboardAddress", it.form?.liveDashboardAddress)
            findNavController().navigate(R.id.action_formEditorFragment_to_shareFragment, args)
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
            override fun handleOnBackPressed() {
            }
        })

    }
}

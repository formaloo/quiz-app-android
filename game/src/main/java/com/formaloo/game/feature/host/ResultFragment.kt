package com.formaloo.game.feature.host

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.collection.ArrayMap
import androidx.navigation.fragment.findNavController
import com.formaloo.common.TokenContainer
import com.formaloo.data.model.FieldData
import com.formaloo.data.model.TopFieldsItem
import com.formaloo.game.R
import com.formaloo.game.feature.adapter.HostParentAdapter
import com.formaloo.game.base.BaseFragment
import com.formaloo.game.feature.adapter.model.ParentItem
import com.formaloo.game.feature.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_result.*
import org.koin.android.viewmodel.ext.android.viewModel

class ResultFragment : BaseFragment() {

    lateinit var adapterHostParent: HostParentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_result, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val formVm: FormViewModel by viewModel()

        // get arguments
        val slug = arguments?.getString("slug")
        val liveCode = arguments?.getString("liveCode")

        // start new game button and navigate to main fragment
        resultAction.text = getString(R.string.start_new_game)
        resultAction.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_mainFragment)
        }



        // get form data with live code to access form title and description
        formVm.getFormDataWithLiveCode(liveCode!!)
        formVm.liveForm.observe(this, {
            resultFormName.text = it.form?.title
        })


        /*** get form submits with token and show in recyclerview
        * submits send to adapter in format of parent item model
        * and we have a list of list data like table
        * handle this section with recyclerView in recyclerView [HostChildAdapter] and [HostParentAdapter]*/

        adapterHostParent = HostParentAdapter(formVm)
        parentRecyclerView.adapter = adapterHostParent

        var fieldDataMapList = arrayListOf<ArrayMap<String, FieldData>>()
        var topFieldsItem = arrayListOf<List<TopFieldsItem>>()
        var parentItem = arrayListOf<ParentItem>()

        formVm.getFormSubmits(slug!!)
        formVm.submits.observe(this, {
            val topFieldData = it?.data?.topFields

            it.data?.rows?.forEach {
                var fieldDataMap = ArrayMap<String, FieldData>()
                it?.renderedData?.entries?.forEach {
                    fieldDataMap.put(it.key, it.value)
                }
                fieldDataMapList.add(fieldDataMap)
                topFieldsItem.add(topFieldData as List<TopFieldsItem>)
                parentItem.add(ParentItem(it?.slug!!, topFieldsItem, fieldDataMapList))
            }
            adapterHostParent.setItemList(parentItem)
        })


        /* any time game host change player status and point field
        *  edit row observe and show a dialog
        *  this operation handle in HostParentAdapter but dialog show in fragment */
        formVm.editRow.observe(this, {
            openAlert("point/status updated")
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
                findNavController().navigate(R.id.action_resultFragment_to_mainFragment)
            }
        })
    }
}

package com.formaloo.game.feature.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.ArrayMap
import androidx.navigation.fragment.findNavController
import com.formaloo.data.model.FieldData
import com.formaloo.data.model.TopFieldsItem
import com.formaloo.game.base.BaseFragment
import com.formaloo.game.feature.adapter.model.ParentItem
import com.formaloo.game.R
import com.formaloo.game.feature.adapter.PlayerParentAdapter
import com.formaloo.game.feature.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_result.*
import androidx.activity.OnBackPressedCallback
import com.formaloo.game.feature.PlayerInfo
import org.koin.android.viewmodel.ext.android.viewModel


class PlayerResultFragment : BaseFragment() {

    lateinit var adapterParentParent: PlayerParentAdapter

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
        val liveDashboardAddress = PlayerInfo.playerFormInfo?.form?.liveDashboardAddress


        // get form title with form address
        formVm.initLessonAddress(PlayerInfo.playerFormInfo?.form?.address!!)
        formVm.getFormData()
        formVm.form1.observe(this, {
            resultFormName.text = it.title
        })

        // play again section
        startAction.visibility = View.VISIBLE
        startAction.text = getString(R.string.play_again)
        startAction.setOnClickListener {
            findNavController().navigate(R.id.action_playerResultFragment_to_mainFragment)
        }

        // update form result data
        resultAction.setOnClickListener {
            formVm.getSubmitsRow(liveDashboardAddress!!)
        }


        /*** get form submits and show in recyclerview
         * submits send to adapter in format of parent item model
         * and we have a list of list data like table
         * handle this section with recyclerView in recyclerView [PlayerChildAdapter] and [PlayerParentAdapter]*/

        adapterParentParent = PlayerParentAdapter()
        parentRecyclerView.adapter = adapterParentParent

        formVm.getSubmitsRow(liveDashboardAddress!!)
        formVm.submits.observe(this, {
            var fieldDataMapList = arrayListOf<ArrayMap<String, FieldData>>()
            var topFieldsItem = arrayListOf<List<TopFieldsItem>>()
            var parentItem = arrayListOf<ParentItem>()

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
            adapterParentParent.setItemList(parentItem)
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

        // disable backpress
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
    }
}

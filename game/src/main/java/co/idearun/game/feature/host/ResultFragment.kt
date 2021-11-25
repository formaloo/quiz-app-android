package co.idearun.game.feature.host

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.collection.ArrayMap
import androidx.navigation.fragment.findNavController
import co.idearun.common.TokenContainer
import co.idearun.data.model.FieldData
import co.idearun.data.model.TopFieldsItem
import co.idearun.game.R
import co.idearun.game.feature.adapter.HostParentAdapter
import co.idearun.game.base.BaseFragment
import co.idearun.game.model.ParentItem
import co.idearun.game.feature.viewmodel.FormViewModel
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
        val slug = arguments?.getString("slug")
        val liveCode = arguments?.getString("liveCode")
        val liveDashboardAddress = arguments?.getString("liveDashboardAddress")

        formVm.getLiveSubmits(liveDashboardAddress!!)
        formVm.getFormDataWithLiveCode(liveCode!!)

        formVm.liveForm.observe(this, {
            resultFormName.text = it.form?.title
        })


        formVm.editRow.observe(this, {
            openAlert("point/status updated")
        })

        resultAction.text = getString(R.string.start_new_game)
        resultAction.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_mainFragment)
        }

        adapterHostParent = HostParentAdapter(requireContext(), formVm)
        parentRecyclerView.adapter = adapterHostParent

        formVm.getFormSubmits(slug!!, "JWT ${TokenContainer.authorizationToken}")

        var fieldDataMapList = arrayListOf<ArrayMap<String, FieldData>>()
        var topFieldsItem = arrayListOf<List<TopFieldsItem>>()
        var parentItem = arrayListOf<ParentItem>()

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

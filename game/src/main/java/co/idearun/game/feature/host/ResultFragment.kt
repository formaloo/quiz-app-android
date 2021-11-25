package co.idearun.game.feature.host

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.collection.ArrayMap
import androidx.navigation.fragment.findNavController
import co.idearun.common.TokenContainer
import co.idearun.common.base.OnRvItemClickListener
import co.idearun.data.model.FieldData
import co.idearun.data.model.TopFieldsItem
import co.idearun.game.R
import co.idearun.game.adapter.ChildItemAdapter
import co.idearun.game.adapter.ParentItemAdapter
import co.idearun.game.base.BaseFragment
import co.idearun.game.model.ParentItem
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_result.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class ResultFragment : BaseFragment() {

    lateinit var adapterChild: ChildItemAdapter
    lateinit var adapterParent: ParentItemAdapter

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
        val address = arguments?.getString("address")
        val slug = arguments?.getString("slug")
        val liveCode = arguments?.getString("liveCode")
        val liveDashboardAddress = arguments?.getString("liveDashboardAddress")


        Timber.i("$address vs slug: $slug")
        formVm.getLiveSubmits(liveDashboardAddress!!)

        formVm.getFormDataWithLiveCode(liveCode!!)

        formVm.liveForm.observe(this,{
            Timber.i("slug: $it")
            resultFormName.text = it.form?.title
        })


        formVm.editRow.observe(this,{
            openAlert("point/status updated")
        })

        resultAction.text = "Start new game"
        resultAction.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_mainFragment)
        }
        //adapterChild = ChildItemAdapter()
        adapterParent = ParentItemAdapter(requireContext(), formVm)
        parentRecyclerView.adapter = adapterParent

        adapterChild = ChildItemAdapter(requireContext())
        adapterChild.setOnRvItemClickListener(object : OnRvItemClickListener<ChildItemAdapter.callBackData>{
            override fun onItemClick(item: ChildItemAdapter.callBackData, position: Int) {
                Timber.i("test test test ${item.point}   ${item.status}")
            }

        })

        //formVm.getSubmitsRow(liveDashboardAddress!!)
        formVm.getFormSubmits(slug!!, "JWT ${TokenContainer.authorizationToken}")

        var fieldDataMapList = arrayListOf<ArrayMap<String, FieldData>>()
        var topFieldsItem = arrayListOf<List<TopFieldsItem>>()
        var parentItem = arrayListOf<ParentItem>()

        formVm.submits.observe(this, {
            Timber.i("field size " + it?.data?.rows?.size)

            val topFieldData = it?.data?.topFields

            it.data?.rows?.forEach {
                var fieldDataMap = ArrayMap<String, FieldData>()
                it?.renderedData?.entries?.forEach {
                    Timber.i("rendred ${it.key} vs ${it.value.rawValue}")
                    fieldDataMap.put(it.key, it.value)
                }
                fieldDataMapList.add(fieldDataMap)
                topFieldsItem.add(topFieldData as List<TopFieldsItem>)
                parentItem.add(ParentItem(it?.slug!!,"asd", topFieldsItem, fieldDataMapList))
            }
            adapterParent.setItemList(parentItem)
        })

        formVm.failure.observe(this, {
            formVm.hideLoading()
            checkFailureStatus(it)
        })

        formVm.isLoading.observe(this, {
            if (it) loading.visibility = View.VISIBLE else loading.visibility = View.GONE
        })

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_resultFragment_to_mainFragment)
            }

        })

    }
}

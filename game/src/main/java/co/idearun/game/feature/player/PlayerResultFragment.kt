package co.idearun.game.feature.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.ArrayMap
import androidx.fragment.app.activityViewModels
import co.idearun.data.model.FieldData
import co.idearun.data.model.TopFieldsItem
import co.idearun.data.model.fieldList
import co.idearun.game.base.BaseFragment
import co.idearun.game.model.ParentItem
import co.idearun.game.adapter.ParentItemAdapter
import co.idearun.game.R
import co.idearun.game.adapter.ChildItemAdapter
import co.idearun.game.adapter.ChildItemPlayerAdapter
import co.idearun.game.adapter.ParentItemPlayerAdapter
import co.idearun.game.model.ParentItemPlayer
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_result.*
import timber.log.Timber

class PlayerResultFragment : BaseFragment() {

    lateinit var adapterParent: ParentItemPlayerAdapter
    lateinit var adapterChield: ChildItemPlayerAdapter

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




        val formVm: FormViewModel by activityViewModels()
        val slug = arguments?.getString("slug")
        val liveDashboardAddress = formVm.userForm.value?.form?.liveDashboardAddress

        Timber.i("Live Dashboard $liveDashboardAddress")





        formVm.getLiveSubmits(formVm.userForm.value?.form?.liveDashboardAddress!!)
        formVm.getSubmitsRow(formVm.userForm.value?.form?.liveDashboardAddress!!)


        adapterParent = ParentItemPlayerAdapter(requireContext())
        parentRecyclerView.adapter = adapterParent
        adapterChield = ChildItemPlayerAdapter(requireContext())

        formVm.liveSubmits.observe(this, {

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
                    parentItem.add(ParentItem("asd", topFieldsItem, fieldDataMapList))
                }
                adapterParent.setItemList(parentItem)
            })
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

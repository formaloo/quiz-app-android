package co.idearun.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.idearun.common.TokenContainer
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_result.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class ResultFragment : BaseFragment() {

    lateinit var adapter: ResultAdapter

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

        adapter = ResultAdapter()
        rvFields.adapter = adapter

        formVm.getSubmitsRow(slug!!)


        formVm.submits.observe(this, {
            Timber.i("field size " + it?.data?.rows?.size)
            adapter.submitList(it?.data?.topFields)

            it.data?.rows?.get(0)?.renderedData?.entries?.forEach {
                Timber.i("rendred ${it.key} vs ${it.value.rawValue}")
                adapter.fieldDataMap.put(it.key, it.value)

            }

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

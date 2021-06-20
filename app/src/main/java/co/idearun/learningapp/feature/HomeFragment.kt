package co.idearun.learningapp.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import co.idearun.learningapp.common.base.BaseFragment
import co.idearun.learningapp.common.base.BaseViewModel
import co.idearun.learningapp.common.exception.Failure
import co.idearun.learningapp.databinding.FragmentHomeBinding
import co.idearun.learningapp.feature.adapter.FormListAdapter
import co.idearun.learningapp.feature.viewmodel.FormViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import timber.log.Timber

class HomeFragment : BaseFragment(), KoinComponent {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var formListAdapter: FormListAdapter
    private val viewModel: FormViewModel by viewModel()


    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()


    }

    private fun initData() {
        fetchFormList(true)

        viewModel.failure.observe(viewLifecycleOwner, {
            when (it) {
                is Failure.FeatureFailure -> renderFailure(it.msgRes)

                else -> {

                }
            }
        })

        viewModel.form.observe(viewLifecycleOwner, {
            it?.let {
                Timber.e("forms $it")
            }

        })

    }

    private fun initView() {
        formListAdapter = FormListAdapter()

        binding.lessonRv.apply {
            adapter = formListAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

    }


    private fun fetchFormList(force: Boolean) {
        lifecycleScope.launch {
            viewModel.fetchFormList(force).collectLatest { pagingData ->
                formListAdapter.submitData(pagingData)

            }
        }

    }


    private fun renderFailure(message: String?) {
        Timber.e("renderFailure $message")
        fetchFormList(true)

    }


}


package co.idearun.learningapp.feature

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import co.idearun.learningapp.common.base.BaseFragment
import co.idearun.learningapp.common.base.BaseViewModel
import co.idearun.learningapp.common.exception.Failure
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.FragmentHomeBinding
import co.idearun.learningapp.feature.adapter.FormListAdapter
import co.idearun.learningapp.feature.adapter.FormListListener
import co.idearun.learningapp.feature.flashCard.FlashCardActivity
import co.idearun.learningapp.feature.viewmodel.FormViewModel
import co.idearun.learningapp.feature.viewmodel.SharedViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import timber.log.Timber

class HomeFragment : BaseFragment(), KoinComponent, FormListListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var formListAdapter: FormListAdapter
    private val viewModel: FormViewModel by viewModel()
    private val shardedVM: SharedViewModel by viewModel()


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

        getLastFormData()

        viewModel.failure.observe(viewLifecycleOwner, {
            when (it) {
                is Failure.FeatureFailure -> renderFailure(it.msgRes)

                else -> {

                }
            }
        })

        viewModel.form.observe(viewLifecycleOwner, {
            it?.let {
                binding.lessonInprogress.progress = shardedVM.retrieveLessonProgress()[it.slug]
                binding.lessonInprogress.item = it
                binding.lessonInprogress.listener = this
                binding.executePendingBindings()
            }

        })


    }

    private fun getLastFormData() {
        val lastLesson = shardedVM.getLastLesson()
        if (lastLesson?.isNotEmpty()==true){
            viewModel.initFormSlug(lastLesson)
            viewModel.retrieveFormFromDB()

        }else{
            binding.lessonInprogress.progress =0
            binding.executePendingBindings()
        }


    }

    private fun initView() {
        formListAdapter = FormListAdapter(shardedVM.retrieveLessonProgress(), this)

        binding.lessonRv.apply {
            adapter = formListAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

    }

    override fun openForm(form: Form?, formItemLay: View) {
        val intent = Intent(requireActivity(), FlashCardActivity::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            formItemLay,
            ViewCompat.getTransitionName(formItemLay)!!
        )
        intent.putExtra("form", form)
        startActivity(intent, options.toBundle())

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

    override fun onResume() {
        getLastFormData()
        formListAdapter.resetProgress(shardedVM.retrieveLessonProgress())
        formListAdapter.notifyDataSetChanged()
        super.onResume()

    }
}


package co.idearun.learningapp.feature.flashCard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.R
import co.idearun.learningapp.common.extension.visible
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.ActivityFlashCardBinding
import co.idearun.learningapp.feature.viewmodel.SharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class FlashCardActivity : FlashCardBaseActivity(), FlashcardListener {

    private var formsProgressMap = hashMapOf<String?, Int?>()
    private var form: Form? = null
    private var fieldsFlashAdapter: FieldsFlashAdapter? = null
    private var lastFieldToCheck: Fields? = null
    private var fields: ArrayList<Fields> = arrayListOf()
    private lateinit var binding: ActivityFlashCardBinding
    val shardedVM: SharedViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, R.layout.activity_flash_card)
        binding.listener = this
        binding.flashcardListener = this
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this



        baseMethod.hideAB(supportActionBar)

        checkBundle()
        initView()
        initData()
    }


    private fun checkBundle() {
        intent.extras?.let {
            it.getSerializable("form")?.let {
                if (it is Form) {
                    binding.form = it
                    it.fields_list?.let {
                        this.fields = it
                    }

                    binding.executePendingBindings()
                    form = it

                } else {

                }
            }
        }
    }

    private fun initView() {

        updateTheme(form)

        binding.flashcardFieldsRec.apply {
            fieldsFlashAdapter = FieldsFlashAdapter(
                this@FlashCardActivity,
                object : SwipeStackListener {
                    override fun onSwipeEnd(position: Int) {
                        next()
                    }
                }, this@FlashCardActivity, form!!, viewModel
            )

            adapter = fieldsFlashAdapter
            layoutManager =
                object : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }

                    override fun canScrollHorizontally(): Boolean {
                        return false
                    }
                }
        }

        checkLessonProgress()

    }

    private fun initData() {
        shardedVM.saveLastForm(form?.slug ?: "")
        viewModel.initFormSlug(form?.slug ?: "")
        viewModel.getSubmitEntity()
    }


    override fun next() {
        with(binding.flashcardFieldsRec) {
            val visibleItemPosition =
                (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

            updateFormProgress(visibleItemPosition)

            if (fields.size > visibleItemPosition + 1) {
                Handler(Looper.getMainLooper()).postDelayed({
                    scrollToPosition(visibleItemPosition + 1)
                }, 150)

            } else {
                openCongView()
            }

            binding.progress = visibleItemPosition + 1

        }

    }

    override fun pre() {
        with(binding.flashcardFieldsRec) {
            var visibleItemPosition =
                (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

            updateFormProgress(visibleItemPosition)

            if (0 <= visibleItemPosition - 1) {
                scrollToPosition(visibleItemPosition - 1)

            }

            binding.progress = visibleItemPosition
        }
    }

    override fun closePage() {
        onBackPressed()

    }

    override fun share() {
        val address = getString(R.string.DiSPLAY_FORM) + form?.address
        baseMethod.shareVia(address, getString(R.string.share_form_link_via), this)

    }

    private fun openCongView() {
        updateFormProgress(0)
        callWorker()
        binding.flashCongView.visible()

    }

    private fun updateFormProgress(pos: Int) {
        formsProgressMap[form?.slug] = pos
        shardedVM.saveFormProgress(formsProgressMap)

    }

    private fun checkLessonProgress() {
        formsProgressMap = shardedVM.retrieveFormProgress()

        val formSlug = form?.slug ?: ""
        val progress = formsProgressMap[form?.slug ?: ""]

        if (progress == null) {
            formsProgressMap[formSlug] = 0
            shardedVM.saveFormProgress(formsProgressMap)
        } else {
            binding.flashcardFieldsRec.scrollToPosition(progress + 1)
        }
        binding.progress = (progress ?: 0) + 1
        binding.executePendingBindings()

    }

}
package co.idearun.learningapp.feature.lesson

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.R
import co.idearun.learningapp.common.Constants
import co.idearun.learningapp.common.extension.invisible
import co.idearun.learningapp.common.extension.visible
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.ActivityLessonBinding
import co.idearun.learningapp.feature.lesson.adapter.LessonFieldsAdapter
import co.idearun.learningapp.feature.lesson.listener.LessonListener
import co.idearun.learningapp.feature.lesson.listener.SwipeStackListener
import co.idearun.learningapp.feature.viewmodel.SharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class LessonActivity : LessonBaseActivity(), LessonListener {

    private lateinit var binding: ActivityLessonBinding
    private val shardedVM: SharedViewModel by viewModel()
    private var fieldsFlashAdapter: LessonFieldsAdapter? = null
    private var form: Form? = null
    private var lessonsProgressMap = hashMapOf<String?, Int?>()
    private var fields: ArrayList<Fields> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, R.layout.activity_lesson)
        binding.listener = this
        binding.flashcardListener = this
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        binding.layoutFashCong.flashcardListener = this
        binding.layoutFashCong.listener = this
        binding.layoutFashCong.viewmodel = viewModel

        baseMethod.hideAB(supportActionBar)

        checkBundle()
        initData()
        initView()


    }

    private fun checkBundle() {
        intent.extras?.let {
            val progress = it.getInt("progress") ?: 0
            it.getSerializable("form")?.let {
                if (it is Form) {

                    it.fields_list?.let {
                        this.fields = it
                    }
                    form = it


                } else {

                }
            }
        }
    }

    private fun initView() {
        fieldsFlashAdapter = LessonFieldsAdapter(
            this@LessonActivity, this,
            object : SwipeStackListener {
                override fun onSwipeEnd(position: Int) {
                    next()
                }
            },
            form!!, viewModel,
        )

        binding.flashcardFieldsRec.apply {
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





    }

    private fun initData() {
        formVM.initLessonSlug(form?.slug ?: "")
        formVM.initLessonAddress(form?.address ?: "")
        formVM.getFormData()

        shardedVM.saveLastLesson(form?.slug ?: "")

        viewModel.initFormSlug(form?.slug ?: "")
        viewModel.getSubmitEntity()

        formVM.form.observe(this, {
            it?.let {
                form = it
                it.fields_list?.let {
                    this.fields = it
                }
                checkLessonProgress()
                updateTheme(form)

                binding.form = form
                binding.layoutFashCong.form = form
                binding.executePendingBindings()

            }

        })

    }


    override fun next() {
        with(binding.flashcardFieldsRec) {
            val visibleItemPosition =
                (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

            viewModel.saveEditSubmitToDB(false, visibleItemPosition)

            val next = visibleItemPosition + 1
            updateLessonProgress(next)

            if (fields.size > next) {
                Handler(Looper.getMainLooper()).postDelayed({
                    scrollToPosition(next)
                }, Constants.SCROLL_DELAY)

                checkNeedActionFields(next)

            } else {
                openCongView()
            }

            binding.flashPreBtn.visible()

            binding.progress = next


            Timber.d("visibleItemPosition$visibleItemPosition")
        }

    }

    private fun checkNeedActionFields(pos: Int): Boolean {
        val type = fieldsFlashAdapter?.collection?.get(pos)?.type

        if (type == Constants.SINGLE_SELECT || type == Constants.DROPDOWN || type == Constants.YESNO || type == Constants.RATING || type == Constants.META) {
            binding.flashcardDoneBtn.invisible()

        } else {
            binding.flashcardDoneBtn.visible()

        }

        return false
    }

    override fun pre() {
        with(binding.flashcardFieldsRec) {
            val visibleItemPosition =
                (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()


            updateLessonProgress(visibleItemPosition - 1)

            if (0 <= visibleItemPosition - 1) {
                scrollToPosition(visibleItemPosition - 1)

            }

            if (visibleItemPosition == 1) {
                binding.flashPreBtn.invisible()
            }

            binding.flashNextBtn.visible()
            binding.progress = visibleItemPosition
        }
    }

    override fun closePage() {
        onBackPressed()

    }

    override fun share() {
        val shareTxt =
            "I'm learning ${form?.title ?: ""} in Learning App. Check it out on your phone: ${
                getString(R.string.appAddress)
            }"
        baseMethod.shareVia(shareTxt, getString(R.string.app_name), this)

    }

    private fun openCongView() {
        updateLessonProgress(-1)
        shardedVM.saveLastLesson("")
        val visibleItemPosition =
            (binding.flashcardFieldsRec.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        Timber.d("visibleItemPosition$visibleItemPosition")
        viewModel.saveEditSubmitToDB(true, visibleItemPosition)
        callWorker()
        binding.flashCongView.visible()

    }

    private fun updateLessonProgress(pos: Int) {
        lessonsProgressMap[form?.slug] = pos
        shardedVM.saveLessonProgress(lessonsProgressMap)

    }

    private fun checkLessonProgress() {
        lessonsProgressMap = shardedVM.retrieveLessonProgress()

        val formSlug = form?.slug ?: ""
        val progress = lessonsProgressMap[form?.slug ?: ""]

        if (progress == null || progress == 0 || progress == -1) {
            lessonsProgressMap[formSlug] = 0
            shardedVM.saveLessonProgress(lessonsProgressMap)
        } else {
            binding.flashPreBtn.visible()
            binding.flashcardFieldsRec.scrollToPosition(progress + 1)
        }

        if (progress ?: 0 == 0) {
            binding.flashPreBtn.invisible()

        }

        binding.progress = (progress ?: 0)
        binding.executePendingBindings()

    }

    override fun onPause() {
        super.onPause()

    }

}
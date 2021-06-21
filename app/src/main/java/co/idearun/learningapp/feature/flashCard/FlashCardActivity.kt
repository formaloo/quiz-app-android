package co.idearun.learningapp.feature.flashCard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.R
import co.idearun.learningapp.common.Constants
import co.idearun.learningapp.common.Constants.SECTION
import co.idearun.learningapp.common.extension.invisible
import co.idearun.learningapp.common.extension.visible
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.ActivityFlashCardBinding
import timber.log.Timber

class FlashCardActivity : FlashCardBaseActivity(), FlashcardListener {

    private var form: Form? = null
    private var fieldsFlashAdapter: FieldsFlashAdapter? = null
    private var lastFieldToCheck: Fields? = null
    private var fields: ArrayList<Fields> = arrayListOf()
    private lateinit var binding: ActivityFlashCardBinding

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
                    binding.executePendingBindings()
                    form = it

                } else {

                }
            }
        }
    }

    private fun initView() {
        updateTheme(form)
        form?.fields_list?.let { fields ->
            this.fields = fields

            fieldsFlashAdapter = FieldsFlashAdapter(
                this,
                object : SwipeStackListener {
                    override fun onSwipeEnd(position: Int) {
                        next()
                    }

                }, this, form!!, viewModel
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
            fieldsFlashAdapter?.collection = fields

        }

    }

    private fun initData() {
        viewModel.initFormSlug(form?.slug ?: "")
        viewModel.getSubmitEntity()
        viewModel.getSubmitEntityList()
        viewModel.submitEntity.observe(this, {
            it?.let {
                Timber.e("submitEntity ${it.newRow} ${it.progressNumber}")
                if (it.newRow == false && it.progressNumber != null)
                    binding.flashcardFieldsRec.scrollToPosition(it.progressNumber!! + 1)

            }
        })


    }


    override fun closePage() {
        onBackPressed()

    }

    override fun share() {

    }

    override fun next() {
        with(binding.flashcardFieldsRec) {
            val visibleItemPosition =
                (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

            if (fields.size > visibleItemPosition + 1) {
                val newRow = visibleItemPosition == fields.size - 1
                Timber.e("newRow $newRow,$visibleItemPosition")
                viewModel.saveEditSubmitToDB(newRow, visibleItemPosition)

                Handler(Looper.getMainLooper()).postDelayed({
                    scrollToPosition(visibleItemPosition + 1)
                    binding.flashPreBtn.visible()
                    if (fields.size <= visibleItemPosition + 2) {
                        binding.flashNextBtn.invisible()
                    } else {
                        binding.flashNextBtn.visible()

                    }
                }, 300)

            } else {
                openCongView()
            }
        }

    }

    private fun openCongView() {
        binding.flashCongView.visible()

    }

    override fun pre() {
        with(binding.flashcardFieldsRec) {
            var visibleItemPosition =
                (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

            if (0 <= visibleItemPosition - 1) {
                scrollToPosition(visibleItemPosition - 1)
                binding.flashNextBtn.visible()
                if (0 > visibleItemPosition - 2) {
                    binding.flashPreBtn.invisible()
                } else {
                    binding.flashPreBtn.visible()
                }
            }
        }
    }

    override fun checkField(field: Fields, pos: Int) {
        fieldsFlashAdapter?.let { adapter ->
            lastFieldToCheck = if (pos > 0) {
                adapter.collection[pos - 1]
            } else {
                null
            }

        }

        //checkSkipBtn
        val type = if (field.sub_type != null) {
            field.sub_type
        } else {
            field.type
        }

        if (
            type == Constants.DROPDOWN ||
            type == Constants.YESNO ||
            type == Constants.SINGLE_SELECT ||
            type == Constants.Like_Dislike ||
            type == Constants.star ||
            type == Constants.embeded ||
            type == Constants.nps ||
            type == Constants.SECTION
        ) {
            binding.flashcardSkipBtn.invisible()

        } else {
            binding.flashcardSkipBtn.visible()

        }


        //checkPreBtn
        if (field.sub_type == SECTION && lastFieldToCheck?.sub_type == SECTION) {
            binding.flashPreBtn.visible()

        } else {
            binding.flashPreBtn.invisible()

        }


        //checkNextBtn
        if (field.sub_type == SECTION) {
            binding.flashNextBtn.visible()

        } else {
            binding.flashNextBtn.invisible()

        }

    }

}
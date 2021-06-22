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
import co.idearun.learningapp.feature.viewmodel.SharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

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
                    binding.executePendingBindings()
                    form = it

                } else {

                }
            }
        }
    }

    private fun initView() {
        formsProgressMap = shardedVM.retrieveFormProgress()
        val formSlug = form?.slug ?: ""
        val progress = formsProgressMap[form?.slug ?: ""]

        if (progress == null) {
            formsProgressMap[formSlug] = 0
            shardedVM.saveFormProgress(formsProgressMap)
        } else {
            binding.flashcardFieldsRec.scrollToPosition(progress + 1)

        }

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


    }


    override fun closePage() {
        onBackPressed()

    }

    override fun share() {
        val address = getString(R.string.DiSPLAY_FORM) + form?.address
        baseMethod.shareVia(address, getString(R.string.share_form_link_via), this)

    }

    override fun next() {
        with(binding.flashcardFieldsRec) {
            val visibleItemPosition =
                (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            updateNextData(visibleItemPosition, fields)

            updateNextView(fields, visibleItemPosition, this)
        }

    }

    private fun updateNextData(visibleItemPosition: Int, fields: java.util.ArrayList<Fields>) {
        val newRow = visibleItemPosition == fields.size - 1
        viewModel.saveEditSubmitToDB(newRow, visibleItemPosition)
        if (newRow) {
            formsProgressMap.remove(form?.slug)

        } else {
            formsProgressMap[form?.slug] = visibleItemPosition
            shardedVM.saveFormProgress(formsProgressMap)
        }
    }

    private fun updateNextView(
        fields: java.util.ArrayList<Fields>,
        visibleItemPosition: Int,
        recyclerView: RecyclerView
    ) {
        if (fields.size > visibleItemPosition + 1) {
            Handler(Looper.getMainLooper()).postDelayed({
                recyclerView.scrollToPosition(visibleItemPosition + 1)

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

    private fun openCongView() {
        callWorker()
        binding.flashCongView.visible()

    }

    override fun pre() {
        with(binding.flashcardFieldsRec) {
            var visibleItemPosition =
                (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

            updatePreView(visibleItemPosition)
            updatepreView(visibleItemPosition, this)
        }
    }

    private fun updatepreView(visibleItemPosition: Int, recyclerView: RecyclerView) {
        if (0 <= visibleItemPosition - 1) {
            recyclerView.scrollToPosition(visibleItemPosition - 1)
            binding.flashNextBtn.visible()
            if (0 > visibleItemPosition - 2) {
                binding.flashPreBtn.invisible()
            } else {
                binding.flashPreBtn.visible()
            }
        }
    }

    private fun updatePreView(visibleItemPosition: Int) {
        formsProgressMap[form?.slug] = visibleItemPosition
        shardedVM.saveFormProgress(formsProgressMap)

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
            if (field.required == true) {
                binding.flashcardSkipBtn.invisible()
                binding.flashcardDoneBtn.visible()

            } else {
                binding.flashcardSkipBtn.visible()
                binding.flashcardDoneBtn.invisible()

            }

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
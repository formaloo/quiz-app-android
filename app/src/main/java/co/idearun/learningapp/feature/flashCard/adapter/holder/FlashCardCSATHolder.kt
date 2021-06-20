package co.idearun.learningapp.feature.flashCard.adapter.holder

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.idearun.learningapp.common.extension.invisible
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.LayoutUiCsatItemBinding
import co.idearun.learningapp.feature.flashCard.FlashcardListener
import co.idearun.learningapp.feature.flashCard.ViewsListener
import co.idearun.learningapp.feature.flashCard.adapter.*
import co.idearun.learningapp.feature.viewmodel.UIViewModel

class FlashCardCSATHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val binding = LayoutUiCsatItemBinding.bind(itemView)

    fun bindItems(
        field: Fields,
        position_: Int,
        listener: ViewsListener,
        form: Form,
        viewmodel: UIViewModel,
        flashcardListener: FlashcardListener
    ) {
        binding.field = field
        binding.form = form
        binding.fieldUiHeader.field = field
        binding.fieldUiHeader.form = form
        binding.listener = listener
        binding.viewmodel = viewmodel
        val context = binding.starlay.context
        binding.lifecycleOwner = context as LifecycleOwner
        binding.fieldUiFooter.field = field
        binding.fieldUiFooter.viewmodel = viewmodel

        var selectedItem: Int? = null


        setUpCSATIconList(field, viewmodel, selectedItem, flashcardListener)

        if (field.required == true) {
            viewmodel.reuiredField(field)

        } else {

        }

    }

    private fun setUpCSATIconList(
        field: Fields,
        viewmodel: UIViewModel,
        selectedItem: Int?,
        flashcardListener: FlashcardListener
    ) {
        val list = when (field.thumbnail_type) {
            CSATThumbnailType.flat_face.name -> {

                arrayListOf(
                    CSATFlat.ANGRY,
                    CSATFlat.SAD,
                    CSATFlat.NEUTRAL,
                    CSATFlat.SMILE,
                    CSATFlat.LOVE
                )
            }
            CSATThumbnailType.funny_face.name -> {
                arrayListOf(
                    CSATFunny.ANGRY,
                    CSATFunny.SAD,
                    CSATFunny.NEUTRAL,
                    CSATFunny.SMILE,
                    CSATFunny.LOVE
                )

            }
            CSATThumbnailType.outlined.name -> {
                arrayListOf(
                    CSATOutline.ANGRY,
                    CSATOutline.SAD,
                    CSATOutline.NEUTRAL,
                    CSATOutline.SMILE,
                    CSATOutline.LOVE
                )
            }
            CSATThumbnailType.heart.name -> {
                arrayListOf(
                    CSATHEART.ANGRY,
                    CSATHEART.SAD,
                    CSATHEART.NEUTRAL,
                    CSATHEART.SMILE,
                    CSATHEART.LOVE
                )

            }
            CSATThumbnailType.monster.name -> {
                arrayListOf(
                    CSATMonster.ANGRY,
                    CSATMonster.SAD,
                    CSATMonster.NEUTRAL,
                    CSATMonster.SMILE,
                    CSATMonster.LOVE
                )
            }
            CSATThumbnailType.star.name -> {
                arrayListOf(
                    CSATSTAR.ANGRY,
                    CSATSTAR.SAD,
                    CSATSTAR.NEUTRAL,
                    CSATSTAR.SMILE,
                    CSATSTAR.LOVE
                )
            }

            else -> {
                binding.csatIconRes.invisible()
                arrayListOf()

            }
        }

        binding.csatIconRes.apply {
            layoutManager = StaggeredGridLayoutManager(5, RecyclerView.VERTICAL)
            adapter = FlashCardCSATAdapter(list, (selectedItem ?: 0) - 1, object : CSATListener {
                override fun csatSelected(pos: Int) {
                    viewmodel.addKeyValueToReq(field.slug!!, pos)
                    hideErr(binding, viewmodel)
                    flashcardListener.next()
                }

            })
        }


    }

    private fun hideErr(binding: LayoutUiCsatItemBinding, viewmodel: UIViewModel) {
        binding.errLay.invisible()
        viewmodel.setErrorToField(Fields(), "")

    }


}
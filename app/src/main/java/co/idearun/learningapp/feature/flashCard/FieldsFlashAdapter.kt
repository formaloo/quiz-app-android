package co.idearun.learningapp.feature.flashCard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import co.idearun.form.R
import co.idearun.form.view.form.flashCard.holder.*
import co.idearun.form.view.form.formUI.UIViewModel
import co.idearun.form.view.form.formUI.ViewsListener
import co.idearun.formCommon.FormConstants
import co.idearun.formCommon.FormConstants.DATE
import co.idearun.formCommon.FormConstants.DROPDOWN
import co.idearun.formCommon.FormConstants.FILE
import co.idearun.formCommon.FormConstants.Like_Dislike
import co.idearun.formCommon.FormConstants.MATRIX
import co.idearun.formCommon.FormConstants.MULTI_SELECT
import co.idearun.formCommon.FormConstants.PHONE_VERIFICATION
import co.idearun.formCommon.FormConstants.SECTION
import co.idearun.formCommon.FormConstants.SIGNATURE
import co.idearun.formCommon.FormConstants.SINGLE_SELECT
import co.idearun.formCommon.FormConstants.TIME
import co.idearun.formCommon.FormConstants.YESNO
import co.idearun.formCommon.FormConstants.nps
import co.idearun.formCommon.FormConstants.star
import co.idearun.formModel.form.Fields
import co.idearun.formModel.form.Form
import co.idearun.formModel.submit.RenderedData
import co.idearun.learningapp.data.model.form.Fields
import java.util.*


class FieldsFlashAdapter(
    private val listener: ViewsListener,
    private val swipeStackListener: SwipeStackListener,
    private val flashcardListener: FlashcardListener,
    private val form: Form,
    private val viewmodel: UIViewModel,
    private val rowRenderedData: Map<String, RenderedData>?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var lastPosition = RecyclerView.NO_POSITION
    private val TYPE_DROP_DOWN = 0
    private val TYPE_MULTI = 1
    private val TYPE_SIGNLE = 2
    private val TYPE_EDT = 3
    private val TYPE_LIKE_DISLIKE = 4
    private val TYPE_STAR = 5
    private val TYPE_NPS = 6
    private val TYPE_FILE = 7
    private val TYPE_SECTION = 8
    private val TYPE_TIME = 9
    private val TYPE_DATE = 10
    private val TYPE_MATRIX = 11
    private val TYPE_PHONE_VERIFICATION = 12
    private val TYPE_SIGNATURE = 13
    private val TYPE_CSAT = 14


    internal var collection = arrayListOf<Fields>()

    fun setCollection(items: ArrayList<Fields>) {
        collection = items
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getHolder(viewType, parent)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position_: Int) {

        val btnItem = collection[position_]
        val context = holder.itemView.context
        val itemViewType = getItemViewType(position_)

        when (itemViewType) {
            TYPE_DROP_DOWN -> {
                (holder as FlashCardDropDownHolder).bindItems(
                    btnItem,
                    position_,
                    listener,
                    flashcardListener,
                    form, viewmodel

                )

            }
            TYPE_MATRIX -> {
                (holder as FlashCardMatrixHolder).bindItems(
                    btnItem,
                    position_,
                    listener,
                    form,
                    flashcardListener,
                    viewmodel
                )

            }
            TYPE_MULTI -> {
                (holder as FlashCardMultiHolder).bindItems(
                    btnItem, position_,
                    listener, form, flashcardListener, viewmodel
                )

            }
            TYPE_SIGNLE -> {
                (holder as FlashCardSingleHolder).bindItems(
                    btnItem,
                    position_,
                    listener,
                    form,
                    flashcardListener,
                    viewmodel
                )

            }
            TYPE_EDT -> {
                (holder as FlashCardTextHolder).bindItems(
                    btnItem,
                    position_,
                    listener,
                    form,
                    flashcardListener,
                    viewmodel
                )

            }
            TYPE_LIKE_DISLIKE -> {
                (holder as FlashCardLikeDislikeHolder).bindItems(
                    btnItem,
                    position_,
                    listener,
                    form,
                    flashcardListener,
                    viewmodel
                )

            }
            TYPE_STAR -> {
                (holder as FlashCardStarHolder).bindItems(
                    btnItem,
                    position_,
                    listener,
                    form,
                    flashcardListener,
                    viewmodel
                )

            }
            TYPE_CSAT -> {
                (holder as FlashCardCSATHolder).bindItems(
                    btnItem,
                    position_,
                    listener,
                    form,
                    viewmodel,
                    flashcardListener
                )

            }
            TYPE_NPS -> {
                (holder as FlashCardNPSHolder).bindItems(
                    btnItem,
                    position_,
                    listener,
                    form,
                    flashcardListener,
                    viewmodel
                )

            }
            TYPE_FILE -> {
                (holder as FlashCardFileHolder).bindItems(
                    btnItem,
                    position_,
                    listener,
                    form,
                    viewmodel
                )

            }
            TYPE_SECTION -> {
                (holder as FlashCardSectionHolder).bindItems(
                    btnItem,
                    position_,
                    listener,
                    form,
                    flashcardListener,
                    viewmodel
                )

            }
            TYPE_TIME -> {
                (holder as FlashCardTimeHolder).bindItems(
                    btnItem,
                    position_,
                    listener,
                    form,
                    flashcardListener,
                    viewmodel
                )

            }
            TYPE_DATE -> {
                (holder as FlashCardDateHolder).bindItems(
                    btnItem,
                    position_,
                    listener,
                    form,
                    flashcardListener,
                    viewmodel
                )

            }
            TYPE_PHONE_VERIFICATION -> {
                (holder as FlashCardTextHolder).bindItems(
                    btnItem,
                    position_,
                    listener,
                    form,
                    flashcardListener,
                    viewmodel
                )
            }
            TYPE_SIGNATURE -> {
                (holder as FlashCardTextHolder).bindItems(
                    btnItem,
                    position_,
                    listener,
                    form,
                    flashcardListener,
                    viewmodel
                )
            }
            else -> {
                (holder as FlashCardTextHolder).bindItems(
                    btnItem,
                    position_,
                    listener,
                    form,
                    flashcardListener,
                    viewmodel
                )

            }
        }

        holder.setIsRecyclable(false)

        val animation: Animation = AnimationUtils.loadAnimation(context, getAnimationId(position_,itemViewType))
        holder.itemView.startAnimation(animation)
        lastPosition = position_

    }

    private fun getAnimationId(position_: Int, itemViewType: Int): Int {
       return when (itemViewType) {
            TYPE_DROP_DOWN -> {
                if (position_ > lastPosition) R.anim.slide_in_bottom else R.anim.slide_in_top


            }
            TYPE_MATRIX -> {
                if (position_ > lastPosition) R.anim.slide_in_bottom else R.anim.slide_in_top

            }
            TYPE_MULTI -> {
                if (position_ > lastPosition) R.anim.slide_in_bottom else R.anim.slide_in_top

            }
            TYPE_SIGNLE -> {
                if (position_ > lastPosition) R.anim.slide_in_bottom else R.anim.slide_in_top


            }
            TYPE_EDT -> {
                if (position_ > lastPosition) R.anim.slide_in_bottom else R.anim.slide_in_top

            }
            TYPE_LIKE_DISLIKE -> {
                if (position_ > lastPosition) R.anim.slide_in_bottom else R.anim.slide_in_top


            }
            TYPE_STAR -> {
                if (position_ > lastPosition) R.anim.slide_in_bottom else R.anim.slide_in_top


            }
            TYPE_CSAT -> {
                if (position_ > lastPosition) R.anim.slide_in_bottom else R.anim.slide_in_top


            }
            TYPE_NPS -> {
                if (position_ > lastPosition) R.anim.slide_in_bottom else R.anim.slide_in_top


            }
            TYPE_FILE -> {
                if (position_ > lastPosition) R.anim.slide_in_bottom else R.anim.slide_in_top


            }
            TYPE_SECTION -> {
                if (position_ > lastPosition) R.anim.slide_in_bottom else R.anim.slide_in_top


            }
            TYPE_TIME -> {
                if (position_ > lastPosition) R.anim.slide_in_bottom else R.anim.slide_in_top


            }
            TYPE_DATE -> {
                if (position_ > lastPosition) R.anim.slide_in_bottom else R.anim.slide_in_top


            }
            TYPE_PHONE_VERIFICATION -> {
                if (position_ > lastPosition) R.anim.slide_in_bottom else R.anim.slide_in_top

            }
            TYPE_SIGNATURE -> {
                if (position_ > lastPosition) R.anim.slide_in_bottom else R.anim.slide_in_top

            }
            else -> {
                if (position_ > lastPosition) R.anim.slide_in_bottom else R.anim.slide_in_top


            }
        }


    }

    override fun getItemViewType(position: Int): Int {
        val fields = collection[position]
        val type = if (fields.sub_type != null) {
            fields.sub_type
        } else {
            fields.type
        }
        return when (type) {
            DROPDOWN -> {
                TYPE_DROP_DOWN
            }
            YESNO -> {
                TYPE_SIGNLE
            }
            MULTI_SELECT -> {
                TYPE_MULTI
            }
            SINGLE_SELECT -> {
                TYPE_SIGNLE
            }
            Like_Dislike -> {
                TYPE_LIKE_DISLIKE

            }
            FormConstants.embeded -> {
                TYPE_CSAT

            }
            star -> {
                TYPE_STAR

            }
            nps -> {
                TYPE_NPS

            }
            FILE -> {
                TYPE_FILE

            }
            SECTION -> {
                TYPE_SECTION

            }
            TIME -> {
                TYPE_TIME

            }
            DATE -> {
                TYPE_DATE

            }
            MATRIX -> {
                TYPE_MATRIX

            }
            PHONE_VERIFICATION -> {
                TYPE_PHONE_VERIFICATION

            }
            SIGNATURE -> {
                TYPE_SIGNATURE

            }
            else -> {
                TYPE_EDT
            }
        }

    }

    override fun getItemCount(): Int {
        return collection.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }


    private fun getHolder(viewType: Int, parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView: View
        return when (viewType) {
            TYPE_DROP_DOWN -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_flash_card_dropdown_item, parent, false);
                FlashCardDropDownHolder(itemView)
            }
            TYPE_MULTI -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_flash_card_multi_item, parent, false);
                FlashCardMultiHolder(itemView)
            }
            TYPE_SIGNLE -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_flash_card_signle_item, parent, false);
                FlashCardSingleHolder(itemView)
            }
            TYPE_EDT -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_flash_card_edt_item, parent, false);
                FlashCardTextHolder(itemView)
            }
            TYPE_LIKE_DISLIKE -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_flash_card_like_dislike_item, parent, false);
                FlashCardLikeDislikeHolder(itemView)
            }
            TYPE_STAR -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_flash_card_star_item, parent, false);
                FlashCardStarHolder(itemView)
            }
//            TYPE_CSAT -> {
//                itemView = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.layout_flash_card_csat_item, parent, false);
//                return CSATViewHolder(itemView)
//            }
            TYPE_NPS -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_flash_card_nps_item, parent, false);
                FlashCardNPSHolder(itemView)
            }
            TYPE_FILE -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_flash_card_file_item, parent, false);
                FlashCardFileHolder(itemView)
            }
            TYPE_SECTION -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_flash_card_section_item, parent, false);
                FlashCardSectionHolder(itemView, swipeStackListener)
            }
            TYPE_TIME -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_flash_card_time_item, parent, false);
                FlashCardTimeHolder(itemView)
            }
            TYPE_DATE -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_flash_card_date_item, parent, false);
                FlashCardDateHolder(itemView)
            }
            TYPE_MATRIX -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_flash_card_matrix_item, parent, false);
                FlashCardMatrixHolder(itemView)
            }
//            TYPE_PHONE_VERIFICATION -> {
//                itemView = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.layout_flash_card_phone_verify_item, parent, false);
//                return PhoneVerifyViewHolder(itemView)
//            }
//            TYPE_SIGNATURE -> {
//                itemView = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.layout_flash_card_signature_item, parent, false);
//                return SignatureViewHolder(itemView)
//            }
            else -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_flash_card_edt_item, parent, false);
                FlashCardTextHolder(itemView)

            }
        }

    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }
}



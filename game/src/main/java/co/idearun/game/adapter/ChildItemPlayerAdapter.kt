package co.idearun.game.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.RecyclerView
import co.idearun.data.model.FieldData
import co.idearun.data.model.TopFieldsItem
import com.google.android.material.textfield.TextInputEditText
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.widget.TextView
import co.idearun.game.R
import timber.log.Timber


class ChildItemPlayerAdapter(
    var context: Context
) :
    RecyclerView.Adapter<ChildItemPlayerAdapter.ChildViewHolder>() {

    companion object {
        const val VIEW_TYPE_TEXT_FIELD = 0
        const val VIEW_TYPE_DROP_DOWN = 1
        const val VIEW_TYPE_TEXT_FIELD_NAME = 2
    }

    var contexta: Context? = null
    var itemList = arrayListOf<TopFieldsItem?>()
    var ItemValue = ArrayMap<String, FieldData>()
    var myItemViewType = VIEW_TYPE_TEXT_FIELD

    fun setChildItemList(ChildItemList: List<TopFieldsItem?>) {
        this.itemList = ChildItemList as ArrayList<TopFieldsItem?>
        notifyDataSetChanged()
    }

    fun setChildItemValue(ChildItemValue: ArrayMap<String, FieldData>) {
        this.ItemValue = ChildItemValue
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ChildViewHolder {

        contexta = viewGroup.context
        var layoutId = R.layout.item_field_text
        when (i) {
            VIEW_TYPE_TEXT_FIELD -> layoutId = R.layout.item_field_text
            VIEW_TYPE_DROP_DOWN -> layoutId = R.layout.item_field_text
            VIEW_TYPE_TEXT_FIELD_NAME -> layoutId = R.layout.item_field_textview
        }
        contexta = viewGroup.context
        val view: View = LayoutInflater
            .from(viewGroup.context)
            .inflate(
                layoutId,
                viewGroup, false
            )
        return ChildViewHolder(view)
    }


    override fun getItemViewType(position: Int): Int {
        val fieldType = itemList[position]?.type
        if (fieldType.equals("dropdown")) {
            myItemViewType = VIEW_TYPE_DROP_DOWN
            return VIEW_TYPE_DROP_DOWN
        }

        if (fieldType.equals("hidden")) {
            myItemViewType = VIEW_TYPE_TEXT_FIELD_NAME
            return VIEW_TYPE_TEXT_FIELD_NAME
        }
        myItemViewType = VIEW_TYPE_TEXT_FIELD
        return VIEW_TYPE_TEXT_FIELD
    }

    override fun onBindViewHolder(
        childViewHolder: ChildViewHolder,
        position: Int
    ) {

        val field = itemList[position]
        val fieldType = field?.type


        when (myItemViewType) {

            VIEW_TYPE_TEXT_FIELD_NAME -> {
                childViewHolder.itemTextView?.text = ItemValue.get(field?.slug)?.value
            }

            VIEW_TYPE_TEXT_FIELD -> {

                childViewHolder.fieldsEdt?.hint = field?.title
                disableEditText(childViewHolder.fieldsEdt!!)

                childViewHolder.fieldsEdt?.setText(
                    ItemValue.get(field?.slug)?.value,
                    TextView.BufferType.EDITABLE
                )
            }

            VIEW_TYPE_DROP_DOWN -> {
                childViewHolder.fieldsEdt?.hint = field?.title

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    childViewHolder.fieldsEdt?.setBackgroundColor(context.getColor(R.color.colorBlue))
                    childViewHolder.fieldsEdt?.setTextColor(context.getColor(R.color.white))

                }

                childViewHolder.fieldsEdt?.setText(
                    ItemValue.get(field?.slug)?.value,
                    TextView.BufferType.EDITABLE
                )

            }

        }


        val fadeIn: Animation = AlphaAnimation(0f, 1f)
        fadeIn.setDuration(1000)

        childViewHolder.itemView.animation = fadeIn

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ChildViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var fieldsEdt: TextInputEditText? = null
        var itemTextView: TextView? = null

        init {
            if (myItemViewType.equals(VIEW_TYPE_TEXT_FIELD_NAME))
                itemTextView = itemView.findViewById(R.id.itemTextView)
            fieldsEdt = itemView.findViewById(R.id.fieldEdt)
        }
    }

    private fun disableEditText(editText: TextInputEditText) {
        editText.isFocusable = false
        editText.isEnabled = false
        editText.isCursorVisible = false
        editText.keyListener = null
    }

    private fun setPlayerNameEditText(editText: TextInputEditText) {
        editText.setBackgroundColor(Color.TRANSPARENT)
        editText.gravity = Gravity.CENTER
        editText.textSize = 22f
        editText.setTextColor(context.resources?.getColor(R.color.colorBlue)!!)
    }

}

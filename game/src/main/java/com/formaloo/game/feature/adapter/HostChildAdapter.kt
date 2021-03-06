package com.formaloo.game.feature.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.RecyclerView
import com.formaloo.data.model.FieldData
import com.formaloo.data.model.TopFieldsItem
import com.google.android.material.textfield.TextInputEditText
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.formaloo.common.Constants
import com.formaloo.game.R


class HostChildAdapter:
    RecyclerView.Adapter<HostChildAdapter.ChildViewHolder>() {


    companion object {
        const val VIEW_TYPE_TEXT_FIELD = 0
        const val VIEW_TYPE_DROP_DOWN = 1
        const val VIEW_TYPE_TEXT_FIELD_NAME = 2
    }


    var status = ""
    var itemList = arrayListOf<TopFieldsItem?>()
    var ItemValue = ArrayMap<String, FieldData>()
    var editTextValue = ArrayMap<String, Any>()
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

        var layoutId = R.layout.item_field_text
        when (i) {
            VIEW_TYPE_TEXT_FIELD -> layoutId = R.layout.item_field_text
            VIEW_TYPE_DROP_DOWN -> layoutId = R.layout.item_field_status
            VIEW_TYPE_TEXT_FIELD_NAME -> layoutId = R.layout.item_field_name
        }

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
        if (fieldType.equals(Constants.DROPDOWN)) {
            myItemViewType = PlayerChildAdapter.VIEW_TYPE_DROP_DOWN
        } else if (fieldType.equals(Constants.HIDDEN)) {
            myItemViewType = PlayerChildAdapter.VIEW_TYPE_TEXT_FIELD_NAME
        } else {
            myItemViewType = PlayerChildAdapter.VIEW_TYPE_TEXT_FIELD
        }
        return myItemViewType
    }

    override fun onBindViewHolder(
        childViewHolder: ChildViewHolder,
        position: Int
    ) {

        var list = Array(8, { i -> "" })


        val field = itemList[position]

        when (myItemViewType) {

            VIEW_TYPE_TEXT_FIELD_NAME->{
                childViewHolder.itemTextView?.text = ItemValue.get(field?.slug)?.value
            }

            VIEW_TYPE_TEXT_FIELD -> {
                if (!field?.title.equals(Constants.POINT))
                    disableEditText(childViewHolder.fieldsEdt!!)
                childViewHolder.fieldsEdt?.hint = field?.title


                if(!ItemValue.get(field?.slug)?.value.isNullOrBlank()) {
                    childViewHolder.fieldsEdt?.setText(
                        ItemValue.get(field?.slug)?.value,
                        TextView.BufferType.EDITABLE
                    )
                    childViewHolder.fieldsEdt?.setTextColor(childViewHolder.fieldsEdt!!.context.resources.getColor(R.color.purple))
                }
            }
            VIEW_TYPE_DROP_DOWN -> {

                val field1 = itemList[position]

                field1?.choiceItems?.forEachIndexed { index, choiceItemsItem ->
                    list.set(index, choiceItemsItem?.title!!)
                }

            }

        }


        childViewHolder.fieldsEdt?.addTextChangedListener {
            editTextValue.put(field?.slug, it.toString())
        }

        childViewHolder.fieldSpinner?.setOnClickListener {

            AlertDialog.Builder(childViewHolder.fieldSpinner!!.context)
                .setSingleChoiceItems(list, -1, null)
                .setPositiveButton("Ok", { dialog, whichButton ->
                    dialog.dismiss()
                    val selectedPosition: Int =
                        (dialog as AlertDialog).getListView().getCheckedItemPosition()
                    status = list[selectedPosition]

                    var choiceItemSlug =
                        itemList.get(position)?.choiceItems?.get(selectedPosition)?.slug
                    var fieldSlug = itemList.get(position)?.slug
                    editTextValue.put(fieldSlug, choiceItemSlug)
                    childViewHolder.fieldSpinner?.text = status
                    // Do something useful withe the position of the selected radio button
                })
                .show()
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
        var fieldSpinner: TextView? = null
        var itemTextView: TextView? = null

        init {

            if (myItemViewType == VIEW_TYPE_TEXT_FIELD)
                fieldsEdt = itemView.findViewById(R.id.fieldEdt)
            if (myItemViewType == VIEW_TYPE_DROP_DOWN)
                fieldSpinner = itemView.findViewById(R.id.dropdownSpinner)
            if (myItemViewType == VIEW_TYPE_TEXT_FIELD_NAME)
                itemTextView = itemView.findViewById(R.id.itemTextView)

        }
    }

    private fun disableEditText(editText: TextInputEditText) {
        editText.isFocusable = false
        editText.isEnabled = false
        editText.isCursorVisible = false
        editText.keyListener = null
        //editText.setBackgroundColor(Color.TRANSPARENT)
    }

}

package co.idearun.game

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
import timber.log.Timber
import android.R.attr.country
import android.content.Context
import android.content.DialogInterface
import android.view.ContentInfo
import android.widget.*
import androidx.appcompat.app.AlertDialog
import co.idearun.common.base.OnRvItemClickListener
import co.idearun.data.model.form.Form
import splitties.alertdialog.appcompat.*


class ChildItemAdapter(
    var context: Context
) :
    RecyclerView.Adapter<ChildItemAdapter.ChildViewHolder>() {

    private var onRvItemClickListener: OnRvItemClickListener<callBackData>? = null


    companion object {
        const val VIEW_TYPE_TEXT_FIELD = 0
        const val VIEW_TYPE_DROP_DOWN = 1
    }

    var status = ""
    var point = ""
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

        var layoutId = R.layout.item_field_text
        when (i) {
            VIEW_TYPE_TEXT_FIELD -> layoutId = R.layout.item_field_text
            VIEW_TYPE_DROP_DOWN -> layoutId = R.layout.item_field_dropdown
        }
        // Here we inflate the corresponding
        // layout of the child item
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

        myItemViewType = VIEW_TYPE_TEXT_FIELD
        return VIEW_TYPE_TEXT_FIELD
    }

    override fun onBindViewHolder(
        childViewHolder: ChildViewHolder,
        position: Int
    ) {
        childViewHolder.bind(itemList[position]!!)

        var list = Array(4, { i -> "" })


        val field = itemList[position]
        Timber.i("field size list ${itemList.size}")

        when (myItemViewType) {
            VIEW_TYPE_TEXT_FIELD -> {
                Timber.i("test test moo")
                childViewHolder.fieldsEdt?.hint = field?.title


                childViewHolder.fieldsEdt?.setText(
                    ItemValue.get(field?.slug)?.value,
                    TextView.BufferType.EDITABLE
                )
            }
            VIEW_TYPE_DROP_DOWN -> {

                val field1 = itemList[position]
                Timber.i("bib $position")
                Timber.i("bib ${itemList[position]?.choiceItems?.size}")

                field1?.choiceItems?.forEachIndexed { index, choiceItemsItem ->
                    list.set(index, choiceItemsItem?.title!!)
                }

            }

        }


        childViewHolder.fieldsEdt?.setOnClickListener {
            Timber.i("mio ${field?.title}")
        }

        childViewHolder.fieldSpinner?.setOnClickListener {
            Timber.i("bib $position")


            AlertDialog.Builder(context)
                .setSingleChoiceItems(list, -1, null)
                .setPositiveButton("ok", { dialog, whichButton ->
                    dialog.dismiss()
                    val selectedPosition: Int =
                        (dialog as AlertDialog).getListView().getCheckedItemPosition()
                    status = list[selectedPosition]
                    // Do something useful withe the position of the selected radio button
                })
                .show()
        }



        if (onRvItemClickListener != null) {
            childViewHolder.itemView.setOnClickListener {
                var data: callBackData?
                data = callBackData(status, "")
                if (childViewHolder.fieldsEdt?.hint == "point") {
                    data = callBackData(status, childViewHolder.fieldsEdt?.text.toString())
                    Timber.i("data " + data)
                }
                onRvItemClickListener!!.onItemClick(data,2)
            }
        }


        val fadeIn: Animation = AlphaAnimation(0f, 1f)
        fadeIn.setDuration(1000)

        childViewHolder.itemView.animation = fadeIn

    }

    override fun getItemCount(): Int {

        // This method returns the number
        // of items we have added
        // in the ChildItemList
        // i.e. the number of instances
        // of the ChildItemList
        // that have been created
        return itemList.size
    }

    // This class is to initialize
    // the Views present
    // in the child RecyclerView
    inner class ChildViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var fieldsEdt: TextInputEditText? = null
        var fieldSpinner: TextView? = null

        init {

            if (myItemViewType == VIEW_TYPE_TEXT_FIELD)
                fieldsEdt = itemView.findViewById(R.id.fieldEdt)
            if (myItemViewType == VIEW_TYPE_DROP_DOWN)
                fieldSpinner = itemView.findViewById(R.id.dropdownSpinner)

        }

        fun bind(field: TopFieldsItem) {


        }
    }

    fun setOnRvItemClickListener(onRvItemClickListener: OnRvItemClickListener<callBackData>) {
        this.onRvItemClickListener = onRvItemClickListener
    }

    data class callBackData(
        var status: String,
        var point: String
    )

}

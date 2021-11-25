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
import timber.log.Timber
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import co.idearun.common.base.OnRvItemClickListener
import co.idearun.game.R


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


                if (field?.type == "hidden") {
                    childViewHolder.fieldsEdt?.setBackgroundColor(Color.TRANSPARENT)
                    childViewHolder.fieldsEdt?.gravity = Gravity.CENTER
                    childViewHolder.fieldsEdt?.textSize = 22f
                    childViewHolder.fieldsEdt?.setTextColor(context.resources.getColor(R.color.colorBlue))
                }

                if (!field?.title.equals("امتیاز"))
                    disableEditText(childViewHolder?.fieldsEdt!!)
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


/*        onChange(childViewHolder.fieldsEdt!!, object: Runnable{
            override fun run() {
                Timber.i("text changed listener ${childViewHolder.fieldsEdt!!.text.toString()}")
            }

        })   */



        childViewHolder.fieldsEdt?.addTextChangedListener {
            onChange(childViewHolder.fieldsEdt!!, {
                Timber.i("text changed listener  ${itemList[position]?.title} is -> ${childViewHolder.fieldsEdt!!.text.toString()} ")
            })
        }

        childViewHolder.fieldsEdt?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Timber.i(p0.toString())
                editTextValue.put(field?.slug, p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

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
                    Timber.i("status $status" + itemList.get(position)?.choiceItems?.get(selectedPosition)?.slug)
                    var choiceItemSlug = itemList.get(position)?.choiceItems?.get(selectedPosition)?.slug
                    var fieldSlug = itemList.get(position)?.slug
                    editTextValue.put(fieldSlug,choiceItemSlug)
                    editTextValue.forEach {
                        Timber.i("${it.key} data is -> ${it.value}")
                    }
                    childViewHolder.fieldSpinner?.text = status
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
                    Timber.i("data " + data + "status $status")
                }
                onRvItemClickListener!!.onItemClick(data, 2)
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

    fun comS(s1: String, s2: String): Boolean {
        if (s1.length == s2.length) {
            val l = s1.length
            for (i in 0 until l) {
                if (s1[i] != s2[i]) return false
            }
            return true
        }
        return false
    }

    fun onChange(EdTe: EditText, FRun: Runnable?) {
        class finalS {
            var s = ""
        }

        val dat = finalS()
        EdTe.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    dat.s = "" + EdTe.text
                } else if (!comS(dat.s, "" + EdTe.text)) {
                    Handler().post(FRun!!)
                }
            }
        })
    }

    private fun disableEditText(editText: TextInputEditText) {
        editText.isFocusable = false
        editText.isEnabled = false
        editText.isCursorVisible = false
        editText.keyListener = null
        //editText.setBackgroundColor(Color.TRANSPARENT)
    }

}

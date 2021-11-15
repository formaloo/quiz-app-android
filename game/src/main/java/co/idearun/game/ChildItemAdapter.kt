package co.idearun.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.RecyclerView
import co.idearun.data.model.FieldData
import co.idearun.data.model.TopFieldsItem
import com.google.android.material.textfield.TextInputEditText
import timber.log.Timber


class ChildItemAdapter(
) :
    RecyclerView.Adapter<ChildItemAdapter.ChildViewHolder>() {


    var itemList = arrayListOf<TopFieldsItem?>()
    var ItemValue = ArrayMap<String, FieldData>()

    fun setChildItemList(ChildItemList : List<TopFieldsItem?>){
        this.itemList = ChildItemList as ArrayList<TopFieldsItem?>
        notifyDataSetChanged()
    }

    fun setChildItemValue(ChildItemValue: ArrayMap<String, FieldData>){
        this.ItemValue = ChildItemValue
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ChildViewHolder {

        // Here we inflate the corresponding
        // layout of the child item
        val view: View = LayoutInflater
            .from(viewGroup.context)
            .inflate(
                R.layout.item_field_text,
                viewGroup, false
            )
        return ChildViewHolder(view)
    }

    override fun onBindViewHolder(
        childViewHolder: ChildViewHolder,
        position: Int
    ) {
        childViewHolder.bind(itemList[position]!!)

        val field = itemList[position]

        childViewHolder.fieldsEdt.hint = field?.title
        childViewHolder.fieldsEdt.setText(ItemValue.get(field?.slug)?.value, TextView.BufferType.EDITABLE)

        if (field?.type.equals("dropdown")) {
            field?.choiceItems?.forEach { }
            childViewHolder.fieldsEdt.setText("این فیلد وضعیت است", TextView.BufferType.EDITABLE)
        }


        Timber.i("in CHiled Adapter ${itemList.size}")
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
        var fieldsEdt: TextInputEditText

        init {
            fieldsEdt = itemView.findViewById(R.id.fieldEdt)

        }

        fun bind(field: TopFieldsItem) {



        }
    }
}

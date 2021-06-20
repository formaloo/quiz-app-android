package co.idearun.learningapp.feature.flashCard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import co.idearun.learningapp.R
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import timber.log.Timber


class SwipeStackAdapter(
    private val mData: List<Fields>,
    private val form: Form
) : BaseAdapter() {

    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(position: Int): Fields {
        return mData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val vh: FlashCardStackHolder
        var view = convertView
        val item = mData[position]

        Timber.e("getView $item")
        if (convertView == null) {

            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_stack_section_item, parent, false)

            vh = FlashCardStackHolder(view)
            view?.tag = vh
            vh.bindItems(item, position, form)

        } else {
            ((convertView.tag) as FlashCardStackHolder).bindItems(item, position, form)
        }

        return view

    }


}
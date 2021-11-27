package com.formaloo.feature.lesson.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.formaloo.feature.R
import com.formaloo.data.model.form.Fields
import com.formaloo.data.model.form.Form
import com.formaloo.feature.lesson.adapter.holder.StackHolder


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
        val vh: StackHolder
        var view = convertView
        val item = mData[position]

        if (convertView == null) {

            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_stack_section_item, parent, false)

            vh = StackHolder(view)
            view?.tag = vh
            vh.bindItems(item, position, form)

        } else {
            ((convertView.tag) as StackHolder).bindItems(item, position, form)
        }

        return view

    }


}
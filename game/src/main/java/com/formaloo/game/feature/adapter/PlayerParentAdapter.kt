package com.formaloo.game.feature.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.formaloo.data.model.TopFieldsItem
import com.formaloo.game.R
import com.formaloo.game.feature.adapter.model.ParentItem


class PlayerParentAdapter:
    RecyclerView.Adapter<PlayerParentAdapter.ParentViewHolder>() {

    var itemList = arrayListOf<ParentItem>()

    fun setItemList(ChildItemList : List<ParentItem?>){
        this.itemList = ChildItemList as ArrayList<ParentItem>
        notifyDataSetChanged()
    }


    private val viewPool = RecycledViewPool()
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ParentViewHolder {

        val view: View = LayoutInflater
            .from(viewGroup.context)
            .inflate(
                R.layout.item_parent,
                viewGroup, false
            )
        return ParentViewHolder(view)
    }

    override fun onBindViewHolder(
        parentViewHolder: ParentViewHolder,
        position: Int
    ) {

        val (slug, fieldList, fieldValue) = itemList[position]


        val layoutManager = LinearLayoutManager(
            parentViewHolder.ChildRecyclerView
                .context,
            LinearLayoutManager.VERTICAL,
            false
        )

        layoutManager.initialPrefetchItemCount = fieldList?.get(position)?.size!!

        val childItemAdapter = PlayerChildAdapter()
        childItemAdapter.setChildItemValue(fieldValue?.get(position)!!)
        childItemAdapter.setChildItemList(fieldList?.get(position) as ArrayList<TopFieldsItem?>)


        parentViewHolder.ChildRecyclerView.layoutManager = layoutManager
        parentViewHolder.ChildRecyclerView.adapter = childItemAdapter
        parentViewHolder.ChildRecyclerView
            .setRecycledViewPool(viewPool)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ParentViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val ChildRecyclerView: RecyclerView

        init {
            ChildRecyclerView = itemView
                .findViewById(
                    R.id.childRecyclerView
                )

        }
    }
}

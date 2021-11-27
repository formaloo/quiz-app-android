package com.formaloo.game.feature.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.formaloo.common.TokenContainer
import com.formaloo.data.model.TopFieldsItem
import com.formaloo.game.R
import com.formaloo.game.feature.adapter.model.ParentItem
import com.formaloo.game.feature.viewmodel.FormViewModel


class HostParentAdapter(var vm: FormViewModel) :
    RecyclerView.Adapter<HostParentAdapter.ParentViewHolder>() {

    var itemList = arrayListOf<ParentItem>()

    fun setItemList(ChildItemList: List<ParentItem?>) {
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

        val childItemAdapter = HostChildAdapter()
        childItemAdapter.setChildItemValue(fieldValue?.get(position)!!)
        childItemAdapter.setChildItemList(fieldList.get(position) as ArrayList<TopFieldsItem?>)

        parentViewHolder.ChildRecyclerView.layoutManager = layoutManager
        parentViewHolder.ChildRecyclerView.adapter = childItemAdapter
        parentViewHolder.ChildRecyclerView
            .setRecycledViewPool(viewPool)

        parentViewHolder.actionButton.setOnClickListener {
            vm.editRow(slug!!,TokenContainer.authorizationToken!!,childItemAdapter.editTextValue)
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ParentViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val ChildRecyclerView: RecyclerView
        val actionButton: Button

        init {
            ChildRecyclerView = itemView
                .findViewById(
                    R.id.childRecyclerView
                )
            actionButton = itemView
                .findViewById(
                    R.id.actionBtn
                )

            actionButton.visibility = View.VISIBLE
        }
    }
}

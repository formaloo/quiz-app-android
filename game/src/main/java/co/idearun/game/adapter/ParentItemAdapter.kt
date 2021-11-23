package co.idearun.game.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import androidx.recyclerview.widget.RecyclerView.VISIBLE
import co.idearun.common.base.OnRvItemClickListener
import co.idearun.data.model.TopFieldsItem
import co.idearun.game.R
import co.idearun.game.model.ParentItem
import timber.log.Timber


class ParentItemAdapter (var context: Context) :
    RecyclerView.Adapter<ParentItemAdapter.ParentViewHolder>() {
    // An object of RecyclerView.RecycledViewPool
    // is created to share the Views
    // between the child and
    // the parent RecyclerViews

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

        // Here we inflate the corresponding
        // layout of the parent item
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

        // Create an instance of the ParentItem
        // class for the given position
        val (ParentItemTitle, fieldList, fieldValue) = itemList[position]



        // For the created instance,
        // get the title and set it
        // as the text for the TextView

        parentViewHolder.ParentItemTitle.text = ParentItemTitle

        // Create a layout manager
        // to assign a layout
        // to the RecyclerView.

        // Here we have assigned the layout
        // as LinearLayout with vertical orientation
        val layoutManager = LinearLayoutManager(
            parentViewHolder.ChildRecyclerView
                .context,
            LinearLayoutManager.VERTICAL,
            false
        )

        // Since this is a nested layout, so
        // to define how many child items
        // should be prefetched when the
        // child RecyclerView is nested
        // inside the parent RecyclerView,
        // we use the following method
        layoutManager.initialPrefetchItemCount = fieldList?.get(position)?.size!!

        // Create an instance of the child
        // item view adapter and set its
        // adapter, layout manager and RecyclerViewPool
        val childItemAdapter = ChildItemAdapter(context)
        childItemAdapter.setChildItemValue(fieldValue?.get(position)!!)
        childItemAdapter.setChildItemList(fieldList?.get(position)!! as ArrayList<TopFieldsItem?>)

        Timber.i("khob $position")
        fieldValue?.get(position)!!.entries.forEach {
            Timber.i("khob ${it.value} vs ${it.key}")
        }


        childItemAdapter.setOnRvItemClickListener(object:
            OnRvItemClickListener<ChildItemAdapter.callBackData> {
            override fun onItemClick(item: ChildItemAdapter.callBackData, position: Int) {
                Timber.i("on click is worked ${item.point}")
            }

        })


        parentViewHolder.ChildRecyclerView.layoutManager = layoutManager
        parentViewHolder.ChildRecyclerView.adapter = childItemAdapter
        parentViewHolder.ChildRecyclerView
            .setRecycledViewPool(viewPool)
    }

    // This method returns the number
    // of items we have added in the
    // ParentItemList i.e. the number
    // of instances we have created
    // of the ParentItemList
    override fun getItemCount(): Int {
        return itemList.size
    }

    // This class is to initialize
    // the Views present in
    // the parent RecyclerView
    inner class ParentViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val ParentItemTitle: TextView
        val ChildRecyclerView: RecyclerView
        val actionButton: Button

        init {
            ParentItemTitle = itemView
                .findViewById(
                    R.id.textView3
                )
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

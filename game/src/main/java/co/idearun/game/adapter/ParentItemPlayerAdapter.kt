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


class ParentItemPlayerAdapter (var context: Context) :
    RecyclerView.Adapter<ParentItemPlayerAdapter.ParentViewHolder>() {
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
        val (slug, fieldList, fieldValue) = itemList[position]



        // For the created instance,
        // get the title and set it
        // as the text for the TextView


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

        Timber.i("khob $position and ${fieldValue?.size} and ${fieldList?.get(position)?.size}")
        fieldValue?.get(position)!!.entries.forEach {
            Timber.i("khob ${it.value} vs ${it.key}")
        }

        Timber.i("khob $position")
        fieldList?.get(position)!!.forEach {
            Timber.i("khob ${it?.title}}")
        }


        layoutManager.initialPrefetchItemCount = fieldList.get(position).size

        // Create an instance of the child
        // item view adapter and set its
        // adapter, layout manager and RecyclerViewPool
        val childItemAdapter = ChildItemPlayerAdapter(context)
        childItemAdapter.setChildItemValue(fieldValue.get(position))
        childItemAdapter.setChildItemList(fieldList.get(position) as ArrayList<TopFieldsItem?>)


        childItemAdapter.setOnRvItemClickListener(object: OnRvItemClickListener<ChildItemPlayerAdapter.callBackData>{
            override fun onItemClick(item: ChildItemPlayerAdapter.callBackData, position: Int) {
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
        val ChildRecyclerView: RecyclerView

        init {

            ChildRecyclerView = itemView
                .findViewById(
                    R.id.childRecyclerView
                )

        }
    }
}

package com.formaloo.feature.lesson.adapter.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.formaloo.feature.R
import com.formaloo.feature.databinding.LayoutCsatItemBinding
import com.formaloo.feature.lesson.adapter.*
import com.formaloo.feature.lesson.listener.CSATListener
import java.util.*

class CSATAdapter(
    private val item: ArrayList<out Enum<*>>,
    private val selectedItem: Int? = null,
    private val csatListener: CSATListener
) :
    RecyclerView.Adapter<CSATAdapter.NPSItemViewHolder>() {


    private var selectedPos: Int? = selectedItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NPSItemViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_csat_item, parent, false);
        return NPSItemViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: NPSItemViewHolder, position_: Int) {
        holder.bindItems(item[holder.absoluteAdapterPosition])

        holder.itemView.isSelected = selectedPos == position_;

        holder.itemView.setOnClickListener {
            if (selectedPos!=null && selectedPos!!>=0){
                notifyItemChanged(selectedPos!!)
            }
            selectedPos = holder.absoluteAdapterPosition
            notifyItemChanged(holder.absoluteAdapterPosition)

            csatListener.csatSelected(holder.absoluteAdapterPosition + 1)
        }
    }


    override fun getItemCount(): Int {
        return item.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    class NPSItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = LayoutCsatItemBinding.bind(itemView)

        fun bindItems(item: Enum<*>) {

            when (item) {
                is CSATOutline -> {
                    binding.scatTxv.setText(item.str)
                    binding.scatImg.setImageResource(item.drawable)
                }
                is CSATFunny -> {
                    binding.scatTxv.setText(item.str)
                    binding.scatImg.setImageResource(item.drawable)

                }
                is CSATFlat -> {
                    binding.scatTxv.setText(item.str)
                    binding.scatImg.setImageResource(item.drawable)

                }
                is CSATMonster -> {
                    binding.scatTxv.setText(item.str)
                    binding.scatImg.setImageResource(item.drawable)

                }
                is CSATHEART -> {
                    binding.scatTxv.setText(item.str)
                    binding.scatImg.setImageResource(item.drawable)
                    binding.scatTxv.invisible()
                }
                is CSATSTAR -> {
                    binding.scatTxv.setText(item.str)
                    binding.scatImg.setImageResource(item.drawable)
                    binding.scatTxv.invisible()

                }
                else -> {

                }
            }


        }


    }
}



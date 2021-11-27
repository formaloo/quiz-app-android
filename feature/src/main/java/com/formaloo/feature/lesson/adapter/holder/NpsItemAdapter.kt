package com.formaloo.feature.lesson.adapter.holder

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.formaloo.feature.R
import com.formaloo.common.Constants
import com.formaloo.data.model.form.Fields
import com.formaloo.data.model.form.Form
import com.formaloo.feature.databinding.LayoutFlashCardNpsBtnItemBinding
import com.formaloo.feature.lesson.listener.LessonListener
import com.formaloo.feature.viewmodel.UIViewModel

class NpsItemAdapter(
    private val fields: Fields,
    private val form: Form,
    private val viewmodel: UIViewModel,
    private val lessonListener: LessonListener
) : RecyclerView.Adapter<NpsItemAdapter.NPSItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NPSItemViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_flash_card_nps_btn_item, parent, false);
        return NPSItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NPSItemViewHolder, position_: Int) {
        holder.bindItems(fields, position_, form, viewmodel, lessonListener)

        holder.setIsRecyclable(false)
    }


    override fun getItemCount(): Int {
        return 11
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    class NPSItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var lastSelectedItem: AppCompatButton? = null
        val binding = LayoutFlashCardNpsBtnItemBinding.bind(itemView)

        fun bindItems(
            field: Fields,
            position_: Int,
            form: Form,
            viewmodel: UIViewModel,
            lessonListener: LessonListener

        ) {
            binding.field = field
            binding.form = form
            binding.holder = this
            binding.viewmodel = viewmodel
            binding.lifecycleOwner = binding.npsBtn.context as LifecycleOwner

            binding.npsBtn.setOnClickListener {
                viewmodel.npsBtnClicked(field, absoluteAdapterPosition)
                Handler(Looper.getMainLooper()).postDelayed({
                    lessonListener.next()
                }, Constants.AUTO_NEXT_DELAY)
            }
            if (field.required == true) {
                viewmodel.reuiredField(field)

            } else {

            }
        }



    }
}



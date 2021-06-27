package co.idearun.learningapp.feature.lesson.adapter.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.R
import co.idearun.learningapp.common.extension.invisible
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.LayoutFlashCardNpsBtnItemBinding
import co.idearun.learningapp.feature.viewmodel.UIViewModel

class NpsItemAdapter(
    private val fields: Fields,
    private val form: Form,
    private val viewmodel: UIViewModel,
    private val errLay: RelativeLayout
) : RecyclerView.Adapter<NpsItemAdapter.NPSItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NPSItemViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_flash_card_nps_btn_item, parent, false);
        return NPSItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NPSItemViewHolder, position_: Int) {
        holder.bindItems(fields, position_, form, viewmodel, errLay)

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
            errLay: RelativeLayout

        ) {
            binding.field = field
            binding.form = form
            binding.holder = this
            binding.viewmodel = viewmodel
            binding.lifecycleOwner = binding.npsBtn.context as LifecycleOwner

            binding.npsBtn.setOnClickListener {
                viewmodel.npsBtnClicked(field, absoluteAdapterPosition)
            }
            if (field.required == true) {
                viewmodel.reuiredField(field)

            } else {

            }
        }



    }
}



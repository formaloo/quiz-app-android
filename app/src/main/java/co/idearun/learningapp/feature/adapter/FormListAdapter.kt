package co.idearun.learningapp.feature.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.R
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.LessonContentBinding
import org.koin.core.KoinComponent
import java.io.Serializable


class FormListAdapter(
    progressMap: HashMap<String?, Int?>?,
    private val listener: FormListListener
) :
    PagingDataAdapter<Form, FormListAdapter.BtnsViewHolder>(DiffUtilCallBack()),
    Serializable {

    private var formsProgressMap: HashMap<String?, Int?>?=progressMap

    fun resetProgress(formsProgressMap: HashMap<String?, Int?>?){
        this.formsProgressMap=formsProgressMap
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): BtnsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.lesson_content, parent, false)
        return BtnsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BtnsViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bindItems(item, listener,formsProgressMap)
        }

    }

    class BtnsViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), Serializable, KoinComponent {
        val binding = LessonContentBinding.bind(itemView)

        fun bindItems(
            form: Form?,
            listener: FormListListener,
            formsProgressMap: HashMap<String?, Int?>?
        ) {
            binding.item = form
            formsProgressMap?.let {
                val progress = formsProgressMap[form?.slug ?: ""]
                binding.progress=progress
            }
            itemView.setOnClickListener {
                listener.openForm(form, binding.formItemLay)

            }
        }

    }

}



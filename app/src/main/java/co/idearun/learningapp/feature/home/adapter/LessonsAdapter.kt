package co.idearun.learningapp.feature.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.R
import co.idearun.learningapp.common.extension.isVisible
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.LessonContentBinding
import org.koin.core.KoinComponent
import timber.log.Timber
import java.io.Serializable


class LessonsAdapter(
    progressMap: HashMap<String?, Int?>?,
    private val listener: LessonListListener
) : PagingDataAdapter<Form, LessonsAdapter.ViewHolder>(DiffUtilCallBack()),
    Serializable {

    private var formsProgressMap: HashMap<String?, Int?>? = progressMap

    fun resetProgress(formsProgressMap: HashMap<String?, Int?>?) {
        this.formsProgressMap = formsProgressMap
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.lesson_content, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bindItems(item, listener, (formsProgressMap?: hashMapOf())[item.slug])
        }
        holder.setIsRecyclable(false)

    }

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), Serializable, KoinComponent {
        val binding = LessonContentBinding.bind(itemView)

        fun bindItems(
            form: Form?,
            listener: LessonListListener,
            progress:Int?
        ) {
            binding.item = form

            binding.progress = progress?:0
            binding.done = progress?:0 == -1

            Timber.e("bindItems startTv ${binding.startTv.isVisible() } ")
            Timber.e("bindItems formProgress ${binding.formProgress.isVisible() } ")
            itemView.setOnClickListener {
                listener.openLesson(form, binding.formItemLay)

            }
        }


    }

}



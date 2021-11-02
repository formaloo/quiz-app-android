package co.idearun.game.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.collection.ArrayMap
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import co.idearun.auth.viewmodel.AuthViewModel
import co.idearun.common.TokenContainer
import co.idearun.game.FormFieldsAdapter
import co.idearun.game.R
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_form.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class PlayerFormFragment : Fragment() {

    lateinit var adapter: FormFieldsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_form, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm: FormViewModel by activityViewModels()
        adapter = FormFieldsAdapter()
        rvFields.adapter = adapter



        vm.initLessonAddress(vm.userForm.value?.form?.address!!)
        vm.getFormData()


        vm.form.observe(this, {
            Timber.i("TAG get form $it")
            Timber.i("TAG get form ${it.fields_list?.size}")
            val fields = it.fields_list
            adapter.submitList(it.fields_list)

            fields?.forEach {
                Timber.i("TAG field title ${it.title}")
            }
        })

        val jsonBody = HashMap<String, Any>()
        submitFormBtn.setOnClickListener {

            adapter.fieldSlugList.forEachIndexed { index, fields ->
                val view = rvFields.getChildAt(index)
                val viewHolder = adapter.FormFieldsViewHolder(view)

                val value = viewHolder.fieldsEdt.text.toString()
                val slug = fields.slug!!

                jsonBody.put(slug, value)

                Timber.i("players " + viewHolder.fieldsEdt.text.toString())
            }.also {
                // submit form
            }


        }


    }
}

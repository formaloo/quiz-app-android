package co.idearun.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.ArrayMap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import co.idearun.common.TokenContainer
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_form_host.*
import kotlinx.android.synthetic.main.fragment_form.rvFields
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import timber.log.Timber

class HostFormFragment : Fragment() {

    lateinit var adapter: FormFieldsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_form_host, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm: FormViewModel by activityViewModels()
        adapter = FormFieldsAdapter()
        rvFields.adapter = adapter

        val liveCode = arguments?.getString("liveCode")
        var slug: String? = null

        vm.getFormDataWithLiveCode("JWT ${TokenContainer.authorizationToken}", liveCode!!)

        vm.liveForm.observe(this,{
            val address = it?.form?.address!!
            slug = it.form?.slug!!
            vm.initLessonAddress(address)
            vm.getFormData()
        })





        vm.form1.observe(this, {
            Timber.i("TAG get form $it")
            Timber.i("TAG get form ${it.fields_list?.size}")
            val fields = it.fields_list
            adapter.submitList(it.fields_list)

            fields?.forEach {
                Timber.i("TAG field title ${it.title} = ${it.slug}")
            }
        })

        val body = ArrayMap<String, Any>()
        submitFormBtn.setOnClickListener {

            adapter.fieldSlugList.forEachIndexed { index, fields ->
                val view = rvFields.getChildAt(index)
                val viewHolder = adapter.FormFieldsViewHolder(view)

                if (!viewHolder.fieldsEdt.text.toString().isBlank()) {
                    Timber.i("test nulla")
                    val value = viewHolder.fieldsEdt.text.toString()
                    val slugField = fields.slug!!
                    body[slugField] = value
                }

               // Timber.i(fields.title + " = " + slug + " = " + value)
            }.also {
                val bodyM = RequestBody.create(
                    "application/json; charset=utf-8".toMediaTypeOrNull(),
                    JSONObject(body).toString()
                )

                vm.submitFormData(slug!!, bodyM).also {
                    vm.disableForm(slug!!,"JWT ${TokenContainer.authorizationToken}", false)
                }
            }

        }


        vm.disableForm.observe(this,{
            val args = Bundle()
            args.putString("slug", slug)
            findNavController().navigate(R.id.action_hostFormFragment_to_resultFragment, args)
        })


    }
}

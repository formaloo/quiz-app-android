package co.idearun.game.feature.host

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.ArrayMap
import androidx.navigation.fragment.findNavController
import co.idearun.common.TokenContainer
import co.idearun.game.R
import co.idearun.game.adapter.FormFieldsAdapter
import co.idearun.game.base.BaseFragment
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_formeditor.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class FormEditorFragment : BaseFragment() {

    lateinit var adapter: FormFieldsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_formeditor, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val formVm: FormViewModel by viewModel()
        adapter = FormFieldsAdapter()
        adapter.disableField = true

        parentRecyclerView.adapter = adapter
        val formAddress = arguments?.getString("formAddress")
        val formSlug = arguments?.getString("formSlug")

        Timber.i("TAG Address $formAddress")
        Timber.i("TAG Slug $formSlug")
        formVm.initLessonAddress(formAddress!!)
        formVm.getFormData()


        formVm.form1.observe(this, {
            Timber.i("TAG get form $it")
            Timber.i("TAG get form ${it.fields_list?.size}")
            val fields = it.fields_list
            Timber.i("field size "+ fields?.size)
            adapter.submitList(it.fields_list)

            formTitleEdt.setText(it.title)

            fields?.forEach {
                Timber.i("TAG field title ${it.title}")
            }
            //Toast.makeText(context, "title -> ${it.title} and slug -> ${it.slug} ", Toast.LENGTH_LONG).show()

        })


        setBtn.setOnClickListener {
            Timber.i(TokenContainer.authorizationToken)
            Timber.i(TokenContainer.sessionToken)
            //  vm.copyForm(formSlug!!, "JWT ${TokenContainer.authorizationToken}")

            val req = ArrayMap<String, Any>()
            req["title"] = formTitleEdt.text.toString()
            req["description"] = formDescriptionEdt.text.toString()
            req["public_rows"] = true

            formVm.editForm(formSlug!!, "JWT ${TokenContainer.authorizationToken}", req)
        }

        formVm.editForm.observe(this, {
            formVm.createLive(it.slug, "JWT ${TokenContainer.authorizationToken}")
        })

        formVm.liveForm.observe(this, {
            val args = Bundle()
            Timber.i(it.code)
            args.putString("liveCode", it.code)
            args.putString("liveDashboardAddress", it.form?.liveDashboardAddress)
            findNavController().navigate(R.id.action_formEditorFragment_to_shareFragment, args)

        })

        formVm.failure.observe(this, {
            formVm.hideLoading()
            checkFailureStatus(it)
        })

        formVm.isLoading.observe(this,{
            if (it) loading.visibility = View.VISIBLE else loading.visibility = View.GONE
        })

    }
}

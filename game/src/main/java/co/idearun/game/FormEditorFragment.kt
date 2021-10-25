package co.idearun.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import co.idearun.auth.viewmodel.AuthViewModel
import co.idearun.common.TokenContainer
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_formeditor.*
import kotlinx.android.synthetic.main.fragment_games.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class FormEditorFragment : Fragment() {

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

        val vm: FormViewModel by viewModel()
        adapter = FormFieldsAdapter()
        rvFields.adapter = adapter
        val formAddress = arguments?.getString("formAddress")
        val formSlug = arguments?.getString("formSlug")

        Timber.i("TAG Address $formAddress")
        Timber.i("TAG Slug $formSlug")
        vm.initLessonAddress(formAddress!!)
        vm.getFormData()


        val vm1: AuthViewModel by viewModel()

        vm.form.observe(this, {
            Timber.i("TAG get form $it")
            Timber.i("TAG get form ${it.fields_list?.size}")
            val fields = it.fields_list
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
            vm.copyForm(formSlug!!, "JWT ${TokenContainer.authorizationToken}")
        }


    }

}
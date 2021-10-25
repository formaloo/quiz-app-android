package co.idearun.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.collection.ArrayMap
import androidx.fragment.app.Fragment
import co.idearun.auth.viewmodel.AuthViewModel
import co.idearun.common.TokenContainer
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_formeditor.*
import kotlinx.android.synthetic.main.fragment_games.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
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


        val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Im15YXBwLTAwMSJ9.eyJ0b2tlbiI6ImF1dGgiLCJzaWQiOiJjOTcxZmNkZC1iMTgwLTQ2MWYtOWZjOS1iN2ExMzliNjExNDkiLCJ1aWQiOjEwMTAyLCJlbWFpbCI6InRlc3Q1QGdtYWlsLmNvbSIsImZpcnN0X25hbWUiOiJ0ZXN0NSIsImxhc3RfbmFtZSI6IiIsInBob25lX251bWJlciI6IjA5MTEzMjEyMjExIiwidXNlcm5hbWUiOiI1NTFiNzc4Y2MwMzk0OGFjOWIwYzgwNTBhNGU4MDIiLCJ2ZXJpZmllZF9lbWFpbCI6ZmFsc2UsInZlcmlmaWVkX3Bob25lIjpmYWxzZSwibGFzdF91cGRhdGUiOiIyMDIxLTEwLTA5VDIwOjQ2OjU3LjczNloiLCJncm91cHMiOiIiLCJpc3MiOiJpY2FzIiwiYXVkIjpbImljYXMiLCJjcm0iLCJmb3JteiIsImludm9pY2UiLCJwcm9qZWN0YW50IiwiYWN0aW9ucyJdLCJleHAiOjE2MzUyMTQ1ODIsImlhdCI6MTYzNTE4NDU4Mn0.xtZZAXN2FDtYRDP1yf19zJiKRDyn-NR-p8NO6ulrukBpCiWhN9DyqBRmLUorCoUR0fI4U25bmzX2ld2SVmwvby2hu60dBu9Dn8WjCR52xlOHXJt1khCur0x_ztNA6ov7fxbKoKtr6kln6t0pst-Jia_FSMpv9RRcMbRKnqxvAB9wkT1K5OEMz2cJ6cIPQiNfWJzcN6k3LVmLkLdAfwr8btikxCWeDRZjUQ13U1z8mf8ap63hYCpfiIhqOACsnqcHwVoOZ9QYGP7BX5WHbUg1ci26KvnVi7KLptGrkwU138JuvQ_jcataBo2-BMnyLItu7uUSL75DUWp01Ii5XKZfgQ"
        setBtn.setOnClickListener {
            Timber.i(TokenContainer.authorizationToken)
            Timber.i(TokenContainer.sessionToken)
          //  vm.copyForm(formSlug!!, "JWT ${TokenContainer.authorizationToken}")

            val req = ArrayMap<String, Any>()
            req["title"] = formTitleEdt.text.toString()
            req["description"] = formDescriptionEdt.text.toString()

            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(), JSONObject(req).toString()
            )

            vm.editForm(formSlug!!, "JWT $token", body)
        }
    }
}

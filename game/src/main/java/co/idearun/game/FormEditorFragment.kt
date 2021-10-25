package co.idearun.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import co.idearun.auth.viewmodel.AuthViewModel
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

        val token =
            "ewyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Im15YXBwLTAwMSJ9.eyJ0b2tlbiI6ImF1dGgiLCJzaWQiOiJjOTcxZmNkZC1iMTgwLTQ2MWYtOWZjOS1iN2ExMzliNjExNDkiLCJ1aWQiOjEwMTAyLCJlbWFpbCI6InRlc3Q1QGdtYWlsLmNvbSIsImZpcnN0X25hbWUiOiJ0ZXN0NSIsImxhc3RfbmFtZSI6IiIsInBob25lX251bWJlciI6IjA5MTEzMjEyMjExIiwidXNlcm5hbWUiOiI1NTFiNzc4Y2MwMzk0OGFjOWIwYzgwNTBhNGU4MDIiLCJ2ZXJpZmllZF9lbWFpbCI6ZmFsc2UsInZlcmlmaWVkX3Bob25lIjpmYWxzZSwibGFzdF91cGRhdGUiOiIyMDIxLTEwLTA5VDIwOjQ2OjU3LjczNloiLCJncm91cHMiOiIiLCJpc3MiOiJpY2FzIiwiYXVkIjpbImljYXMiLCJjcm0iLCJmb3JteiIsImludm9pY2UiLCJwcm9qZWN0YW50IiwiYWN0aW9ucyJdLCJleHAiOjE2MzUxOTQ4NDIsImlhdCI6MTYzNTE2NDg0Mn0.ZTZtMuahJ43rMPtflMrb2FL_Qz9I4-mNUEZwGNM5x_ZYYlhILHMMcuqy3flXkOQxgVrzvRaJN-xUcbzivN5-1iLy_4tyip9T1SXjMzYvq0erKk3SqrtiPNUFz-joGhsXmCo7UIe4fFf87pz0Y8EyhVQszER06fLXzqH1yL_Amzq5DYFSc_qV0gJ1MmQosz3HlCDmT0cy3kWhAYx9-3H7JQ5ob53r40mRF0eChG1GbsAZRXui6w4O_NsZvPT9XbeEmtKa2qrLcAkQTCeRuIqjATZGgaV96LdyopwpvGn6lUuC1LXzDB1Gv_X4cCds2q3EgS9FddgfwYOG_CVGl3TiWA"

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
            vm.copyForm(formSlug!!, "JWT $token")
        }


    }

}
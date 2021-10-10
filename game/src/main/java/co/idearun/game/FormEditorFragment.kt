package co.idearun.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_games.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class FormEditorFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_games, container,false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm: FormViewModel by viewModel()

        val formAddress = arguments?.getString("formAddress")

        Timber.i("TAG $formAddress")
        vm.initLessonAddress(formAddress!!)
        vm.getFormData()

        vm.form.observe(this,{
            Timber.i("TAG get form $it")
        })

    }

}
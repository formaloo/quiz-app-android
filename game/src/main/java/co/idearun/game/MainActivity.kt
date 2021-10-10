package co.idearun.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import co.idearun.data.model.form.Form
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val vm: FormViewModel by viewModel()
        Log.i("TAG", "onCreate in quiz app")


        val m = vm.getLessonsList(false)
        Log.i("TAG", "getLessonsList: ${m.isCompleted}")


        vm.getFormTag(0)
        vm.formTag.observe(this,{

            Timber.i("Tag list data ${it.data?.forms?.size}")
            Timber.i("Tag list data $it")

            val address = it.data?.forms?.get(0)?.address
            Timber.i("TAG $address")
            vm.initLessonAddress(address!!)
            vm.getFormData()

        })

        vm.form.observe(this,{
            Timber.i("$it")
        })



        lifecycleScope.launch {
            vm.fetchLessonList(false).collectLatest { pagingData ->
                Log.i("TAG", "pagingData: ${pagingData}")
            }
        }

        vm.form.observe(this, object : Observer<Form> {
            override fun onChanged(t: Form?) {
                Log.i("TAG", "form result: $t")

            }
        })
    }
}
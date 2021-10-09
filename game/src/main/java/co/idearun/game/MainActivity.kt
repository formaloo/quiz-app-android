package co.idearun.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import co.idearun.data.model.form.Form
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val vm: FormViewModel by viewModel()
        Log.i("TAG", "onCreate in quiz app")


        val m = vm.getLessonsList(false)
        Log.i("TAG", "getLessonsList: ${m.isCompleted}")


        val test = vm.getCatList()


        vm.initLessonAddress("pl7sv")
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
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

class GamesFragment: Fragment() {

    lateinit var adapter: GamesAdapter
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

        adapter = GamesAdapter()

        rvGameList.adapter = adapter
        val vm: FormViewModel by viewModel()

        vm.getFormTag(0)
        vm.formTag.observe(this,{

            adapter.submitList(it.data?.forms?.toMutableList())
            Timber.i("Tag list data $it")

/*
            val address = it.data?.forms?.get(0)?.address
            Timber.i("TAG $address")
            vm.initLessonAddress(address!!)
            vm.getFormData()*/

        })


    }

}
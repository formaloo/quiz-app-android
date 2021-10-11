package co.idearun.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.idearun.common.base.OnRvItemClickListener
import co.idearun.data.model.form.Form
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_games.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class GamesFragment : Fragment() {

    lateinit var adapter: GamesAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_games, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*

        val fadeIn: Animation = AlphaAnimation(0f, 1f)
        fadeIn.setDuration(1000)

        ivSelectGame.animation = fadeIn
*/


        ivSelectGame.startAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_in))

        adapter = GamesAdapter()

        adapter.setOnRvItemClickListener(object : OnRvItemClickListener<Form> {
            override fun onItemClick(item: Form, position: Int) {

                val args = Bundle()
                args.putString("formAddress", item.address)
                findNavController().navigate(
                    R.id.action_gamesFragment_to_formEditorFragment, args
                )
            }

        })
        rvGameList.adapter = adapter
        val vm: FormViewModel by viewModel()

        vm.getFormTag(0)
        vm.formTag.observe(this, {

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
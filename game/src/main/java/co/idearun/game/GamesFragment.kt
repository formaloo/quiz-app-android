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
import co.idearun.common.UserInfoManager
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

        val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Im15YXBwLTAwMSJ9.eyJ0b2tlbiI6ImF1dGgiLCJzaWQiOiJjOTcxZmNkZC1iMTgwLTQ2MWYtOWZjOS1iN2ExMzliNjExNDkiLCJ1aWQiOjEwMTAyLCJlbWFpbCI6InRlc3Q1QGdtYWlsLmNvbSIsImZpcnN0X25hbWUiOiJ0ZXN0NSIsImxhc3RfbmFtZSI6IiIsInBob25lX251bWJlciI6IjA5MTEzMjEyMjExIiwidXNlcm5hbWUiOiI1NTFiNzc4Y2MwMzk0OGFjOWIwYzgwNTBhNGU4MDIiLCJ2ZXJpZmllZF9lbWFpbCI6ZmFsc2UsInZlcmlmaWVkX3Bob25lIjpmYWxzZSwibGFzdF91cGRhdGUiOiIyMDIxLTEwLTA5VDIwOjQ2OjU3LjczNloiLCJncm91cHMiOiIiLCJpc3MiOiJpY2FzIiwiYXVkIjpbImljYXMiLCJjcm0iLCJmb3JteiIsImludm9pY2UiLCJwcm9qZWN0YW50IiwiYWN0aW9ucyJdLCJleHAiOjE2MzUyMTQ1ODIsImlhdCI6MTYzNTE4NDU4Mn0.xtZZAXN2FDtYRDP1yf19zJiKRDyn-NR-p8NO6ulrukBpCiWhN9DyqBRmLUorCoUR0fI4U25bmzX2ld2SVmwvby2hu60dBu9Dn8WjCR52xlOHXJt1khCur0x_ztNA6ov7fxbKoKtr6kln6t0pst-Jia_FSMpv9RRcMbRKnqxvAB9wkT1K5OEMz2cJ6cIPQiNfWJzcN6k3LVmLkLdAfwr8btikxCWeDRZjUQ13U1z8mf8ap63hYCpfiIhqOACsnqcHwVoOZ9QYGP7BX5WHbUg1ci26KvnVi7KLptGrkwU138JuvQ_jcataBo2-BMnyLItu7uUSL75DUWp01Ii5XKZfgQ"
        val vm: FormViewModel by viewModel()
        val args = Bundle()

        adapter.setOnRvItemClickListener(object : OnRvItemClickListener<Form> {
            override fun onItemClick(item: Form, position: Int) {
                vm.copyForm(item.slug, "JWT $token")
            }
        })


        vm.form.observe(this,{

            with(it){
                args.putString("formAddress", address)
                args.putString("formSlug", slug)
            }

            findNavController().navigate(
                R.id.action_gamesFragment_to_formEditorFragment, args
            )
        })


        rvGameList.adapter = adapter

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
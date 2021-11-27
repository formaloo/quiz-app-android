package com.formaloo.game.feature.host

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.formaloo.common.TokenContainer
import com.formaloo.common.base.OnRvItemClickListener
import com.formaloo.data.model.form.Form
import com.formaloo.game.R
import com.formaloo.game.feature.adapter.GamesAdapter
import com.formaloo.game.base.BaseFragment
import com.formaloo.game.feature.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_games.*
import org.koin.android.viewmodel.ext.android.viewModel

class GamesFragment : BaseFragment() {

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

        // set anim for imageView
        ivSelectGame.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))

        val formVm: FormViewModel by viewModel()

        adapter = GamesAdapter()
        rvGameList.adapter = adapter

        /* get forms with Game Tag from formaloo server
        * all game form has game tag*/
        formVm.getFormTag(0)
        formVm.formTag.observe(this, {
            adapter.submitList(it.data?.forms?.toMutableList())
        })


        /* when click on a form with Game tag, copyForm request has copy it in your formaloo account*/
        adapter.setOnRvItemClickListener(object : OnRvItemClickListener<Form> {
            override fun onItemClick(item: Form, position: Int) {
                formVm.copyForm(item.slug, "JWT ${TokenContainer.authorizationToken}")
            }
        })


        /*
        * in this section you have game from on your formaloo account
        * now we send form address and slug to form editor fragment to
        * config your form name, description and view fields
        */
        formVm.form.observe(this, {

            val args = Bundle()
            with(it) {
                args.putString("formAddress", address)
                args.putString("formSlug", slug)
            }

            // navigate with navigation component
            findNavController().navigate(
                R.id.action_gamesFragment_to_formEditorFragment, args
            )
        })


        // handle failure
        formVm.failure.observe(this, {
            formVm.hideLoading()
            checkFailureStatus(it)
        })

        // handle loading
        formVm.isLoading.observe(this, {
            if (it) loading.visibility = View.VISIBLE else loading.visibility = View.GONE
        })

    }

}
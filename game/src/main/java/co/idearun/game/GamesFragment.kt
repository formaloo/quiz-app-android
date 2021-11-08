package co.idearun.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import co.idearun.common.TokenContainer
import co.idearun.common.base.OnRvItemClickListener
import co.idearun.data.model.form.Form
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_games.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

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
/*

        val fadeIn: Animation = AlphaAnimation(0f, 1f)
        fadeIn.setDuration(1000)

        ivSelectGame.animation = fadeIn
*/


        ivSelectGame.startAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_in))

        adapter = GamesAdapter()


       val formVm: FormViewModel by viewModel()
//        val vmAuth: AuthViewModel by viewModel()
        val args = Bundle()

    /*    vmAuth.authorizeUser(TokenContainer.sessionToken!!)
        vmAuth.authorizeData.observe(this,{
            TokenContainer.authorizationToken = it.token
        })*/

        Timber.i("Token ${TokenContainer.authorizationToken}")
        adapter.setOnRvItemClickListener(object : OnRvItemClickListener<Form> {
            override fun onItemClick(item: Form, position: Int) {
                formVm.copyForm(item.slug, "JWT ${TokenContainer.authorizationToken}")
            }
        })


        formVm.form.observe(this,{

            with(it){
                args.putString("formAddress", address)
                args.putString("formSlug", slug)
            }

            findNavController().navigate(
                R.id.action_gamesFragment_to_formEditorFragment, args
            )
        })


        rvGameList.adapter = adapter

        formVm.getFormTag(0)
        formVm.formTag.observe(this, {

            adapter.submitList(it.data?.forms?.toMutableList())
            Timber.i("Tag list data $it")


/*
            val address = it.data?.forms?.get(0)?.address
            Timber.i("TAG $address")
            vm.initLessonAddress(address!!)
            vm.getFormData()*/

        })


        formVm.getSubmitsRow("kCF8jgM13VWAprX")
        formVm.submits.observe(this,{
            Timber.i("test live"+it.status)
        })
        formVm.failure.observe(this, {
            formVm.hideLoading()
            checkFailureStatus(it)
        })

        formVm.isLoading.observe(this,{
            if (it) loading.visibility = View.VISIBLE else loading.visibility = View.GONE
        })

    }

}
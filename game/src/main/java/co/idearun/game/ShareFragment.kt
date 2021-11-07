package co.idearun.game

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.idearun.auth.viewmodel.AuthViewModel
import co.idearun.common.UserInfoManager
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_share.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.w3c.dom.Text
import java.util.zip.Inflater
import android.R.attr.label
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService
import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE

import androidx.core.content.ContextCompat.getSystemService
import timber.log.Timber


class ShareFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_share, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userInfoManager = UserInfoManager(requireContext())


        val liveCode = arguments?.getString("liveCode")
        liveCodeEdt.setText(liveCode, TextView.BufferType.EDITABLE)

        shareLiveCodeBtn.setOnClickListener {
            shareAction(liveCode!!)
        }

        playBtn.setOnClickListener {
            val args = Bundle()
            args.putString("liveCode", liveCode)
            findNavController().navigate(R.id.action_shareFragment_to_hostFormFragment, args)


            (requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).apply {
                setPrimaryClip(ClipData.newPlainText("simple text", liveCode))
            }
        }

        startAnim(imageView3)
    }

    private fun startAnim(view: View) {
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
    }

    private fun shareAction(liveCode: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, liveCode)
        shareIntent.setType("text/plain")
        startActivity(shareIntent)
    }

}
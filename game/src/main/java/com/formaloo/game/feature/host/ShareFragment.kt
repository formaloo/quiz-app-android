package com.formaloo.game.feature.host

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_share.*

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.widget.Toast

import com.formaloo.game.R


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

        imageView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))


        /*** get liveCode and liveDashboardAddress from [FormEditorFragment]
        */
        val liveCode = arguments?.getString("liveCode")
        val liveDashboardAddress = arguments?.getString("liveDashboardAddress")

        // show live code and copy in clipboard when click on it
        liveCodeTv.setText(liveCode, TextView.BufferType.EDITABLE)
        liveCodeTv.setOnClickListener {
            (requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).apply {
                setPrimaryClip(ClipData.newPlainText("simple text", liveCode))
            }
            Toast.makeText(context, getString(R.string.copy_clipboard), Toast.LENGTH_LONG).show()
        }


        // share live code with intent
        shareLiveCodeBtn.setOnClickListener {
            shareAction(liveCode!!)
        }

        // play game
        playBtn.setOnClickListener {
            val args = Bundle()
            args.putString("liveCode", liveCode)
            args.putString("liveDashboardAddress", liveDashboardAddress)
            findNavController().navigate(R.id.action_shareFragment_to_hostFormFragment, args)
        }
    }

    // share live code with this method
    private fun shareAction(liveCode: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, liveCode)
        shareIntent.setType("text/plain")
        startActivity(shareIntent)
    }

}
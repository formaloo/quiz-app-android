package com.formaloo.game.feature.host

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.formaloo.game.R
import kotlinx.android.synthetic.main.fragment_host.*

class HostFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_host, container,false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_left))

        beHostBtn.setOnClickListener {
            findNavController().navigate(R.id.action_hostFragment_to_gamesFragment)
        }

    }

}
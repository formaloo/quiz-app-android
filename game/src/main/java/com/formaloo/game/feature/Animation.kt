package com.formaloo.game.feature

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import com.formaloo.game.R

object Animation {
    fun fadeInAnim(view: View, context: Context?){
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
    }
}
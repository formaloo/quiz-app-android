package co.idearun.game

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils

object Animation {
    fun fadeInAnim(view: View, context: Context?){
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
    }
}
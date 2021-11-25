package co.idearun.game.feature

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import co.idearun.game.R

object Animation {
    fun fadeInAnim(view: View, context: Context?){
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
    }
}
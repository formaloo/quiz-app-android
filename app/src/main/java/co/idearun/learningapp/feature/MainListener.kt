package co.idearun.learningapp.feature

import android.view.View
import co.idearun.learningapp.R
import co.idearun.learningapp.common.BaseMethod
import org.koin.core.KoinComponent
import org.koin.core.inject

interface MainListener : KoinComponent {

    fun openFormaloo(v: View?) {
        val context = v?.context
        context?.let {
            val baseMethod: BaseMethod by inject()
            baseMethod.openUri(context, context.getString(R.string.formaloo_site_address))

        }
    }
}
package com.formaloo.game.base

import androidx.fragment.app.Fragment
import com.formaloo.common.exception.Failure
import com.formaloo.game.R
import org.json.JSONObject
import splitties.alertdialog.appcompat.*
import timber.log.Timber

open class BaseFragment : Fragment() {


    // Show alert dialog
    fun openAlert(msg: String) {
        requireContext().alertDialog {
            message = msg
            setCancelable(false)
            okButton()
        }.onShow {
            positiveButton.setTextColor(resources.getColor(android.R.color.holo_blue_dark))
        }.show()
    }


    // handle Error and Failure
    fun checkFailureStatus(it: Failure) {
        when (it) {
            is Failure.FeatureFailure -> renderFailure(it.msgRes)
            is Failure.UNAUTHORIZED_Error -> {
                openAlert(getString(R.string.auth_err))
            }
            is Failure.ServerError -> {
                openAlert(getString(R.string.server_err))
            }
            is Failure.NetworkConnection -> {
                openAlert(getString(R.string.no_internet))
            }
            else -> {
                openAlert(getString(R.string.no_internet))
            }
        }

    }


    fun renderFailure(message: String?) {
        Timber.e("renderFailure $message")
        message?.let {
            try {
                val jObjError = JSONObject(message)
                setErrorsToViews(jObjError)
            } catch (e: Exception) {
                openAlert(e?.message!!)
                Timber.e(e.localizedMessage)
            }
        }
    }

    fun setErrorsToViews(jObjError: JSONObject) {
        if (jObjError.has("errors")) {
            val jsonArray = jObjError.getJSONArray("errors")
            jsonArray?.let {
                if (jsonArray.length() > 0 && jsonArray[0] is JSONObject) {
                    openAlert((jsonArray[0] as JSONObject)["message"].toString())
                }
            }


        }
    }


}
package co.idearun.game

import android.widget.Toast
import androidx.fragment.app.Fragment
import co.idearun.common.exception.Failure
import org.json.JSONObject
import timber.log.Timber

open class BaseFragment: Fragment() {


    fun checkFailureStatus(it: Failure) {
        when (it) {
            is Failure.FeatureFailure -> renderFailure(it.msgRes)
            is Failure.UNAUTHORIZED_Error -> {
                Toast.makeText(
                    context,
                    getString(R.string.auth_err),
                    Toast.LENGTH_LONG
                ).show()

            }
            is Failure.ServerError -> {
                Toast.makeText(
                    context,
                    getString(R.string.server_err),
                    Toast.LENGTH_LONG
                ).show()

            }
            is Failure.NetworkConnection -> {
                Toast.makeText(
                    context,
                    getString(R.string.no_internet),
                    Toast.LENGTH_LONG
                ).show()

            }
            else -> {
                Toast.makeText(
                    context,
                    getString(R.string.no_internet),
                    Toast.LENGTH_LONG
                ).show()
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
                Toast.makeText(
                    context,
                    e.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()

                Timber.e("${e.localizedMessage}")

            }
        }
    }

    fun setErrorsToViews(jObjError: JSONObject) {
        if (jObjError.has("errors")) {
            val jsonArray = jObjError.getJSONArray("errors")
            jsonArray?.let {
                if (jsonArray.length() > 0 && jsonArray[0] is JSONObject) {
                    Toast.makeText(
                        context,
                        (jsonArray[0] as JSONObject)["message"].toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        }
    }
}
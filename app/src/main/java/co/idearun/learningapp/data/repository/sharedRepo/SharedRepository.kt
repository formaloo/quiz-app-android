package co.idearun.learningapp.data.repository.sharedRepo

import android.content.SharedPreferences
import co.idearun.learningapp.common.Constants
import org.json.JSONObject

interface SharedRepository {
    fun saveFormProgress(formProgress: Map<String?, Int?>)
    fun retrieveFormProgress(): HashMap<String?, Int?>

    fun saveLastForm(formSlug:String?)
    fun getLastForm():String?
}


class SharedRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : SharedRepository {

    override fun saveFormProgress(formProgress: Map<String?, Int?>) {
        val jsonObject = JSONObject(formProgress)
        val formProgressString = jsonObject.toString()
        sharedPreferences.edit().putString(Constants.PREFERENCES_PROGRESS, formProgressString)
            .apply()
    }

    override fun retrieveFormProgress(): HashMap<String?, Int?>{
        val outputMap = hashMapOf<String?, Int?>()

        sharedPreferences.getString(Constants.PREFERENCES_PROGRESS,null)?.let {
            val jsonObject = JSONObject(it)
            val keysItr = jsonObject.keys()
            while (keysItr.hasNext()) {
                val key = keysItr.next()
                val value = jsonObject[key] as Int
                outputMap[key] = value
            }
        }


        return outputMap
    }

    override fun saveLastForm(formSlug: String?) {
        sharedPreferences.edit().putString(Constants.PREFERENCES_LAST_FORM, formSlug).apply()

    }

    override fun getLastForm(): String? {
        return sharedPreferences.getString(Constants.PREFERENCES_LAST_FORM,"")
    }

}
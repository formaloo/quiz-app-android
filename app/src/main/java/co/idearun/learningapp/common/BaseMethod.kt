package co.idearun.learningapp.common

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.*
import android.util.Base64
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.loader.content.CursorLoader
import co.idearun.learningapp.R
import co.idearun.learningapp.common.Constants.PICKFILE_RESULT_CODE
import co.idearun.learningapp.feature.Binding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class BaseMethod {
    val TAG = "BaseMethod"

    fun rotateView(view: View?, deg: Float, duration: Long) {
        val rotate: ObjectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, deg)
        //        rotate.setRepeatCount(10);
        rotate.setDuration(duration)
        rotate.start()
    }


    fun getHexHashtagColorFromRgbStr(color: String?): String? {
        return try {
            when {
                color != null -> {
                    val rgbToInt = getIntColorFromRgbStr(color)
                    when {
                        rgbToInt != null -> {
                            return convertIntColorToHashtagHex(rgbToInt)
                        }
                        else -> {
                            null
                        }
                    }
                }
                else -> {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e(Binding.TAG, "getHexColor: $e")
            null
        }

    }

    fun getIntColorFromRgbStr(color_: String?): Int? {
        return try {
            var color = color_
            if (color != null) {
                color = color.replace("{\"", "")
                color = color.replace("\"", "")

                val a = color.substring(color.indexOf("a:") + 2, color.indexOf("}"))
                val r = color.substring(color.indexOf("r:") + 2, color.indexOf(",g"))
                val g = color.substring(color.indexOf("g:") + 2, color.indexOf(",b"))
                val b = color.substring(color.indexOf("b:") + 2, color.indexOf(",a"))

                return convertRgbToInt(r, g, b)


            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(Binding.TAG, "getHexColor: $e")
            null
        }

    }

    fun convertIntColorToHex(color: Int): String {
        return Integer.toHexString(color)
    }

    fun convertIntColorToHashtagHex(color: Int): String {
        return "#${convertIntColorToHex(color)}"
    }

    fun convertRgbToInt(r: String, g: String, b: String): Int {
        return Color.rgb(Integer.parseInt(r), Integer.parseInt(g), Integer.parseInt(b))
    }

    fun getColorStateList(color: String?): ColorStateList? {
        getHexHashtagColorFromRgbStr(color)?.let {
            return ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_enabled),
                    intArrayOf(android.R.attr.state_enabled)
                ), intArrayOf(
                    Color.parseColor(it) //disabled
                    , Color.parseColor(it) //enabled
                )
            )

        }
        return null
    }

    fun openUri(activity: Activity, uri: String) {
        if (uri.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(uri)
            activity.startActivity(intent)
        }

    }

    @JvmName("hasPermissions1")
    fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun hideNotificationBar(activity: Activity) {
        //Remove notification bar
        activity.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

    }

    fun openLogo(supportActionBar: ActionBar?, logo: Int) {
        supportActionBar?.setLogo(logo)
        supportActionBar?.setDisplayUseLogoEnabled(true)
    }

    fun hideLogo(supportActionBar: ActionBar?) {
        supportActionBar?.setDisplayUseLogoEnabled(false)
        supportActionBar?.setLogo(null)

    }

    fun hideABTitle(supportActionBar: ActionBar?) {
        supportActionBar?.setDisplayShowTitleEnabled(false)

    }

    fun showABTitle(supportActionBar: ActionBar?) {
        supportActionBar?.setDisplayShowTitleEnabled(true)

    }

    fun showBackBtn(supportActionBar: ActionBar?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }

    fun hideBackBtn(supportActionBar: ActionBar?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)

    }

    fun hideAB(supportActionBar: ActionBar?) {
        supportActionBar?.hide()

    }

    fun showAB(supportActionBar: ActionBar?) {
        supportActionBar?.show()

    }

    fun setTitle(supportActionBar: ActionBar?, title: Int) {
        supportActionBar?.setTitle(title)
        supportActionBar?.setDisplayShowTitleEnabled(true)

    }


    fun getDateOnLocale(time: String): String? {
        return convertStringToDate(time)

    }

    fun loadImage(drawable: String?, window: Window, hexColor: String?) {
        if (drawable != null)
            try {
                Picasso.get().load(drawable).into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        window.setBackgroundDrawable(BitmapDrawable(bitmap))
                    }

                    override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                        Log.e(TAG, "onBitmapFailed: $e")
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                        hexColor?.let {
                            window.setBackgroundDrawable(ColorDrawable(Color.parseColor(it)))

                        }
                    }

                })
            } catch (e: Exception) {
                Log.e("loadImage", "Exception $e")

            }


    }

    fun closeExitNav(view: View) {
        view.startAnimation(
            AnimationUtils.loadAnimation(view.context, R.anim.nav_default_exit_anim)
        )
    }

    fun openNavEnter(view: View) {
        view.startAnimation(
            AnimationUtils.loadAnimation(
                view.context,
                R.anim.nav_default_enter_anim
            )
        )

    }

    fun closeExitBtm(view: View) {
        view.startAnimation(
            AnimationUtils.loadAnimation(
                view.context,
                R.anim.abc_slide_out_bottom
            )
        )
    }


    fun closeFragmentAnim(view: View) {
        view.startAnimation(
            AnimationUtils.loadAnimation(
                view.context,
                R.anim.nav_default_exit_anim
            )
        )
    }

    fun openFragmentAnim(view: View) {
        view.startAnimation(
            AnimationUtils.loadAnimation(
                view.context,
                R.anim.nav_default_enter_anim
            )
        )

    }

    fun openBtmEnter(view: View) {
        view.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.abc_slide_in_bottom))
    }

    fun closeBottomSlideSheet(view: View) {
        view.startAnimation(
            AnimationUtils.loadAnimation(
                view.context,
                R.anim.design_bottom_sheet_slide_out
            )
        )
    }

    fun openBottomSlideSheet(view: View) {
        view.startAnimation(
            AnimationUtils.loadAnimation(
                view.context,
                R.anim.design_bottom_sheet_slide_in
            )
        )

    }

    fun closeTopSlideSheet(view: View) {
        view.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.abc_tooltip_exit))
    }

    fun openTopSlideSheet(view: View) {
        view.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.abc_tooltip_enter))

    }

    fun changeStatusBarColor(activity: Activity, color: Int) {
        val window = activity.window
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color

        }else{

        }


    }

    fun showMsg(view: View, msg: Int) {

        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun showMsgCustom(v: View, msg: Int, context: Context) {
        //        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
        val snack = Snackbar.make(v, msg, Snackbar.LENGTH_LONG)
        val view = snack.getView()

        val params = view.getLayoutParams() as FrameLayout.LayoutParams
        params.gravity = Gravity.BOTTOM
        params.gravity = Gravity.CENTER_HORIZONTAL

        params.width = ViewGroup.LayoutParams.MATCH_PARENT
//        view.setBackground(context.resources.getDrawable(R.color.black_warm))
//        view.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
//                params.setMargins(0, 0, 0, 100);
        view.setLayoutParams(params)

        val snackbarView = snack.getView()
        val snackTextView = snackbarView.findViewById<View>(R.id.snackbar_text)

        snackTextView.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY)
//        snackTextView.setGravity(View.FOCUS_RIGHT)

        //        snack.setAction("بستن", new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                snack.dismiss();
        //            }
        //        });


        snack.show()

    }

    fun openFilePickerInten(activity: Activity, type: String, action: String) {
        var chooseFile = Intent(action)
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE)
        chooseFile.type = type
        activity.startActivityForResult(Intent.createChooser(chooseFile, ""), PICKFILE_RESULT_CODE)
    }


    fun getPathFromUri(uri: Uri, context: Context): String? {
        var result: String? = null
        val column_index: Int

        val projection = arrayOf(MediaStore.Images.Media.DATA)

        val cursorLoader = CursorLoader(context, uri, projection, null, null, null)

        val cursor = cursorLoader.loadInBackground()

        if (cursor != null) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(column_index)
            cursor.close()
        }


        return result
    }

    fun getPath(context: Context, uri: Uri): String? {
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf("_data")
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(uri, projection, null, null, null)
                cursor?.let {
                    val column_index: Int = cursor.getColumnIndexOrThrow("_data")
                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index)
                    }
                }

            } catch (e: java.lang.Exception) {
                // Eat it
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                "Title${Random().nextInt()}",
                null
            )

        return Uri.parse(path)
    }


    @Throws(FileNotFoundException::class)
    fun decodeUri(c: Context, uri: Uri?, requiredSize: Int): Bitmap? {
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true

        uri?.let {
            BitmapFactory.decodeStream(c.contentResolver.openInputStream(uri), null, o)

        }

        var width_tmp = o.outWidth
        var height_tmp = o.outHeight
        var scale = 1

        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break
            width_tmp /= 2
            height_tmp /= 2
            scale *= 2
        }

        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale

        return uri?.let {
            BitmapFactory.decodeStream(c.contentResolver.openInputStream(uri), null, o2)
        }
    }


    fun getOutputMediaFile(type: Int, context: Context): File? {
        val mediaStorageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        /**Create the storage directory if it does not exist */
        if (!mediaStorageDir!!.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }

        /**Create a media file name */
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val mediaFile: File
        if (type == 1) {
            mediaFile = File(
                mediaStorageDir.path + File.separator +
                        "IMG_" + timeStamp + ".png"
            )

            //            imageFilePath = mediaFile.getAbsolutePath();
        } else {
            return null
        }

        return mediaFile
    }

    fun convertBitmapToBase64(bitmap: Bitmap): String {

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos) //bm is the bitmap object
        val byteArrayImage = baos.toByteArray()
        return Base64.encodeToString(byteArrayImage, Base64.DEFAULT)


    }


    fun shareVia(extraTxt: String, title: String, mContext: Context) {
        val sendIntent = Intent()
        sendIntent.type = "text/plain"
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, extraTxt)
        val createChooser = Intent.createChooser(sendIntent, title)
        createChooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mContext.startActivity(createChooser)
    }

    fun retrieveErr(it: JSONObject, key: String): String {
        return when {
            it.has(key) -> {
                it.getJSONArray(key)[0].toString()
            }
            else -> ""
        }

    }

    fun retrieveGeneralErr(gErrors: JSONArray): String {
        gErrors.let {
            return if (it.length() > 0) {
                it[0].toString()
            } else ""
        }
    }


    fun getJSONObject(it: JSONObject, key: String): JSONObject {
        return if (it.has(key)) {
            it.getJSONObject(key) as JSONObject

        } else {
            JSONObject()
        }
    }

    fun getJSONArray(it: JSONObject, key: String): JSONArray {
        return if (it.has(key)) {
            it.getJSONArray(key)

        } else {
            JSONArray()
        }
    }

    fun retrieveJSONArrayFirstItem(gErrors: JSONArray): String {
        gErrors.let {
            return if (it.length() > 0) {
                it[0].toString()
            } else ""
        }
    }

    fun hasAllPermissionsGranted(@NonNull grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }


    fun converStrToDate(time: String): Date? {
        val sdf2 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var date: Date? = null
        try {
            date = sdf2.parse(time)

        } catch (e: Exception) {


        }

        return date
    }


    fun convertStringToDate(time: String): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val sdf2 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var date: Date? = null
        var datestr: String? = null

        if (time.isNotEmpty())
            try {
                date = sdf.parse(time)
                datestr = sdf2.format(date)

            } catch (e: Exception) {


            }

        return datestr

    }


}

package co.idearun.learningapp.common

import android.Manifest

object Constants {

    const val AUTO_NEXT_DELAY: Long = 400
    const val SCROLL_DELAY: Long = 150

    val PERMISSIONS_EXTERNAL_STORAGE = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    const val PERMISSION_ALL = 1
    const val EXTERNAL_STORAGE = 2
    const val PERMISSION_DUPLICATE = 1
    const val REQUEST_CAPTURE_IMAGE = 100
    const val REQUEST_GALLREY = 200
    const val requeiredSize: Int = 500
    const val PICKFILE_RESULT_CODE = 220

    const val ERRORS = "errors"
    const val GENERAL_ERRORS = "general_errors"
    const val FORM_ERRORS = "form_errors"
    const val SLUG = "slug"
    const val FORM = "form"
    const val FIELD = "field"

    const val DROPDOWN = "dropdown"
    const val MATRIX = "matrix"
    const val RATING = "rating"
    const val EMAIL = "email"
    const val FILE = "file"
    const val LONG_TEXT = "long_text"
    const val SHORT_TEXT = "short_text"
    const val SIGNATURE = "signature"
    const val MONEY = "money"
    const val PHONE = "phone"
    const val TIME = "time"
    const val WEBSITE = "website"
    const val MULTI_SELECT = "multiple_select"
    const val SINGLE_SELECT = "choice"
    const val YESNO = "yes_no"
    const val DATE = "date"
    const val META = "meta"
    const val NUMBER = "number"
    const val SECTION = "section"
    const val PAGE_BREAK = "page_break"
    const val PHONE_VERIFICATION = "phone_verification"
    const val Like_Dislike = "like_dislike"

    const val mobile = "mobile"
    const val landline = "landline"
    const val both = "both"

    const val image = "image"
    const val document = "document"
    const val all = "all"

    const val irr = "irr"
    const val irt = "irt"
    const val usd = "usd"
    const val eur = "eur"
    const val other = "other"

    const val embeded = "embeded"
    const val star = "star"
    const val like_dislike = "like dislike"
    const val nps = "nps"
    const val score = "score"

    const val PREFERENCES_PROGRESS = "PREFERENCES_PROGRESS"
    const val PREFERENCES_LAST_Lesson = "PREFERENCES_LAST_FORM"

}
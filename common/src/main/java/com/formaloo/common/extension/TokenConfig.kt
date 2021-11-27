package com.formaloo.common.extension

import android.view.View

fun String.toJWT(): String {
    return "JWT $this"
}

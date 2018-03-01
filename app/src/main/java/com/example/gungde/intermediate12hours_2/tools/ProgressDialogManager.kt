package com.example.gungde.intermediate12hours_2.tools

import android.app.Activity
import android.app.ProgressDialog

/**
 * Created by gungdeaditya on 2/26/18.
 */
class ProgressDialogManager(act : Activity) : ProgressDialog(act) {

    private val MESSAGE = "Memuat Data..."

    override fun show() {
        setProgressStyle(ProgressDialog.STYLE_SPINNER)
        setCancelable(false)
        setMessage(MESSAGE)
        super.show()
    }

}
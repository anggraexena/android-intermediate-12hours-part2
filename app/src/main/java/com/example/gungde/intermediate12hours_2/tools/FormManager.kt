package com.example.gungde.intermediate12hours_2.tools

import android.widget.EditText

/**
 * Created by gungdeaditya on 2/26/18.
 */
class FormManager {
    fun checkET(et: Array<EditText>): Boolean {
        var status = true
        for (i in 0..et.size - 1) {
            if (et[i].text.toString().trim() == "") {
                status = status && false
                et[i].error = "Field cannot be blank"
            } else {
                status = status && true
            }
        }
        return status
    }
}
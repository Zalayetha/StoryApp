package com.zaghy.storyapp.widget

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class CustomAlertDialog(private val title:String, private val message:String, private val buttonText:String):DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction.
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonText) { dialog, _ ->
                    dialog.dismiss()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
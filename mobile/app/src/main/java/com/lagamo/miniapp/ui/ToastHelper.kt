package com.lagamo.miniapp.ui

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.lagamo.miniapp.R

/**
 * Custom toast notifications matching the web app's design:
 *   - Cyan (success) with ✓ icon
 *   - Magenta (error) with ✗ icon
 */
object ToastHelper {

    fun showSuccess(context: Context, message: String) {
        showCustomToast(context, message, isSuccess = true)
    }

    fun showError(context: Context, message: String) {
        showCustomToast(context, message, isSuccess = false)
    }

    private fun showCustomToast(context: Context, message: String, isSuccess: Boolean) {
        val inflater = LayoutInflater.from(context)
        val layout: View = inflater.inflate(R.layout.toast_custom, null)

        val toastContainer = layout.findViewById<View>(R.id.toast_container)
        val toastIcon = layout.findViewById<TextView>(R.id.toast_icon)
        val toastMessage = layout.findViewById<TextView>(R.id.toast_message)

        if (isSuccess) {
            toastContainer.setBackgroundResource(R.drawable.bg_toast_success)
            toastIcon.setBackgroundResource(R.drawable.bg_toast_icon_success)
            toastIcon.setTextColor(context.getColor(R.color.toast_success_text))
            toastIcon.text = "✓"
            toastMessage.setTextColor(context.getColor(R.color.toast_success_text))
        } else {
            toastContainer.setBackgroundResource(R.drawable.bg_toast_error)
            toastIcon.setBackgroundResource(R.drawable.bg_toast_icon_error)
            toastIcon.setTextColor(context.getColor(R.color.toast_error_text))
            toastIcon.text = "✗"
            toastMessage.setTextColor(context.getColor(R.color.toast_error_text))
        }

        toastMessage.text = message

        val toast = Toast(context)
        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 80)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }
}

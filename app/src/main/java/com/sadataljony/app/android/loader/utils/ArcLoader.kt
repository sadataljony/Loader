package com.sadataljony.app.android.loader.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.sadataljony.app.android.myapplication.R

class ArcLoader(private val mContext: Context) {

    fun simpleArcLoader(): SimpleArcDialog {
        val dialog = SimpleArcDialog(mContext)
        val configuration = LoaderConfiguration(mContext)
        configuration.colors = intArrayOf(
            ContextCompat.getColor(mContext, R.color.purple_700),
            ContextCompat.getColor(mContext, R.color.teal_700)
        )
        configuration.loaderStyle = SimpleArcLoader.STYLE.COMPLETE_ARC
        configuration.setArcWidthInPixel(10)
        configuration.drawCircle(true)
        dialog.setConfiguration(configuration)
        dialog.setCancelable(false)
        return dialog
    }
}

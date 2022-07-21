package com.lostark.lostarkassistanthomework

import android.app.Dialog
import android.content.Context
import android.view.Window

class LoadingDialog : Dialog {
    constructor(context: Context) : super(context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.loading_dialog)
    }
}
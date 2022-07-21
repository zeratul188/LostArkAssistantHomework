package com.lostark.lostarkassistanthomework.checklist.objects

data class Preset(
    var name: String,
    var max: Int,
    var icon: String,
    var end: String,
    var isChecked: Boolean
)

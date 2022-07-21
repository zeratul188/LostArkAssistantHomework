package com.lostark.lostarkassistanthomework.checklist.objects

data class Checklist(
    var name: String,
    var now: Int,
    var max: Int,
    var icon: String,
    var end: String
)

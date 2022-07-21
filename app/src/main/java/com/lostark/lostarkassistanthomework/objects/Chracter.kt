package com.lostark.lostarkassistanthomework.objects

data class Chracter(
    var name: String,
    var level: Double,
    var server: String,
    var job: String,
    var isChecked: Boolean
): Comparable<Chracter> {
    override fun compareTo(other: Chracter): Int {
        if (level < other.level) {
            return 1
        } else if (level == other.level) {
            return 0
        } else {
            return -1
        }
    }

}

package com.lostark.lostarkassistanthomework.checklist.rooms

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lostark.lostarkassistanthomework.App
import java.io.Serializable

@Entity(tableName = "homework")
data class Homework (
    @PrimaryKey(autoGenerate = true) val id: Long,
    var name: String,
    var level: Double,
    var server: String,
    var job: String,
    var daylist: String,
    var daynows: String,
    var daymaxs: String,
    var dayicons: String,
    var dayends: String,
    var weeklist: String,
    var weeknows: String,
    var weekmaxs: String,
    var weekicons: String,
    var weekends: String,
    var dungeonrest: Int,
    var bossrest: Int,
    var questrest: Int,
    var dungeonlost: Int,
    var bosslost: Int,
    var questlost: Int,
    var auto: Boolean,
    var position: Int,
    var isGold: Boolean
): Comparable<Homework>, Serializable {
    override fun compareTo(other: Homework): Int {
        val isLevel = App.prefs.isBoolean("change", true)
        if (isLevel) {
            if (level < other.level) {
                return 1
            } else if (level == other.level) {
                return 0
            } else {
                return -1
            }
        } else {
            if (position > other.position) {
                return 1
            } else if (position == other.position) {
                return 0
            } else {
                return -1
            }
        }
    }

}
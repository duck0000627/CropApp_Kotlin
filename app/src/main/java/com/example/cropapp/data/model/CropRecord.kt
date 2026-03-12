package com.example.cropapp.data.model

import android.R
import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity 告訴 Room 這是一張資料表，可以自訂表名
@Entity(tableName = "crop_records")
data class CropRecord (
    // 讓 id 自動遞增，就像 SQLite 的 AUTOINCREMENT
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,          // 日期(yyyy/mm/dd)
    val cropName: String,     // 作物名稱
    val task: String,       //工作內容
    val field: String,       //工作場地
    val fertilizerName: String, // 肥料名稱
    val amount: Double,       // 用量
    val unit: String = "kg",  // 單位
    val timestamp: Long = System.currentTimeMillis(), // 紀錄時間
)

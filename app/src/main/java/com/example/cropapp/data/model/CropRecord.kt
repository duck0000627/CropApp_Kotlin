package com.example.cropapp.data.model

import android.R
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

// @Entity 告訴 Room 這是一張資料表，可以自訂表名
@Entity(tableName = "crop_records")
data class CropRecord (
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val date: String,          // 日期(yyyy/mm/dd)
    val cropName: String? = "",     // 作物名稱
    val task: String? = "",       //工作內容
    val field: String? = "",       //工作場地
    val fertilizerName: String? = "", // 肥料名稱
    val amount: Double = 0.0,       // 用量
    val unit: String? = "kg",  // 單位
    val timestamp: Long = System.currentTimeMillis(), // 紀錄時間
    val isSynced: Boolean = false  //tag
)

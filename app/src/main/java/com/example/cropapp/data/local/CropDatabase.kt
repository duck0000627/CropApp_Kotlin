package com.example.cropapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cropapp.data.model.CropRecord

// 宣告這個資料庫包含哪些 Entity，以及目前的版本號
@Database(entities = [CropRecord::class], version = 1, exportSchema = false)
abstract class CropDatabase : RoomDatabase() {

    // 讓資料庫知道要提供哪個 DAO 給外面使用
    abstract fun cropDao(): CropDao

}
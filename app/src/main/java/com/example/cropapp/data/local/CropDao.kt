package com.example.cropapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cropapp.data.model.CropRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface CropDao {
    // 取得所有農作物紀錄，並依照時間倒序排列
    // 注意：回傳類型是 Flow，這代表只要資料庫有變動，UI 就會自動更新！類似 Flutter 的 Stream
    @Query("SELECT * FROM crop_records ORDER BY date DESC, timestamp DESC")
    fun getAllCrops(): Flow<List<CropRecord>>

    // 新增一筆紀錄
    // suspend 關鍵字代表這是一個非同步操作，必須在背景執行 (Coroutines)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrop(crop: CropRecord)

    @Update
    suspend fun updateCrop(crop: CropRecord)

    @Delete
    suspend fun deleteCrop(crop: CropRecord)
}
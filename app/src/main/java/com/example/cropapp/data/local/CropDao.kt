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
    @Query("SELECT * FROM crop_records WHERE isDeleted = 0 ORDER BY date DESC, timestamp DESC")
    fun getAllCrops(): Flow<List<CropRecord>>

    // 新增一筆紀錄
    // suspend 關鍵字代表這是一個非同步操作，必須在背景執行 (Coroutines)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrop(crop: CropRecord)

    @Update
    suspend fun updateCrop(crop: CropRecord)

    @Delete
    suspend fun trulyDeleteCrop(crop: CropRecord)

    // 🔍 找出所有還沒上傳的
    @Query("SELECT * FROM crop_records WHERE isSynced = 0 AND isDeleted = 0")
    suspend fun getUnsyncedCrops(): List<CropRecord>

    // 👻 找尋幽靈：抓出所有「已經被使用者刪除，但還沒通知雲端」的墓碑資料
    @Query("SELECT * FROM crop_records WHERE isDeleted = 1")
    suspend fun getPendingDeletes(): List<CropRecord>

    @Query("DELETE FROM crop_records WHERE isSynced = 1 AND isDeleted = 0")
    suspend fun deleteSyncedCrops()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCrops(crops: List<CropRecord>)
}
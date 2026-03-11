package com.example.cropapp.data.repository // 確認路徑是否正確

import com.example.cropapp.data.local.CropDao
import com.example.cropapp.data.model.CropRecord
import kotlinx.coroutines.flow.Flow

// 透過建構子把 Dao 傳進來 (未來這會交給 Hilt 依賴注入來處理)
class CropRepository(private val cropDao: CropDao) {

    // 取得所有農作物紀錄
    // Flow 會持續監聽資料庫，一旦資料庫有新增或刪除，這裡就會自動流出最新資料
    val allCrops: Flow<List<CropRecord>> = cropDao.getAllCrops()

    // 新增一筆農作物紀錄
    suspend fun insertCrop(crop: CropRecord) {
        cropDao.insertCrop(crop)
    }

    //刪除
    suspend fun deleteCrop(crop: CropRecord) {
        cropDao.deleteCrop(crop)
    }
}
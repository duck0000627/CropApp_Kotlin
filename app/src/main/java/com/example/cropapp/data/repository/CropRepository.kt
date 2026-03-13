package com.example.cropapp.data.repository // 確認路徑是否正確

import android.util.Log
import com.example.cropapp.data.local.CropDao
import com.example.cropapp.data.model.CropRecord
import com.example.cropapp.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow

// 透過建構子把 Dao 傳進來 (未來這會交給 Hilt 依賴注入來處理)
class CropRepository(private val cropDao: CropDao) {

    // 取得所有農作物紀錄
    // Flow 會持續監聽資料庫，一旦資料庫有新增或刪除，這裡就會自動流出最新資料
    val allCrops: Flow<List<CropRecord>> = cropDao.getAllCrops()

    // 新增一筆農作物紀錄
    suspend fun insertCrop(crop: CropRecord) {
        //先存入本地資料庫 (Room)，確保畫面能立刻更新
        cropDao.insertCrop(crop)

        //接著，在背景把這筆資料上傳給雲端
        try {
            RetrofitClient.apiService.uploadCropToNetwork(crop)
            cropDao.updateCrop(crop.copy(isSynced = true))
            Log.d("API_TEST", "成功上傳")
        } catch (e: Exception) {
            // 如果剛好經過沒有網路的農田，上傳失敗也沒關係，因為步驟 1 已經存在手機裡了
            Log.e("API_TEST", "雲端上傳失敗：${e.message}")
        }
    }

    //edit
    suspend fun updateCrop(crop: CropRecord) {
        cropDao.updateCrop(crop)

        try {
            RetrofitClient.apiService.updateCropToNetwork(crop.id, crop)
            Log.d("API", "雲端資料修改成功！")
        } catch (e: Exception) {
            Log.e("API", "雲端修改失敗")
        }
    }

    //刪除
    suspend fun deleteCrop(crop: CropRecord) {
        cropDao.deleteCrop(crop)

        try {
            RetrofitClient.apiService.deleteCropFromNetwork(crop.id)
            Log.d("API", "雲端資料刪除成功")
        } catch (e: Exception) {
            Log.e("API", "雲端刪除失敗")
        }
    }

    //資料同步 (Sync)
    suspend fun refreshCrops() {
        try {
            // 🛡️ 階段一：救援行動 (把斷網時累積的本地資料送上雲端)
            val unsyncedCrops = cropDao.getUnsyncedCrops()
            for (crop in unsyncedCrops) {
                try {
                    // 偷偷補上傳
                    RetrofitClient.apiService.uploadCropToNetwork(crop)
                    // 成功後，在本地把它標記成已同步
                    cropDao.updateCrop(crop.copy(isSynced = true))
                } catch (e: Exception) {
                    Log.e("API_SYNC", "這筆 ${crop.cropName} 還是傳不上去，下次再試。")
                }
            }

            // 📥 階段二：抓取雲端最新真相
            val cloudCrops = RetrofitClient.apiService.getAllCropsFromNetwork()

            // 🧹 階段三：安全清理 (只刪除本地已經同步過的舊資料，剛才傳失敗的不會被刪)
            cropDao.deleteSyncedCrops()

            // ✍️ 階段四：把雲端資料存入本地，並全面蓋上「已同步」的乖寶寶印章
            val fullySyncedCrops = cloudCrops.map { it.copy(isSynced = true) }
            cropDao.insertAllCrops(fullySyncedCrops)

            Log.d("API_SYNC", "企業級同步完成！")
        } catch (e: Exception) {
            Log.e("API_SYNC", "大斷網！繼續使用本地資料：${e.message}")
        }
    }
}
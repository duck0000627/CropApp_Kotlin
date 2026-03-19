package com.example.cropapp.data.remote

import com.example.cropapp.data.model.CropRecord
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CropApiService {
    @GET("api/crops")
    suspend fun getAllCropsFromNetwork(): List<CropRecord>

    @POST("api/crops")
    suspend fun uploadCropToNetwork(@Body crop: CropRecord): CropRecord

    // ☁️ 【新增】呼叫 C# 的智慧同步接口，一次傳送一整個 List
    @POST("api/Crops/sync")
    suspend fun syncCropsToNetwork(@Body crops: List<CropRecord>)

    //修改雲端資料的 PUT
    // 注意網址後面的 {id}，這是動態替換的變數
    @PUT("api/crops/{id}")
    suspend fun updateCropToNetwork(
        @Path("id") id: String,
        @Body crop: CropRecord
    )

    //刪除雲端資料的 DELETE
    @DELETE("api/crops/{id}")
    suspend fun deleteCropFromNetwork(
        @Path("id") id: String
    )

}
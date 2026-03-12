package com.example.cropapp.ui.crop_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cropapp.data.model.CropRecord
import com.example.cropapp.data.repository.CropRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CropListViewModel(private val repository: CropRepository) : ViewModel() {

    // 將 Repository 的 Flow 轉換成 UI 專用的 StateFlow
    // stateIn 會幫我們在背景處理資料流，並且提供一個初始值 (emptyList)
    val uiState: StateFlow<List<CropRecord>> = repository.allCrops
        .stateIn(
            scope = viewModelScope, // 綁定 ViewModel 的生命週期
            started = SharingStarted.WhileSubscribed(5000), // 當 UI 消失 5 秒後停止監聽，節省資源
            initialValue = emptyList() // 初始畫面為空清單
        )

    // 提供給 UI 呼叫的新增功能
    fun addCropRecord(
        date: String,
        cropName: String,
        field: String,
        task: String,
        fertilizerName: String,
        amount: Double
    ) {
        viewModelScope.launch {
            val newRecord = CropRecord(
                date = date,
                cropName = cropName,
                field = field,
                task = task,
                fertilizerName = fertilizerName,
                amount = amount
            )
            repository.insertCrop(newRecord)
        }
    }
    //刪除
    fun deleteCropRecord(crop: CropRecord) {
        viewModelScope.launch {
            repository.deleteCrop(crop)
        }
    }
}
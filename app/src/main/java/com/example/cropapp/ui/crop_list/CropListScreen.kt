package com.example.cropapp.ui.crop_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cropapp.data.model.CropRecord


@Composable
fun CropListScreen(viewModel: CropListViewModel) {
    // 監聽 ViewModel 的 StateFlow。
    // 只要資料庫有變更，這個 crops 變數就會自動取得最新清單，並觸發畫面重新渲染！
    val crops by viewModel.uiState.collectAsState()

    // 宣告一個變數，用來控制是否要顯示「新增表單對話框」
    // 預設為 false (不顯示)
    var showAddDialog by remember { mutableStateOf(false) }

    // Scaffold 提供了一個基本的畫面骨架，跟 Flutter 的 Scaffold 概念完全一模一樣！
    Scaffold(
        floatingActionButton = {
            // 右下角的懸浮按鈕
            FloatingActionButton(onClick = {
                showAddDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "新增紀錄")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues), // 佔滿整個螢幕
            contentPadding = PaddingValues(vertical = 8.dp), // 清單上下的內距
            verticalArrangement = Arrangement.spacedBy(4.dp) // 每張卡片之間的間距
        ) {
            // items() 是一個特殊的函式，用來把 List 轉換成一個個的 UI 元件
            items(crops) { record ->
                // 在這裡呼叫我們剛剛寫好的單張卡片
                CropItemCard(record = record)
            }
        }
        // 判斷是否要顯示對話框
        if (showAddDialog) {
            AddCropDialog(
                onDismiss = {
                    // 按下取消時，關閉對話框
                    showAddDialog = false
                },
                onConfirm = { inputCropName, inputFertilizer, inputAmount ->
                    // 按下儲存時：
                    // 1. 呼叫 ViewModel 寫入真實資料
                    viewModel.addCropRecord(
                        cropName = inputCropName,
                        fertilizerName = inputFertilizer,
                        amount = inputAmount
                    )
                    // 2. 關閉對話框
                    showAddDialog = false
                }
            )
        }
    }
}
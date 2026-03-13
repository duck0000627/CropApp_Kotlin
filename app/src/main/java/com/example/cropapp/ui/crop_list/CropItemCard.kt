package com.example.cropapp.ui.crop_list // 這裡要注意是否符合你的資料夾路徑

import android.R.attr.onClick
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cropapp.data.model.CropRecord // 記得匯入你剛剛寫的 Data Class

@Composable
fun CropItemCard(record: CropRecord, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{ onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp), // 設定外距 (Margin)
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // 設定陰影
    ) {
        // 卡片內部的垂直排版
        Column(
            modifier = Modifier
                .padding(16.dp) // 設定內距 (Padding)
        ) {
            // 顯示作物名稱
            Text(
                text = "作物：${record.cropName}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary // 使用主題的顏色
            )

            Spacer(modifier = Modifier.height(8.dp)) // 產生垂直間距

            Text(text = "田區：${record.field}")
            Text(text = "工作內容：${record.task}")

            // 顯示肥料與用量
            if (record.task == "施肥") {
                Text(text = "肥料：${record.fertilizerName}")
                Text(text = "用量：${record.amount} ${record.unit}")
            }
        }
    }
}

package com.example.cropapp.ui.crop_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddCropDialog(
    onDismiss: () -> Unit, // 當對話框關閉時觸發
    onConfirm: (String, String, Double) -> Unit // 當按下儲存時觸發，並把資料傳出去
) {
    // 這裡就像 Flutter 的 TextEditingController
    // remember { mutableStateOf("") } 會記住輸入框目前的文字
    var cropName by remember { mutableStateOf("") }
    var fertilizerName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("新增農作物紀錄") },
        text = {
            // Column 讓輸入框垂直排列
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // OutlinedTextField 是帶有外框的輸入框
                OutlinedTextField(
                    value = cropName,
                    onValueChange = { cropName = it }, // 每打一個字就更新變數
                    label = { Text("作物名稱 (如：高麗菜)") }
                )

                OutlinedTextField(
                    value = fertilizerName,
                    onValueChange = { fertilizerName = it },
                    label = { Text("肥料名稱 (如：氮肥)") }
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("用量 (kg)") },
                    // 將鍵盤限制為只能輸入數字 (這點跟 Flutter 很像！)
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // 將輸入的字串轉換為 Double (如果輸入的不是數字，預設為 0.0)
                    val amountDouble = amount.toDoubleOrNull() ?: 0.0
                    // 呼叫傳進來的 onConfirm 函式，把資料往外送
                    onConfirm(cropName, fertilizerName, amountDouble)
                }
            ) {
                Text("儲存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
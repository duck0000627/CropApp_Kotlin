package com.example.cropapp.ui.crop_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCropDialog(
    onDismiss: () -> Unit, // 當對話框關閉時觸發
    onConfirm: (String, String, String, String, String, Double) -> Unit // 當按下儲存時觸發，並把資料傳出去
) {
    var date by remember { mutableStateOf(SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(
        Date()
    )) }
    var showDatePicker by remember { mutableStateOf(false) }
    // 預設選項設定
    val cropOptions = listOf("黑豆","黃豆")
    val fieldOptions = listOf("A1","A2")
    val workOptions = listOf("播種", "施肥", "澆水")

    // 儲存使用者目前選到的值 (預設先選清單的第一個)
    var cropName by remember { mutableStateOf(cropOptions[0]) }
    var field by remember { mutableStateOf(fieldOptions[0]) }
    var task by remember { mutableStateOf(workOptions[0]) }

    // 文字輸入框的狀態
    var fertilizerName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("新增工作紀錄") },
        text = {
            // Column 讓輸入框垂直排列
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                OutlinedTextField(
                    value = date,
                    onValueChange = {},
                    label = {Text("工作日期")},
                    readOnly = true,    // 不讓使用者用鍵盤打字
                    trailingIcon = {
                        IconButton(onClick = {showDatePicker = true}) {
                            Icon(Icons.Default.DateRange, contentDescription = "選擇日期")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                // 使用我們自己包裝的下拉選單元件
                DropdownSelector(
                    label = "作物名稱",
                    options = cropOptions,
                    selectedOption = cropName,
                    onOptionSelected = { cropName = it }
                )

                DropdownSelector(
                    label = "田區代號",
                    options = fieldOptions,
                    selectedOption = field,
                    onOptionSelected = { field = it }
                )

                DropdownSelector(
                    label = "工作內容",
                    options = workOptions,
                    selectedOption = task,
                    onOptionSelected = { task = it }
                )

                if (task == "施肥") {
                    OutlinedTextField(
                        value = fertilizerName,
                        onValueChange = { fertilizerName = it },
                        label = { Text("肥料名稱 (如：氮肥)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("用量 (kg)") },
                        // 將鍵盤限制為只能輸入數字 (這點跟 Flutter 很像！)
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // 將輸入的字串轉換為 Double (如果輸入的不是數字，預設為 0.0)
                    val amountDouble = amount.toDoubleOrNull() ?: 0.0
                    // 呼叫傳進來的 onConfirm 函式，把資料往外送
                    onConfirm(date, cropName, field, task, fertilizerName, amountDouble)
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

    if (showDatePicker){
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        date = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(millis))
                        showDatePicker = false }
                }) {Text("確定") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("取消")
                }
            }){
            DatePicker(state = datePickerState)
        }
    }
}

// 獨立出來的共用下拉選單元件，讓上面的程式碼更乾淨
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true, // 設為唯讀，讓它表現得像個按鈕而不是輸入框
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
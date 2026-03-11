package com.example.cropapp

import com.example.cropapp.data.local.CropDatabase
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.cropapp.data.repository.CropRepository
import com.example.cropapp.ui.crop_list.CropListScreen
import com.example.cropapp.ui.crop_list.CropListViewModel
import com.example.cropapp.ui.theme.CropAppTheme
import kotlin.jvm.java

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. 建立 Room 資料庫實體
        val database = Room.databaseBuilder(
            applicationContext,
            CropDatabase::class.java,
            "crop_database" // 資料庫的檔案名稱
        ).build()

        // 2. 建立 Repository，並把 Dao 傳進去
        val repository = CropRepository(database.cropDao())

        // 3. 建立 ViewModel 的「製造工廠 (Factory)」
        // 因為我們的 ViewModel 需要 repository 作為參數，所以必須透過 Factory 來產生
        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CropListViewModel(repository) as T
            }
        }

        // 4. 取得 ViewModel 實體
        val viewModel: CropListViewModel by viewModels { viewModelFactory }

        // 5. 畫出 UI 畫面
        setContent {
            // 這裡的主題名稱可能會依據你一開始建立專案時的命名而有所不同
            // 如果顯示紅字，請改成你專案中實際的 Theme 名稱
            CropAppTheme {
                // 將準備好的 viewModel 傳給畫面！
                CropListScreen(viewModel = viewModel)
            }
        }
    }
}
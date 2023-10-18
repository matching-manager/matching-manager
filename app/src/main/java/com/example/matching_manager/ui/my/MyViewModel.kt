package com.example.matching_manager.ui.my

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicLong

class MyViewModel(private val repository: MyMatchRepository) : ViewModel() {

    private val _list: MutableLiveData<List<MyMatchDataModel>> = MutableLiveData()
    val list: LiveData<List<MyMatchDataModel>> get() = _list


    suspend fun fetchData() {

        val currentList = withContext(Dispatchers.IO) {
            repository.getList()
        }
        _list.value = currentList

    }


    private val _profileImageUri = MutableLiveData<Uri?>() // 프로필 이미지 Uri를 저장하는 LiveData

    val profileImageUri : LiveData<Uri?> get() = _profileImageUri // 외부에서 프로필 이미지 Uri 접근하는 LiveData

    //live = 액티비티나 프래그먼트에서 읽기 전용
    //_live = 뷰모델 내부에서 컨트럴 전용
    // id 를 부여할 값
    private val idGenerator = AtomicLong(1L)

//    fun setData(data: ArrayList<Image>) {
//        _list.value = data
//    }

//    fun setProfile(name: String, imageUri: Uri?){
//        _profileName.value = name
//        _profileImageUri.value = imageUri
//    }

}


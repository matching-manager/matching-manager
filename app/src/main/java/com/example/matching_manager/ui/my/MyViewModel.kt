package com.example.matching_manager.ui.my

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicLong

class MyViewModel(private val repository: MyMatchRepository) : ViewModel() {

    private val _list: MutableLiveData<List<MyMatchDataModel>> = MutableLiveData()
    val list: LiveData<List<MyMatchDataModel>> get() = _list

    private val _userId: MutableLiveData<String> = MutableLiveData()
    val userId : LiveData<String> get() = _userId

    private val _matchId: MutableLiveData<String> = MutableLiveData()
    val matchId : LiveData<String> get() = _userId

    private val _event: MutableLiveData<MatchEvent> = MutableLiveData()
    val event: LiveData<MatchEvent> get() = _event



    fun fetchData() {
        viewModelScope.launch {
            val currentList = repository.getList()
            Log.d("MatchViewModel", "fetchData() = currentList : ${currentList.size}")

            _list.postValue(currentList)
        }
    }

    fun deleteMatch(data: MyMatchDataModel) {
        viewModelScope.launch {
            repository.deleteData(data)
            _event.postValue(MatchEvent.Dismiss)
        }
    }

    fun editMatch(data: MyMatchDataModel, newData : MyMatchDataModel) {
        viewModelScope.launch {
            repository.editData(data, newData)
            _event.postValue(MatchEvent.Finish)
        }
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
sealed interface MatchEvent {
    object Dismiss : MatchEvent
    object Finish : MatchEvent
}


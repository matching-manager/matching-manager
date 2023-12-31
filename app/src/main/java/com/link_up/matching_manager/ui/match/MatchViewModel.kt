package com.link_up.matching_manager.ui.match

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.link_up.matching_manager.domain.usecase.match.MatchAddDataUseCase
import com.link_up.matching_manager.domain.usecase.match.MatchAutoGetListUseCase
import com.link_up.matching_manager.domain.usecase.match.MatchEditChatCountUseCase
import com.link_up.matching_manager.domain.usecase.match.MatchEditViewCountUseCase
import com.link_up.matching_manager.domain.usecase.match.MatchGetListUseCase
import com.link_up.matching_manager.util.UserInformation
import kotlinx.coroutines.launch

class MatchViewModel(
    val addData: MatchAddDataUseCase,
    val getList: MatchGetListUseCase,
    val editChatCount: MatchEditChatCountUseCase,
    val editViewCount: MatchEditViewCountUseCase,
    val autoGetList : MatchAutoGetListUseCase
) : ViewModel() {

    private var originalList: MutableList<MatchDataModel> = mutableListOf() // 원본 데이터를 보관할 리스트

    private val _list: MutableLiveData<List<MatchDataModel>> = MutableLiveData()
    val list: LiveData<List<MatchDataModel>> get() = _list

    private val _realTimeList: MutableLiveData<List<MatchDataModel>> = MutableLiveData()
    val realTimeList: LiveData<List<MatchDataModel>> get() = _realTimeList

    private val _event: MutableLiveData<MatchEvent> = MutableLiveData()
    val event: LiveData<MatchEvent> get() = _event

    private val database =
        Firebase.database("https://matching-manager-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val matchRef = database.getReference("Match")

    fun fetchData() {
        getList(matchRef, originalList, _list)
    }

    fun addMatch(data: MatchDataModel) {
        viewModelScope.launch {
            addData(matchRef, data)
            _event.postValue(MatchEvent.Finish)
        }
    }

    fun filterItems(area: String?, game: String?) {
        if ("선택" in game.orEmpty() && "선택" in area.orEmpty()) {
            // 게임과 지역이 선택되지 않았을 경우, 필터링을 하지 않고 모든 아이템을 보여줍니다.
            _list.value = originalList
            return
        }
        val filteredList = originalList.filter { item ->
            // 선택된 게임 또는 지역이 있을 경우, 해당 조건에 맞는 아이템만 필터링합니다.
            val isGameMatched = game.isNullOrBlank() || (item.game.contains(
                game,
                ignoreCase = true
            ))
            val isAreaMatched = area.isNullOrBlank() || (item.matchPlace.contains(
                area,
                ignoreCase = true
            ))
            if (area!!.contains("선택")) {
                isGameMatched
            } else if (game!!.contains("선택")) {
                isAreaMatched
            } else {
                //둘 다 체크되었을때
                isGameMatched && isAreaMatched
            }
        }
        _list.value = filteredList
    }

    fun autoFetchData() {
        autoGetList(matchRef, null,  _realTimeList)
    }

    fun plusViewCount(data: MatchDataModel) {
        val query = matchRef.orderByChild("matchId").equalTo(data.matchId)
        //게시물에 담긴 유저ID와 로그인한 유저의 UID가 달라야 countUp
        if(data.userId != UserInformation.userInfo.uid) {
            viewModelScope.launch {
                editViewCount(query, data)
            }
        }
    }

    fun plusChatCount(data: MatchDataModel) {
        val query = matchRef.orderByChild("matchId").equalTo(data.matchId)
        //게시물에 담긴 유저ID와 로그인한 유저의 UID가 달라야 countUp
        if(data.userId != UserInformation.userInfo.uid) {
            viewModelScope.launch {
                editChatCount(query, data)
            }
        }
    }

    sealed interface MatchEvent {
        object Finish : MatchEvent
    }
}
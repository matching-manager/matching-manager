package com.example.matching_manager.ui.home.arena

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matching_manager.domain.model.ArenaEntity
import com.example.matching_manager.domain.model.DocumentsEntity
import com.example.matching_manager.domain.usecase.GetArenaInfoUseCase
import kotlinx.coroutines.launch

class ArenaViewModel(
    private val arenaInfo: GetArenaInfoUseCase
) : ViewModel() {

    private val _list: MutableLiveData<List<ArenaModel>> = MutableLiveData()
    val list: LiveData<List<ArenaModel>> get() = _list

    fun getArenaInfo(
        query: String,
        x: String,
        y: Int,
        radius: Int,
        page: Int,
        size: Int,
        sort: Int,
    ) {
        viewModelScope.launch {
            kotlin.runCatching {
                _list.value = createItem(arenaInfo(query, x, y, radius, page, size, sort))
            }.onFailure {
                // Internet error ...
                // 동작 추가 예정
            }
        }
    }

    private fun createItem(arenaInfo: ArenaEntity): List<ArenaModel> {


        fun createItems(
            arenaInfo: ArenaEntity
        ) : List<ArenaModel> = arenaInfo.documents?.map { document ->
            ArenaModel(
                placeName = document.placeName,
                phone = document.phone,
                addressName = document.addressName, // 지번 주소
                roadAddressName = document.roadAddressName, // 도로명 주소
                placeUrl = document.placeUrl, // 장소 상세페이지 URL
                distance = document.distance // 중심좌표까지의 거리 (단, x,y 파라미터를 준 경우에만 존재) (단위 meter)
            )
        }.orEmpty()

        val items = arrayListOf<ArenaModel>().apply {
            addAll(createItems(arenaInfo))
        }

        // sort : 거리순
        items.sortByDescending { it.distance }

        return items
    }


}
package com.link_up.matching_manager.ui.home.arena

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.link_up.matching_manager.domain.model.ArenaEntity
import com.link_up.matching_manager.domain.usecase.api.GetArenaInfoUseCase
import com.link_up.matching_manager.domain.usecase.geo.GetGeoLocationUseCase
import kotlinx.coroutines.launch

class ArenaViewModel(
    private val arenaInfo: GetArenaInfoUseCase,
    private val getGeoLocation: GetGeoLocationUseCase
) : ViewModel() {

    private val _list: MutableLiveData<List<ArenaModel>> = MutableLiveData()
    val list: LiveData<List<ArenaModel>> get() = _list


    private val _filterArea :MutableLiveData<String?> = MutableLiveData()
    val filterArea : MutableLiveData<String?> get() =  _filterArea


    private val _item: MutableLiveData<ArenaModel> = MutableLiveData()
    val item: LiveData<ArenaModel> get() = _item

    private val _filter = MutableLiveData<String>() //지역
    val filter: LiveData<String> get() = _filter


    init {
        _filterArea.value = null
    }
    fun setFilterArea(area : String){
        _filterArea.value = area
    }

    fun updateFilter(area: String) {
        _filter.value = area
    }


    fun updateItem(item: ArenaModel) {
        _item.value = item
    }


    fun searchArena(query: String, context: Context) {

        filterArea.value?.let {
            getGeoLocation(
                address = it,
                context = context
            ).apply {
                val placeLatitude = latitude.toString()
                val placeLongitude = longitude.toString()
                getArenaInfo(
                    query = query,
                    x = placeLongitude,
                    y = placeLatitude
    //            radius = 1000,
    //            page = 1,
    //            size = 10,
    //            sort = "distance"
                )
            }
        }
    }

    private fun getArenaInfo(
        query: String,
        x: String,
        y: String
//        radius: Int,
//        page: Int,
//        size: Int,
//        sort: String,
    ) {
        Log.d("test", "getArenaInfo")
        viewModelScope.launch {
            kotlin.runCatching {
                _list.value = createItem(arenaInfo(query, x, y))
            }.onFailure {
                Log.d("test", "fail")
                // Internet error ...
                // 동작 추가 예정
            }
        }
    }

    private fun createItem(arenaInfo: ArenaEntity): List<ArenaModel> {

        fun createItems(
            arenaInfo: ArenaEntity
        ): List<ArenaModel> = arenaInfo.documents?.map { document ->
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
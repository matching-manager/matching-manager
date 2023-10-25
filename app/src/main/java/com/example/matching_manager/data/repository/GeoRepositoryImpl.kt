package com.example.matching_manager.data.repository

import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.example.matching_manager.domain.repository.GeoRepository
import java.util.Locale

class GeoRepositoryImpl : GeoRepository {


    // 주소로 위도,경도 구하는 GeoCoding
    // getFromLocationName()을 이용해 Location 객체로 위도 경도를 return하는 함수
    // 인자로 장소( string )와 maxResult( 결과의 최대 개수 int )로 구성, 따라서 사용자가 입력한 주소 str과 maxResult(int)를 인자로 넣어준다.
    override fun geoCoding(address: String, context : Context): Location {
        return try {
            Geocoder(context, Locale.KOREA).getFromLocationName(address, 1)?.let{
                Location("").apply {
                    latitude =  it[0].latitude
                    longitude = it[0].longitude
                }
            }?: Location("").apply {
                latitude = 0.0
                longitude = 0.0
            }
        }catch (e:Exception) {
            e.printStackTrace()
            geoCoding(address,context) //재시도
        }
    }

}
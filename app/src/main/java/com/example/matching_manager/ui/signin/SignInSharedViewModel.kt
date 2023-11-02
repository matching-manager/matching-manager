package com.example.matching_manager.ui.signin

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matching_manager.data.model.UserInfoModel
import com.example.matching_manager.domain.usecase.database.AddUserInfoToDataBaseUseCase
import com.example.matching_manager.domain.usecase.database.CheckUserInfoToDataBaseUseCase
import com.example.matching_manager.domain.usecase.sharedpreference.CheckUserTypeUseCase
import com.example.matching_manager.domain.usecase.sharedpreference.SaveUserTypeUseCase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SignInSharedViewModel(
    private val addUserInformation: AddUserInfoToDataBaseUseCase,
    private val checkUserInformation: CheckUserInfoToDataBaseUseCase,
    private val saveUserType: SaveUserTypeUseCase,
    private val checkUserType: CheckUserTypeUseCase,
    private val context: Context
) : ViewModel() {

    private val _userType: MutableLiveData<String> = MutableLiveData()
    val userType: LiveData<String> get() = _userType

    private val _signInUserType : MutableLiveData<Boolean> = MutableLiveData()
    val signInUserType : LiveData<Boolean> get() = _signInUserType


    private val database =
        Firebase.database("https://matching-manager-default-rtdb.asia-southeast1.firebasedatabase.app/")

    fun checkUserinfo(userInfoModel: UserInfoModel) {
        viewModelScope.launch {
            when(checkUserInformation(userInfoModel, database)){
                true ->{ //기존 유저
                    _userType.value = CheckUserType.EXISTING_USER.name
                }
                false ->{
                    _userType.value = CheckUserType.NEW_USER.name
                }
            }
        }
    }

    fun addUserInfo(userName: String, userPhoneNumber: String) {
        val userdata = UserInformation.userInfo
        viewModelScope.launch {
            addUserInformation(UserInfoModel(
                uid = userdata.uid,
                uidToken = userdata.uidToken,
                email = userdata.email,
                fcmToken = userdata.fcmToken,
                photoUrl = userdata.photoUrl,
                username = userName,
                phoneNUmber = userPhoneNumber
            ), database)
        }
    }


    fun saveUserType(){
        saveUserType(context = context,type = true)
    }
    fun checkUserType(){
        _signInUserType.value = checkUserType(context=context)
    }




}
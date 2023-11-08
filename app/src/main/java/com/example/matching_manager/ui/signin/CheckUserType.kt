package com.example.matching_manager.ui.signin

enum class CheckUserType {
    EXISTING_USER, NEW_USER;

    companion object {
        fun from(name: String?): CheckUserType? {
            return CheckUserType.values().find {
                it.name.uppercase() == name?.uppercase()
            }
        }
    }
}
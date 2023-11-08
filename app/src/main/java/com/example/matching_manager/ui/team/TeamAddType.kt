package com.example.matching_manager.ui.team

enum class TeamAddType {
    RECRUIT, APPLICATION;

    companion object {
        fun from(name: String?): TeamAddType? {
            return TeamAddType.values().find {
                it.name.uppercase() == name?.uppercase()
            }
        }
    }
}
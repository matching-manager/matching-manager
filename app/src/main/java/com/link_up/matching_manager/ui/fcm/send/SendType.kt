package com.link_up.matching_manager.ui.fcm.send

import com.link_up.matching_manager.ui.team.TeamAddType

enum class SendType {
    MATCH, MERCENARY;

    companion object {
        fun from(name: String?): SendType? {
            return SendType.values().find {
                it.name.uppercase() == name?.uppercase()
            }
        }
    }
}
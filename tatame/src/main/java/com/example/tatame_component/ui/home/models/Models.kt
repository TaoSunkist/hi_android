package com.example.tatame_component.ui.home.models

import com.example.tatame_component.ui.home.adapters.IdolsCommonTabEntity
import com.flyco.tablayout.listener.CustomTabEntity
import com.mooveit.library.Fakeit

data class IdolsViewPagerUIModel(val name: String, val tabEntities: ArrayList<CustomTabEntity>) {
    companion object {
        fun fake(): IdolsViewPagerUIModel {
            val titles = arrayOf("我的关注", "全部爱豆")
            val tabEntities = ArrayList<CustomTabEntity>().apply {
                titles.forEach { title ->
                    add(IdolsCommonTabEntity(
                            title = title,
                            selectedIcon = 0,
                            unSelectedIcon = 0)
                    )
                }
            }
            return IdolsViewPagerUIModel(
                    name = Fakeit.ancient().titan(),
                    tabEntities = tabEntities
            )
        }
    }
}

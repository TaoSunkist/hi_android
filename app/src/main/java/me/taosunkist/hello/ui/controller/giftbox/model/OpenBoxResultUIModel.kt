package me.taosunkist.hello.ui.controller.giftbox.model

import com.mooveit.library.Fakeit
import me.taosunkist.hello.R

data class OpenBoxResultUIModel(val prize: Int, val message: String) {


	companion object {
		fun fake(): OpenBoxResultUIModel = OpenBoxResultUIModel(
			message = Fakeit.book().title(),
			prize = (10000..100000).random()
		)
	}

	fun getPrizePromptIconResID(): Int = R.drawable.ic_gold_coin_many
}
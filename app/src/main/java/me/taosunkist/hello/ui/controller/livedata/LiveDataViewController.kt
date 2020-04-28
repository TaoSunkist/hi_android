package me.taosunkist.hello.ui.controller.livedata

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import com.mooveit.library.Fakeit
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.ViewControllerLiveDataBinding
import me.taosunkist.hello.ui.reusable.viewcontroller.controller.BaseViewController

data class LiveDataUIModel(val name: String) {
	companion object {
		fun fake(): LiveDataUIModel {
			return LiveDataUIModel(name = Fakeit.book().title())
		}
	}
}

class LiveDataViewController : BaseViewController() {

	companion object {
		const val TAG = "livedata"
	}

	private lateinit var binding: ViewControllerLiveDataBinding
	private val liveData: LiveData<LiveDataUIModel> = MutableLiveData()

	init {
		presentationStyle.apply {
			tag = TAG
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
		binding = DataBindingUtil.inflate(inflater, R.layout.view_controller_live_data, container, false)
		return binding.also {
			/* 观察者模式 */
			liveData.observe(this, Observer {
				println(it.name)
			})
		}.root
	}
}
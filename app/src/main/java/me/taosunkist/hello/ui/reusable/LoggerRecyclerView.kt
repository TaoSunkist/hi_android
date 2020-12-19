package me.taosunkist.hello.ui.reusable

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.taosunkist.hello.utility.Dimens
import me.taosunkist.hello.utility.Dimens.marginSmall
import java.util.*

class LoggerRecyclerView : RecyclerView {
	var maxItems = 10
	private val mAdapter = LogRecyclerViewAdapter()
	private val mDecoration = MarginDecoration()
	private var mInflater: LayoutInflater? = null
	private val mLogList: MutableList<LogItem> = ArrayList()
	private var mVerticalSpacing = 0

	constructor(context: Context) : super(context) {
		init(context)
	}

	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
		init(context)
	}

	constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
		init(context)
	}

	private fun init(context: Context) {
		mInflater = LayoutInflater.from(context)
		adapter = mAdapter
		setLayout(context)
	}

	private fun setLayout(context: Context) {
		val manager = LinearLayoutManager(context)
		manager.orientation = LinearLayoutManager.VERTICAL
		layoutManager = manager
		addItemDecoration(mDecoration)
		mVerticalSpacing = getOffset()
	}

	private fun getOffset(): Int {
		return marginSmall
	}

	override fun getAdapter(): LogRecyclerViewAdapter? {
		return mAdapter
	}

	fun logI(text: String?) {
		mAdapter.logI(text)
	}

	fun logW(text: String?) {
		mAdapter.logW(text)
	}

	fun logE(text: String?) {
		mAdapter.logE(text)
	}

	enum class LogLevel {
		ERROR, WARN, INFO
	}

	class LogItem internal constructor(var level: LogLevel, var text: String?) {
		@ColorInt
		var textColor = 0

	}

	inner class LogRecyclerViewAdapter :
		Adapter<ViewHolder>() {
		override fun onCreateViewHolder(
			viewGroup: ViewGroup,
			position: Int
		): ViewHolder {
			val view = AppCompatTextView(viewGroup.context).apply {
				layoutParams = ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
				textSize = Dimens.fontSizeSmall
			}
			return ViewHolder(view)
		}

		override fun onBindViewHolder(
			viewHolder: ViewHolder,
			position: Int
		) {
			viewHolder.update(mLogList[position])
		}

		override fun getItemCount(): Int {
			return mLogList.size
		}

		fun logI(text: String?) {
			addLogItem(LogLevel.INFO, text)
		}

		fun logW(text: String?) {
			addLogItem(LogLevel.WARN, text)
		}

		fun logE(text: String?) {
			addLogItem(LogLevel.ERROR, text)
		}

		private fun addLogItem(level: LogLevel, text: String?) {
			val item = LogItem(level, text)
			if (mLogList.size == maxItems) {
				mLogList.removeAt(0)
			}
			mLogList.add(item)
			scrollToPosition(mLogList.size - 1)
			notifyDataSetChanged()
		}
	}

	inner class ViewHolder internal constructor(val text: AppCompatTextView) : RecyclerView.ViewHolder(text) {

		fun update(item: LogItem) {
			when (item.level) {
				LogLevel.INFO -> item.textColor = Color.GREEN
				LogLevel.WARN -> item.textColor = Color.YELLOW
				LogLevel.ERROR -> item.textColor = Color.RED
			}
			text.text = item.text
			text.setTextColor(item.textColor)
		}

	}

	private inner class MarginDecoration : ItemDecoration() {
		override fun getItemOffsets(
			outRect: Rect,
			view: View,
			parent: RecyclerView,
			state: State
		) {
			outRect.top = mVerticalSpacing
			outRect.bottom = mVerticalSpacing
		}
	}
}
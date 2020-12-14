package top.thsunkist.brainhealthy.ui.reusable.views.purchaseable

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import top.thsunkist.brainhealthy.R
import top.thsunkist.brainhealthy.ui.reusable.uimodels.PurchaseableItemUIModel
import top.thsunkist.brainhealthy.ui.reusable.views.BorderedImageView
import top.thsunkist.brainhealthy.utilities.animation.GiftAnimationUtility
import top.thsunkist.brainhealthy.utilities.getDrawable
import top.thsunkist.brainhealthy.utilities.view.bind
import top.thsunkist.brainhealthy.utilities.view.setDrawableLeft

class PurchaseableItemView(context: Context) : FrameLayout(context) {

	private var contentView: View = View.inflate(context, R.layout.cell_purchaseable_item, this)

	private var itemImageView: AppCompatImageView =
		contentView.findViewById(R.id.cell_purchaseable_item_imageview)
	private var selectImageView: BorderedImageView =
		contentView.findViewById(R.id.cell_purchaseable_item_select_imageview)
	private var tagTextView: AppCompatTextView =
		contentView.findViewById(R.id.cell_purchaseable_item_tag_title_textview)

	private var nameTextView: AppCompatTextView =
		contentView.findViewById(R.id.cell_purchaseable_item_name_textview)

	fun setDimension(width: Int, height: Int) {
		layoutParams = LayoutParams(width, height)
	}

	fun bind(model: PurchaseableItemUIModel) {

		itemImageView.bind(model.imageUIModel)
		nameTextView.text = model.itemName
		nameTextView.setTextColor(getColor(context, R.color.textColorLightGray))
		tagTextView.text = model.tagName
		tagTextView.setTextColor(getColor(context, R.color.textColorLightGray))

		model.nameStartDrawableResId?.let {
			nameTextView.setDrawableLeft(getDrawable(contentView.resources, it, 0))
		}
		model.tagTitleStartDrawableResid?.let {
			tagTextView.setDrawableLeft(getDrawable(contentView.resources, it, 0))
		}
		selectImageView.borderColor = Color.TRANSPARENT

		if (model.isSelected) {
			GiftAnimationUtility.giftSelect(itemImageView)
			selectImageView.alpha = 1f
			selectImageView.setBackgroundResource(R.drawable.bg_gift_selected_box)
		} else {
			selectImageView.alpha = 0f
		}
	}
}

class PurchaseableItemViewHolder(context: Context) :
	RecyclerView.ViewHolder(PurchaseableItemView(context)) {

	fun bind(model: PurchaseableItemUIModel) {
		(itemView as PurchaseableItemView).bind(model)
	}

	fun setDimension(width: Int, height: Int) {
		(itemView as? PurchaseableItemView)?.setDimension(width = width, height = height)
	}
}
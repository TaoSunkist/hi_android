package top.thsunkist.brainhealthy.ui.reusable.viewcontroller.controller

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import top.thsunkist.brainhealthy.R
import top.thsunkist.brainhealthy.ui.reusable.uimodels.PurchaseableItemUIModel
import top.thsunkist.brainhealthy.ui.reusable.viewcontroller.presentation.PresentingAnimation
import top.thsunkist.brainhealthy.ui.reusable.views.PageIndicator
import top.thsunkist.brainhealthy.ui.reusable.views.purchaseable.PurchaseableItemViewHolder
import top.thsunkist.brainhealthy.ui.reusable.views.recyclerview.PagerGridLayoutManager
import top.thsunkist.brainhealthy.ui.reusable.views.recyclerview.PagerGridSnapHelper
import top.thsunkist.brainhealthy.utilities.ColorUtility
import top.thsunkist.brainhealthy.utilities.view.Dimens
import top.thsunkist.brainhealthy.utilities.weak
import kotlin.math.ceil

private val cellWidth = Dimens.screenWidth / 4
private val cellHeight = Dimens.purchaseableItemHeight + Dimens.marginMedium

private interface OnItemSelectedListener {
    fun onItemSelected(position: Int)
}

private class GalleryAdapter(delegate: OnItemSelectedListener) :
    RecyclerView.Adapter<PurchaseableItemViewHolder>() {

    var selectedPosition = -1
        set(newPosition) {
            /* 因为layout manager的bug，所以只能notify data set changed */
//            val oldSelected = field
            field = newPosition
//            if (oldSelected > -1) {
//                notifyItemChanged(oldSelected)
//            }
            notifyDataSetChanged()
//            notifyItemChanged(newPosition)
        }
    var models = arrayListOf<PurchaseableItemUIModel>()
    var delegate: OnItemSelectedListener? by weak()

    init {
        this.delegate = delegate
    }

    fun setItems(items: List<PurchaseableItemUIModel>) {
        models.clear()
        models.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseableItemViewHolder {
        val holder = PurchaseableItemViewHolder(parent.context)
        holder.setDimension(cellWidth, cellHeight)
        holder.itemView.setOnClickListener {
            val position = it.getTag(R.id.id_item_position) as? Int
            if (position != null) {
                delegate?.onItemSelected(position)
            }
        }
        return holder
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun onBindViewHolder(holder: PurchaseableItemViewHolder, position: Int) {
        val model = models[position]
        model.isSelected = selectedPosition == position

        holder.bind(model = model)
        holder.itemView.setTag(R.id.id_item_position, position)
    }
}

open class PaginatedGalleryDialogViewController : BaseStyledDialogViewController(),
    OnItemSelectedListener {

    protected lateinit var progressBar: ProgressBar

    private lateinit var gridLayoutManager: PagerGridLayoutManager

    private lateinit var recyclerView: RecyclerView

    private lateinit var galleryAdapter: GalleryAdapter

    private lateinit var pageIndicator: PageIndicator

    open val rowCount: Int
        get() = 1

    open val columnCount: Int
        get() = 4

    init {
        presentationStyle.animation = PresentingAnimation.BOTTOM_FADE
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View {
        gridLayoutManager = PagerGridLayoutManager(
            rowCount,
            columnCount,
            PagerGridLayoutManager.HORIZONTAL
        )

        recyclerView = RecyclerView(requireContext).apply {
            itemAnimator?.changeDuration = 0
            layoutManager = gridLayoutManager
            layoutParams = LinearLayout.LayoutParams(
                cellWidth * columnCount,
                cellHeight * rowCount
            )
            val snapHelper = PagerGridSnapHelper()
            snapHelper.attachToRecyclerView(this)
        }

        recyclerView.layoutDirection = View.LAYOUT_DIRECTION_LOCALE

        pageIndicator = PageIndicator(requireContext).apply {
            setDotSize(Dimens.dpToPx(4))
            setDotMargin(Dimens.dpToPx(4))
            setDefaultDotColor(Color.parseColor("#E8E0FC"))
            setSelectedDotColor(ColorUtility.primaryColor)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Dimens.dpToPx(Dimens.marginSmall)
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }
            gravity = Gravity.CENTER_HORIZONTAL
        }

        galleryAdapter = GalleryAdapter(this)
        recyclerView.adapter = galleryAdapter

        /* Hook up with page indicator */
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var scrollPosition: Float = 0f
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollPosition += dx
                val width = recyclerView.measuredWidth
                val pageIndex = scrollPosition / width
                val progress = (scrollPosition % width) / width.toFloat()
                pageIndicator.onPageScrolled(pageIndex.toInt(), progress)
            }
        })

        return LinearLayout(container.context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            addView(recyclerView)
            addView(pageIndicator)
        }
    }

    fun setItems(items: List<PurchaseableItemUIModel>) {
        galleryAdapter.setItems(items)
        galleryAdapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(0)
        val pageCount = ceil(galleryAdapter.itemCount.toDouble() / (columnCount * rowCount)).toInt()
        pageIndicator.setPageCount(pageCount)
    }

    override fun viewDidLoad(view: View) {
        super.viewDidLoad(view)

        progressBar = ProgressBar(view.context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).also {
                it.gravity = Gravity.CENTER
            }
            isIndeterminate = true
            binding.commonIdContentContainer.addView(this)
        }

        val lp = binding.dialogStyledOverallFramelayout.layoutParams
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.dialogStyledOverallFramelayout.layoutParams = lp
    }

    override fun onItemSelected(position: Int) {
        galleryAdapter.selectedPosition = position
    }
}


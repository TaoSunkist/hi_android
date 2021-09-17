package me.taosunkist.hello.ui.testrecyclerview

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.taosunkist.hello.databinding.FragmentTestRecyclerViewListBinding
import me.taosunkist.hello.ui.BaseFragment
import me.taosunkist.hello.ui.testrecyclerview.placeholder.PlaceholderContent
import me.taosunkist.hello.utility.printf

class TestRecyclerViewFragment : BaseFragment() {

    private var _binding: FragmentTestRecyclerViewListBinding? = null

    private val binding: FragmentTestRecyclerViewListBinding
        get() = _binding!!

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentTestRecyclerViewListBinding.inflate(inflater, container, false).also { _binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.testRecyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = TestRecyclerViewAdapter(PlaceholderContent.ITEM_UI_MODELS)

            binding.refreshListButton.setOnClickListener {
                printf("taohui notifyItemRangeChanged(${0}, ${adapter?.itemCount ?: 0})")
                adapter?.notifyItemRangeChanged(0, adapter?.itemCount ?: 0)
                /*adapter?.notifyDataSetChanged()*/
            }
        }
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            TestRecyclerViewFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
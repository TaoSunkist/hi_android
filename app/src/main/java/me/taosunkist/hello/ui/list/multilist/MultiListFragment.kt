package me.taosunkist.hello.ui.list.multilist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.taosunkist.hello.databinding.FragmentMultiListBinding
import me.taosunkist.hello.ui.list.multilist.itemlistviewwrapper.HeadItemListViewWrapper
import me.taosunkist.hello.ui.list.multilist.itemlistviewwrapper.BodyItemListViewWrapper
import me.taosunkist.hello.ui.reusable.itemlistviewwrapper.ItemListViewWrapperDelegate

class MultiListFragment : Fragment(), ItemListViewWrapperDelegate<CellUIModel> {

    lateinit var bodyItemListViewWrapper: BodyItemListViewWrapper

    lateinit var headItemItemListViewWrapper: HeadItemListViewWrapper

    private var _binding: FragmentMultiListBinding? = null

    private val binding: FragmentMultiListBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentMultiListBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        headItemItemListViewWrapper = HeadItemListViewWrapper(binding.likeEachOthersContainer).apply {

        }

        bodyItemListViewWrapper = BodyItemListViewWrapper(view).apply {
            delegate = this@MultiListFragment
        }

        binding.extendedFloatingActionButton.setOnClickListener { extendedFloatingActionButtonPressed() }
    }

    private fun extendedFloatingActionButtonPressed() {
    }

    override fun listViewControllerOnItemSelected(item: CellUIModel, position: Int, clickTag: String?) {

    }

}
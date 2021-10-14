package me.taosunkist.hello.ui.list.multilist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mooveit.library.Fakeit
import me.taosunkist.hello.databinding.FragmentMultiListBinding
import me.taosunkist.hello.ui.list.multilist.itemlistviewwrapper.FirstListViewWrapper
import me.taosunkist.hello.ui.list.multilist.itemlistviewwrapper.SecondItemListViewWrapper
import me.taosunkist.hello.ui.reusable.itemlistviewwrapper.ItemListViewWrapperDelegate
import top.thsunkist.tatame.model.ui.ImageUIModel

data class MultiListUIModel(val imageUIModel: ImageUIModel, val nickname: String) {

    companion object {
        fun fakes(): MultiListUIModel {
            return MultiListUIModel(imageUIModel = ImageUIModel.fake(), nickname = Fakeit.pokemon().name())
        }
    }
}

class MultiListFragment : Fragment(), ItemListViewWrapperDelegate<CelllUIModel> {

    companion object {

        @JvmStatic
        fun newInstance() = MultiListFragment().apply {}
    }

    lateinit var secondItemListViewWrapper: SecondItemListViewWrapper

    lateinit var firstItemListViewWrapper: FirstListViewWrapper

    private var _binding: FragmentMultiListBinding? = null

    private val binding: FragmentMultiListBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentMultiListBinding.inflate(inflater, container, false)?.also { _binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        secondItemListViewWrapper = SecondItemListViewWrapper(view).apply {
            delegate = this@MultiListFragment
        }
        firstItemListViewWrapper = FirstListViewWrapper(binding.likeEachOthersContainer).apply {}

    }

    override fun listViewControllerOnItemSelected(item: CelllUIModel, position: Int, clickTag: String?) {

    }
}
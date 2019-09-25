package me.taosunkist.hello.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import me.taosunkist.hello.R

class RecyclerViewOrientationFragment : Fragment() {


    companion object {
        const val tag = "RecyclerViewOrientation"

        fun newInstance(): RecyclerViewOrientationFragment {
            return RecyclerViewOrientationFragment()
        }
    }

    private val items = arrayListOf(0, 1, 2, 3, 4)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recycler_view_orientation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var recyclerView = view.findViewById<RecyclerView>(R.id.fragment_recycler_view_orientation_recycler_view)
        val recyclerViewAdapter = MyAdapter()

        recyclerView.adapter = recyclerViewAdapter
        val manager = PageLayoutManager(2, 4)
        recyclerView.layoutManager = manager

        manager.setMarginHorizontal(1)
        manager.setMarginVertical(5)

    }

}
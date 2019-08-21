package me.taosunkist.hello.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
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
        val recyclerView = view.findViewById<RecyclerView>(R.id.fragment_recycler_view_orientation_recycler_view)
        val recyclerViewAdapter = RecyclerViewAdapter()

        recyclerView.adapter = recyclerViewAdapter

    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(AppCompatImageView(parent.context).apply {
                setImageResource(R.drawable.btn_radio_off_mtrl)
                setOnClickListener {
                    items.add(items[items.size - 1] + 1)
                    Toast.makeText(context, "${items.size}", Toast.LENGTH_SHORT).show()
                    notifyDataSetChanged()
                }
            })
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        }
    }
}
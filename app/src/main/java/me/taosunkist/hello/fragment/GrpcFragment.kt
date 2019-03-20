package me.taosunkist.hello.fragment

import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import io.grpc.Grpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.internal.GrpcUtil

import me.taosunkist.hello.R
import java.net.SocketAddress

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GrpcFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class GrpcFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var hiGrpc: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_grpc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hiGrpc = view.findViewById(R.id.hi_grpc)
        hiGrpc?.setOnClickListener { Toast.makeText(this@GrpcFragment.context, "1111", Toast.LENGTH_SHORT).show() }
        var mc: ManagedChannel = ManagedChannelBuilder.forAddress("127.0.0.1", 50051).usePlaintext().build()
    }

    companion object {
        const val TAG: String = "GrpcFragment"
        const val GRPC_SRV_ADDR = "127.0.0.1:50051"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                GrpcFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }

        @JvmStatic
        fun newInstance() = GrpcFragment().apply { }
    }
}

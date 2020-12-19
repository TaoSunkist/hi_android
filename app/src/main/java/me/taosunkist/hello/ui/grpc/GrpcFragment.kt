package me.taosunkist.hello.ui.grpc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import me.taosunkist.hello.R

class GrpcFragment : Fragment() {

    companion object {
        const val TAG = "Grpc"
        const val grpcServerAddress = "127.0.0.1"
        const val grpcServerPort = "50051"

        fun newInstance(): GrpcFragment {
            return GrpcFragment()
        }
    }

    private var hiGrpc: TextView? = null
    private var resultText: TextView? = null
    lateinit var messageEdit: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_grpc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hiGrpc = view.findViewById(R.id.hi_grpc)
        messageEdit = view.findViewById(R.id.messageEdit)
        resultText = view.findViewById(R.id.grpc_response_text)

        hiGrpc!!.setOnClickListener {
            Toast.makeText(context, "send msg to grpc service", Toast.LENGTH_SHORT).show()
            sendMessage()
        }
    }

    private fun sendMessage() {
//        val channel: ManagedChannel = ManagedChannelBuilder.forAddress(grpcServerAddress, grpcServerPort.toInt()).usePlaintext().build()

//        val single = Single.create<String> {
//            val stub = GreeterGrpc.newBlockingStub(channel)
//            val helloRequestOrBuilder = HelloRequest.newBuilder()
//            val helloRequest = helloRequestOrBuilder.setName(messageEdit.text.toString()).build()
//            val reply = stub.sayHello(helloRequest)
//            it.onSuccess(reply.message)
//        }
//
//        single.observeOnMainThread(onSuccess = {
//            channel.shutdown().awaitTermination(1, TimeUnit.SECONDS)
//            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
//        }, onError = {
//            Toast.makeText(context, it.localizedMessage
//                    ?: "unknown error", Toast.LENGTH_SHORT).show()
//        }).addTo(compositeDisposable = CompositeDisposable())

    }
}

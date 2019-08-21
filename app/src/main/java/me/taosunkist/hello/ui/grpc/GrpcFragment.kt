package me.taosunkist.hello.ui.grpc

//import android.app.Activity
//import android.os.AsyncTask
//import android.os.Bundle
//import android.text.TextUtils
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import java.io.PrintWriter
//import java.io.StringWriter
//import java.lang.ref.WeakReference
//import java.util.concurrent.TimeUnit
//import io.grpc.ManagedChannel
//import io.grpc.ManagedChannelBuilder
//import javapb.GreeterGrpc
//import javapb.HelloRequest
//import me.taosunkist.hello.R
//
///**
// * A simple [Fragment] subclass.
// * Use the [GrpcFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class GrpcFragment : Fragment() {
//
//    companion object {
//        const val tag = "Grpc"
//        const val grpcServerAddress = "192.168.1.103"
//        const val grpcServerPort = "50051"
//
//        fun newInstance(): GrpcFragment {
//            return GrpcFragment()
//        }
//    }
//
//    private var hiGrpc: TextView? = null
//    private var resultText: TextView? = null
//    private var messageEdit: EditText? = null
//
//    init {
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_grpc, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        hiGrpc = view.findViewById(R.id.hi_grpc)
//        messageEdit = view.findViewById(R.id.messageEdit)
//        resultText = view.findViewById(R.id.grpc_response_text)
//
//        hiGrpc!!.setOnClickListener {
//            Toast.makeText(context, "send msg to grpc srv", Toast.LENGTH_SHORT).show()
//            sendMessage()
//        }
//    }
//
//    fun sendMessage() {
//        GrpcTask(activity!!).execute(grpcServerAddress, messageEdit!!.text.toString(), grpcServerPort)
//    }
//
//    private class GrpcTask constructor(activity: Activity) : AsyncTask<String, Void, String>() {
//        private val activityReference: WeakReference<Activity> = WeakReference(activity)
//        private var channel: ManagedChannel? = null
//
//        override fun doInBackground(vararg params: String): String {
//            val host = params[0]
//            val message = params[1]
//            val portStr = params[2]
//            val port = if (TextUtils.isEmpty(portStr)) 0 else Integer.valueOf(portStr)
//            try {
//                channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
//                val stub = GreeterGrpc.newBlockingStub(channel)
//                val request = HelloRequest.newBuilder().setName(message).build()
//                val reply = stub.sayHello(request)
//                return reply.message
//            } catch (e: Exception) {
//                val sw = StringWriter()
//                val pw = PrintWriter(sw)
//                e.printStackTrace(pw)
//                pw.flush()
//                return String.format("Failed... : %n%s", sw)
//            }
//
//        }
//
//        override fun onPostExecute(result: String) {
//            try {
//                channel!!.shutdown().awaitTermination(1, TimeUnit.SECONDS)
//            } catch (e: InterruptedException) {
//                Thread.currentThread().interrupt()
//            }
//
//            val activity = activityReference.get() ?: return
//            Log.d("GRPC", "onPostExecute: $result")
//        }
//    }
//}

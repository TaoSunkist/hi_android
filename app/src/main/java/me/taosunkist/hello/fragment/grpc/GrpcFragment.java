package me.taosunkist.hello.fragment.grpc;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javapb.GreeterGrpc;
import javapb.HelloReply;
import javapb.HelloRequest;
import me.taosunkist.hello.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GrpcFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GrpcFragment extends Fragment {


    public GrpcFragment() {
        // Required empty public constructor
    }

    public static GrpcFragment newInstance() {
        GrpcFragment fragment = new GrpcFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grpc, container, false);
    }

    private TextView hiGrpc, resultText;
    private EditText messageEdit;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hiGrpc = view.findViewById(R.id.hi_grpc);
        messageEdit = view.findViewById(R.id.messageEdit);
        resultText = view.findViewById(R.id.grpc_response_text);

        hiGrpc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "send msg to grpc srv", Toast.LENGTH_SHORT).show();
                sendMessage();
            }
        });
    }

    public static final String GRPC_SRV_HOST = "192.168.1.103";
    public static final String GRPC_SRV_PORT = "50051";

    public void sendMessage() {
        new GrpcTask(getActivity()).execute(GRPC_SRV_HOST, messageEdit.getText().toString(), GRPC_SRV_PORT);
    }

    private static class GrpcTask extends AsyncTask<String, Void, String> {
        private final WeakReference<Activity> activityReference;
        private ManagedChannel channel;

        private GrpcTask(Activity activity) {
            this.activityReference = new WeakReference<Activity>(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            String host = params[0];
            String message = params[1];
            String portStr = params[2];
            int port = TextUtils.isEmpty(portStr) ? 0 : Integer.valueOf(portStr);
            try {
                channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);
                HelloRequest request = HelloRequest.newBuilder().setName(message).build();
                HelloReply reply = stub.sayHello(request);
                return reply.getMessage();
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                pw.flush();
                return String.format("Failed... : %n%s", sw);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                channel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Activity activity = activityReference.get();
            if (activity == null) {
                return;
            }
            Log.d("GRPC", "onPostExecute: " + result);
        }
    }
}

package me.taosunkist.hello.fragment.grpc;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
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

    private TextView hiGrpc;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hiGrpc = view.findViewById(R.id.hi_grpc);
        hiGrpc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(me.taosunkist.hello.fragment.GrpcFragment.TAG, "onClick: prepare");
                ManagedChannel mc = ManagedChannelBuilder.forAddress("127.0.0.1", 50051).usePlaintext().build();

                Log.d(me.taosunkist.hello.fragment.GrpcFragment.TAG, "onClick: conned");

            }
        });
    }

}

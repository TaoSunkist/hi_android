package me.taosunkist.hello.ui.frgment.progress;

import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.util.Random;

import me.taosunkist.hello.Dimens;
import me.taosunkist.hello.R;

public class SquareProgressBarFragment extends Fragment {
    public SquareProgressBar squareProgressBar;
    private SeekBar progressSeekBar, widthSeekBar;
    private int currentProcess;

    public static SquareProgressBarFragment newInstance() {

        Bundle args = new Bundle();

        SquareProgressBarFragment fragment = new SquareProgressBarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static final String TAG = "SquareFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_square_progress_bar, container, false);

        view.setBackgroundColor(Color.GRAY);

        final TextView progressView = (TextView) view
                .findViewById(R.id.progressDisplay);
        progressView.setText("32%");


        squareProgressBar = view.findViewById(R.id.subi2);
        squareProgressBar.setImage(R.drawable.blenheim_palace);
        squareProgressBar.setColor("#C9C9C9");
        squareProgressBar.setHoloColor(R.color.errorTextColor);
        squareProgressBar.setRoundedCorners(true, Dimens.INSTANCE.getMarginSmall());
        squareProgressBar.setProgress(32);
        squareProgressBar.setWidth(8);
        squareProgressBar.setOnClickListener(view1 -> {

            Random random = new Random();

            // random progress
            setProgressBarProgress(random.nextInt(100), progressView);

            // random width
            int randWidth = random.nextInt(17) + 4;
            widthSeekBar.setProgress(randWidth);
            squareProgressBar.setWidth(randWidth);

            // random colour
            squareProgressBar.setColorRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        });

        progressSeekBar = (SeekBar) view
                .findViewById(R.id.progressSeekBar);
        progressSeekBar
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // nothing to do
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // nothing to do
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        setProgressBarProgress(progress, progressView);
                    }
                });

        widthSeekBar = (SeekBar) view.findViewById(R.id.widthSeekBar);
        widthSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // nothing to do
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // nothing to do
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                squareProgressBar.setWidth(progress);
            }
        });
        return view;
    }

    private void setProgressBarProgress(int progress, TextView progressView) {
        squareProgressBar.setProgress(progress);
        progressView.setText(progress + "%");
        progressSeekBar.setProgress(progress);
    }

}

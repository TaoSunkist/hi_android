package me.taosunkist.hello;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import me.taosunkist.uilib.WaveView;

/**
 *
 */
public class BezierCurveActivity extends AppCompatActivity {
    private WaveView waveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_curve);
        waveView = (WaveView) findViewById(R.id.wave_view);
        Toast.makeText(this, "1111", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        waveView.run();
    }
    public static void open(Context context) {
        Intent starter = new Intent(context, BezierCurveActivity.class);
        context.startActivity(starter);
    }
}

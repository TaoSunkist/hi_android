package me.taosunkist.hello.ui.shakemusicbar;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.taosunkist.hello.R;

public class ShakeMusicBarActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
	private static final int MIN_VELOCITY = 100;
	private static final String TEXT_START = "Start";
	ShakeMusicBarView mBarView;
	ShakeMusicBarView mSmallBarView;
	private Button mSwitchBtn;
	private SeekBar mVelocitySeekBar;
	private SeekBar mBarCountSeekBar;
	private SeekBar mBarBetweenInvSeekBar;
	private SparseArray<TextView> mMap;
	private Button mModeSwitchBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shake_music_bar);
		mBarView = (ShakeMusicBarView) findViewById(R.id.shake_view);
	
		mBarView.setOnInitFinishedListener(() -> {
			mVelocitySeekBar.setProgress((int)mBarView.getVelocity());
			mBarCountSeekBar.setProgress(mBarView.getBarCount());
			mBarBetweenInvSeekBar.setProgress(mBarView.getInvBetweenBars());

			updateProgressValue(mVelocitySeekBar);
			updateProgressValue(mBarCountSeekBar);
			updateProgressValue(mBarCountSeekBar);
		});
		
		mBarView.init();
		
		mSwitchBtn = (Button) findViewById(R.id.btn_switch);
		mSwitchBtn.setOnClickListener(this);
		
		mModeSwitchBtn = (Button) findViewById(R.id.btn_mode_switch);
		mModeSwitchBtn.setOnClickListener(this);
		
		mVelocitySeekBar = (SeekBar) findViewById(R.id.bar_velocity);
		mVelocitySeekBar.setOnSeekBarChangeListener(this);
		
		mBarCountSeekBar = (SeekBar) findViewById(R.id.bar_count);
		mBarCountSeekBar.setOnSeekBarChangeListener(this);
		
		mBarBetweenInvSeekBar = (SeekBar) findViewById(R.id.bar_intervals);
		mBarBetweenInvSeekBar.setOnSeekBarChangeListener(this);
		
		mMap = new SparseArray<TextView>();
		mMap.put(R.id.bar_count, (TextView)findViewById(R.id.textview_barcounts));
		mMap.put(R.id.bar_intervals, (TextView)findViewById(R.id.textview_barinvs));
		mMap.put(R.id.bar_velocity, (TextView)findViewById(R.id.textview_velocity));
		
		mSmallBarView = (ShakeMusicBarView) findViewById(R.id.shake_small_view);
		mSmallBarView.init();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_switch){
			if(mSwitchBtn.getText().toString().equals(TEXT_START)){
				mBarView.shake(true);
				mSmallBarView.shake(true);
				mSwitchBtn.setText("Stop");
			}else{
				mBarView.shake(false);
				mBarView.stopToHeight(5.0f);
				
				mSmallBarView.shake(false);
				mSmallBarView.stopToHeight(5.0f);
				mSwitchBtn.setText(TEXT_START);
			}
		}else if(v.getId() == R.id.btn_mode_switch){
			if(mModeSwitchBtn.getText().toString().equals("ColorMode")){
				mBarView.changeDrawable(R.drawable.rectangle_background);
				mSmallBarView.changeDrawable(R.drawable.rectangle_background);
				mModeSwitchBtn.setText("BackgroundMode");
			}else{
				mBarView.changeColor(mBarView.getColor());
				mSmallBarView.changeColor(mSmallBarView.getColor());
				mModeSwitchBtn.setText("ColorMode");
			}
		}
		
		
	}


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		updateProgressValue(seekBar);
	}


	private void updateProgressValue(SeekBar seekBar) {
		int progress = seekBar.getProgress();
		TextView textview = mMap.get(seekBar.getId());
		String tag = textview.getTag().toString();
		String s= String.format(tag,progress);	
		textview.setText(s);
	}


	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}


	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		int progress = seekBar.getProgress();
		if(seekBar.getId() == R.id.bar_velocity){
			if(progress == 0){
				progress = progress + MIN_VELOCITY;
			}
			mBarView.setVelocity(progress);
			mSmallBarView.setVelocity(progress);
		}else if(seekBar.getId() == R.id.bar_count){
			if(progress == 0){
				progress = progress + 1;
			}
			mBarView.changeBarCount(progress);
			mSmallBarView.changeBarCount(progress);
		}else if(seekBar.getId() == R.id.bar_intervals){
			if(progress == 0){
				progress = progress + 1;
			}
			mBarView.changeInvBetweenBars(progress);
			mSmallBarView.changeInvBetweenBars(progress);
		}
	}


}

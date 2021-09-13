package me.taosunkist.hello.ui.shakemusicbar;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewPropertyAnimator;

public class IndividualMusicShakeBar extends View {
	private static final Float[] DEFAULT_MULTIPLIER_ARR = {0.9f,0.8f, 0.7f ,0.6f, 0.5f, 0.4f, 0.3f, 0.2f, 0.1f, 0.0f};
	private static final int DEFAULT_ANIMATIOR_SHAKING_DURATION = 150;
	
	private Handler mAnimationHandler;
	private ViewPropertyAnimator mViewAnimator;
	private int mMultiplierIndex = 0;
	private boolean mIsShake = false;
	private List<Float> mShuffleList;
	private float mPrevMulti;
	
	// mVelocity = height/duration => duration = height/velocity, and the unit of duration is in second 
	private float mVelocity;

	public IndividualMusicShakeBar(Context context, Handler handler) {
		super(context);
		mAnimationHandler = handler;
		mViewAnimator = animate();
		mViewAnimator.setDuration(DEFAULT_ANIMATIOR_SHAKING_DURATION);
		mShuffleList = Arrays.asList(DEFAULT_MULTIPLIER_ARR);
	}
	
	public IndividualMusicShakeBar(Context context){
		this(context, new Handler());
	}
	
	public void init(int color, float velocity){
		setBackgroundColor(color);
		setVelocity(velocity);
		startShake();
	}

	public void shake(boolean isShake) {
		if(isShake && mIsShake){
			//already shaking.
			return;
		}
		mIsShake = isShake;
		if(isShake){
			startShake();
		}
	}

	private void startShake() {
		mAnimationHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				IndividualMusicShakeBar.this.runShaking(this);
			}

		}, 0);
	}

	private void runShaking(Runnable shakingTask) {
		if(!mIsShake){
			return;
		}
	
		float height = getHeight();
		float multiplier = mShuffleList.get(mMultiplierIndex);
	
		float shakingHeight = height*multiplier;

		multiplier = acquireDiffFromPrevMultiplier(multiplier);
		
		long durationInMillisecond = calculateDuration(shakingHeight);
		shakingToSpecifiedHeight(shakingHeight, durationInMillisecond);
		
		setupNextShakingAndRun(shakingTask, multiplier, durationInMillisecond);
	}

	private long calculateDuration(float shakingHeight) {
		float durationInSec = shakingHeight / mVelocity;
		long durationInMillisecond = (long) (durationInSec*1000);
		return durationInMillisecond;
	}

	private void shakingToSpecifiedHeight(float shakingHeight, long durationInMillisecond) {
		mViewAnimator.setDuration(durationInMillisecond);
		mViewAnimator.translationY(shakingHeight);
	}

	private void setupNextShakingAndRun(Runnable nextShakingRunnable, float multiplier,
			long nextShakingDuration) {
		setupMextMultiplier(multiplier);
	
		shuffleMultiplier();
		postDelayed(nextShakingRunnable, nextShakingDuration);
	}

	private void setupMextMultiplier(float multiplier) {
		if(mPrevMulti != multiplier){
			++mMultiplierIndex;
		}
		if(mMultiplierIndex >= mShuffleList.size()){
			mMultiplierIndex = 0;
		}
		mPrevMulti = multiplier;
	}
	
	private void shuffleMultiplier(){
		Collections.shuffle(mShuffleList);
	}

	private float acquireDiffFromPrevMultiplier(float multiplier) {
		/*ignore nextMultiplier if it is same with previous one which will cause bar present pause in visual
		 which is not in our expectation.*/
		while(Math.abs(mPrevMulti - multiplier) <= 0.0f){
			multiplier = mShuffleList.get((++mMultiplierIndex) % mShuffleList.size());
		}
		return multiplier;
	}
	
	public void stopToHeight(float yInPX){
		final int height = (int) (getHeight() - yInPX);
		mViewAnimator.translationY(height);
	}
	
	public void setVelocity(float velocity){
		mVelocity = velocity;
	}
	
	public float getVelocity(){
		return mVelocity;
	}
	
}

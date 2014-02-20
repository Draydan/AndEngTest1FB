package com.example.andenginetest1;

import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.engine.camera.*;
 
//import android.graphics.Camera;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
 
public class MainActivity extends SimpleBaseGameActivity{
	
	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;

	public static MainState MainState;
	public static Camera Camera;
	private boolean GameLoaded = false;
	public static MainActivity main;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		main = this;
		Camera = new Camera(0,0, 800, 480);
		final EngineOptions opt = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR , 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), Camera);
		opt.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		return opt;
	}

	
	@Override
	protected void onCreateResources() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Scene onCreateScene() {
		MainState = new MainState();
		GameLoaded = true;
		return MainState;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			if(!GameLoaded) return true;
			if(MainState != null && GameLoaded)
			{
				MainState.KeyPressed(keyCode, event);				
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {		
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
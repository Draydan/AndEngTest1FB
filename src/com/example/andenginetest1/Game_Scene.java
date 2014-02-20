package com.example.andenginetest1;

import java.nio.channels.Pipe;
import java.util.ArrayList;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.CameraScene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

public class Game_Scene extends CameraScene {
	
	public float PulseDirection = 0.1f;
	public final Rectangle rec;
	public double Vx=10, Vy=0;
	public final double g = -50;
	public final int pipeSpeed = -80;
	public final int pushForce = 80;
	public boolean loss = false;
	public double spawnTimer = 0;
	public final double spawnSpawn = 4; // seconds
	public final int pipesInRotation = 12;
	
	
	ArrayList<Rectangle> Pipes = new  ArrayList<Rectangle>();
	
	
	public Game_Scene()
	{
		super(MainActivity.Camera);
		setBackgroundEnabled(false);
		
		rec = new Rectangle(MainActivity.CAMERA_WIDTH/3, MainActivity.CAMERA_HEIGHT/2 , 100, 100, new VertexBufferObjectManager()) 
		{
//			public boolean onAreaTouched(org.andengine.input.touch.TouchEvent pSceneTouchEvent, 
//					float pTouchAreaLocalX, float pTouchAreaLocalY) 
//			{
//				//MainState.ShowMainScene();
//				Vy += pushForce;
//				return true;
//			};			
		};
		
		attachChild(rec);
		rec.setColor(Color.RED);		
		//registerTouchArea(rec);
	}
	
	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		Vy = pushForce;		 
		return super.onSceneTouchEvent(pSceneTouchEvent);
	}
	
	public void Show ()
	{
		setVisible(true);
		setIgnoreUpdate(false);
	}

	public void Hide()
	{
		setVisible(false);
		setIgnoreUpdate(true);
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		//if (rec.getWidth()<=0 || rec.getHeight()<=0 ) PulseDirection = -PulseDirection;
		//rec.setWidth(rec.getWidth() - PulseDirection);
		//rec.setHeight(rec.getHeight() - PulseDirection);
		
		//
		//rec.setX(rec.getX()+Vx);
		Vy += g * pSecondsElapsed;
		rec.setY(rec.getY()+Math.round(Vy * pSecondsElapsed));
		if (rec.getY()<0) loss = true;
		
		spawnTimer += pSecondsElapsed;
		if (spawnTimer >= spawnSpawn)
		{
			spawnTimer = 0;
			Rectangle newPipe = new Rectangle(Math.round(MainActivity.CAMERA_WIDTH * 0.9), 
					Math.round(MainActivity.CAMERA_HEIGHT * (0.25 + 0.2 * Math.sin(Pipes.size()*2*Math.PI/pipesInRotation))) , 
					50, 100, new VertexBufferObjectManager());
			attachChild(newPipe);
			newPipe.setColor(Color.GREEN);
			Rectangle newPipe2 = new Rectangle(Math.round(MainActivity.CAMERA_WIDTH * 0.9), 
					Math.round(MainActivity.CAMERA_HEIGHT * (0.75 + 0.2 * Math.sin(Pipes.size()*2*Math.PI/pipesInRotation))) ,
					50, 100, new VertexBufferObjectManager());
			attachChild(newPipe2);
			newPipe2.setColor(Color.GREEN);
			Pipes.add(newPipe2);
			Pipes.add(newPipe);
		}
		for(int pi=0; pi<Pipes.size(); pi++)
		{
			Rectangle currPipe = Pipes.get(pi); 
			currPipe.setX(currPipe.getX() + pipeSpeed * pSecondsElapsed);
		}
		//if (rec.getX() <= 0 || rec.getX() >= MainActivity.CAMERA_WIDTH ) Vx = -Vx;
		//if (rec.getY() <= 0 || rec.getY() >= MainActivity.CAMERA_HEIGHT ) Vy = -Vy;
		
		super.onManagedUpdate(pSecondsElapsed);
	}

	
}

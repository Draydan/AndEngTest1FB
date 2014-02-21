package com.example.andenginetest1;

import java.nio.channels.Pipe;
import java.util.ArrayList;

import org.andengine.engine.handler.collision.CollisionHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.CameraScene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

public class Game_Scene extends CameraScene {
	
	public float PulseDirection = 0.1f;
	public final Rectangle bird;
	public double Vx=10, Vy=0;
	public final double g = 100;
	public final int pipeSpeed = -80;
	public final int pushForce = -120;
	public boolean loss = false;
	
	public double spawnTimer = 3;
	public final double spawnSpawn = 3; // seconds
	public final int pipesInRotation = 12;
	
	public double deathTimer = 0;
	public final double deathTimerEndSeconds = 2; 
	
	private int GamePlayState = 0; 
	
	private static final int GAME_RUNNING_STATE = 2;
	private static final int GAME_STARTING_STATE = 3;
	private static final int GAME_OVERING_STATE = 4;
	
	private static final int BIRD_STARTING_X = MainActivity.CAMERA_WIDTH/3;
	private static final int BIRD_STARTING_Y = MainActivity.CAMERA_HEIGHT/2;
	
	private static final double PIPE_TO_SCREEN_POSITION = 0.2;
	private static final double PIPE_TO_SCREEN_HEIGHT = 0.15;
	
	ArrayList<Rectangle> Pipes = new  ArrayList<Rectangle>();
	
	
	public Game_Scene()
	{
		super(MainActivity.Camera);
		setBackgroundEnabled(false);
		
		bird = new Rectangle(BIRD_STARTING_X ,BIRD_STARTING_Y , 50, 50, new VertexBufferObjectManager()) 
		{
//			public boolean onAreaTouched(org.andengine.input.touch.TouchEvent pSceneTouchEvent, 
//					float pTouchAreaLocalX, float pTouchAreaLocalY) 
//			{
//				//MainState.ShowMainScene();
//				Vy += pushForce;
//				return true;
//			};			
		};
		
		attachChild(bird);
		bird.setColor(Color.RED);		
		//registerTouchArea(bird);
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
		switch(GamePlayState)
		{
			case 0:
				GamePlayState = GAME_STARTING_STATE;
			break;
			
			case GAME_STARTING_STATE:				
				StartingPlay();
				GamePlayState = GAME_RUNNING_STATE;
			break;
			
			case GAME_RUNNING_STATE :		
			//if (bird.getWidth()<=0 || bird.getHeight()<=0 ) PulseDirection = -PulseDirection;
			//bird.setWidth(bird.getWidth() - PulseDirection);
			//bird.setHeight(bird.getHeight() - PulseDirection);
			
			//bird.setX(bird.getX()+Vx);
			Vy += g * pSecondsElapsed;
			bird.setY(bird.getY()+Math.round(Vy * pSecondsElapsed));
			if (bird.getY()<0) loss = true;
			
			// do spawns
			spawnTimer += pSecondsElapsed;
			if (spawnTimer >= spawnSpawn)
			{
				spawnTimer = 0;
				Rectangle newPipe = new Rectangle(Math.round(MainActivity.CAMERA_WIDTH * 0.9), 
					Math.round(MainActivity.CAMERA_HEIGHT * 
						(PIPE_TO_SCREEN_POSITION + PIPE_TO_SCREEN_HEIGHT * Math.sin(Pipes.size()*2*Math.PI/pipesInRotation))) , 
							50, 100, new VertexBufferObjectManager());
				attachChild(newPipe);
				newPipe.setColor(Color.GREEN);
				Rectangle newPipe2 = new Rectangle(Math.round(MainActivity.CAMERA_WIDTH * 0.9), 
					Math.round(MainActivity.CAMERA_HEIGHT * 
						(1 - PIPE_TO_SCREEN_POSITION + PIPE_TO_SCREEN_HEIGHT * Math.sin(Pipes.size()*2*Math.PI/pipesInRotation))) ,
							50, 100, new VertexBufferObjectManager());
				attachChild(newPipe2);
				newPipe2.setColor(Color.GREEN);
				Pipes.add(newPipe2);
				Pipes.add(newPipe);
			}
			//-----
			
			// move pipes
			for(int pi=0; pi<Pipes.size(); pi++)
			{
				Rectangle currPipe = Pipes.get(pi); 
				currPipe.setX(currPipe.getX() + pipeSpeed * pSecondsElapsed);
			}
			//-------------
			
			// bird collision check
			for(int pi=0; pi<Pipes.size(); pi++)
			{
				Rectangle currPipe = Pipes.get(pi);
				float px = currPipe.getX();
				float py = currPipe.getY();
				float pw = currPipe.getWidth();
				float ph = currPipe.getHeight();

				float bx = bird.getX();
				float by = bird.getY();
				float bw = bird.getWidth();
				float bh = bird.getHeight();				
				//if(Math.abs(currPipe.getX() - bird.getX()) < (currPipe.getWidth() + bird.getWidth())/2 
				//		&& Math.abs(currPipe.getY() - bird.getY()) < (currPipe.getHeight() + bird.getHeight())/2)
				if(( (px > bx && px < bx+bw) || (px+pw > bx && px+pw < bx+bw)) 
						&&
					( (py > by && py < by+bh) || (py+ph > by && py+ph < by+bh))
						)
				{
					GamePlayState = GAME_OVERING_STATE;
					deathTimer = 0;
				}
			}
			//---
			break;
			
			case GAME_OVERING_STATE:
				deathTimer += pSecondsElapsed;
				if(deathTimer >= deathTimerEndSeconds) 
				{
					GamePlayState = GAME_STARTING_STATE;
					EndingPlay();
					MainState.ShowMainScene();
				}
			break;			
		}
		
		//if (bird.getX() <= 0 || bird.getX() >= MainActivity.CAMERA_WIDTH ) Vx = -Vx;
		//if (bird.getY() <= 0 || bird.getY() >= MainActivity.CAMERA_HEIGHT ) Vy = -Vy;
		
		super.onManagedUpdate(pSecondsElapsed);
	}

	public void StartingPlay()
	{
		bird.setX(BIRD_STARTING_X);
		bird.setY(BIRD_STARTING_Y);
	}
	
	public void EndingPlay()
	{
		for(int pi = Pipes.size()-1; pi>=0; pi--) 
		{
			Rectangle currPipe = Pipes.remove(pi);			
			//currPipe.dispose();
			detachChild(currPipe);
		}		
	}
	
}

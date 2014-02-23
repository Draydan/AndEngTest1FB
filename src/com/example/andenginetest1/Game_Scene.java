package com.example.andenginetest1;

import java.util.ArrayList;

import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.entity.text.*;

public class Game_Scene extends CameraScene {
	
	public final AnimatedSprite bird;
	public int score = 0;
	public int hiscore = 0;
	
	public float PulseDirection = 0.1f;	
	public double Vx=10, Vy=0;
	public final double g = 300;
	public final int pipeSpeed = -80;
	public final int pushSpeed = -150;
	public boolean loss = false;
	
	public double spawnTimer = 3;
	public final double spawnSpawn = 3; // seconds
	public final int pipesInRotation = 12;
	public float sizeGrowth = 0;
	public final float sizeGrowthStep = 0.02f;
	public final float sizeGrowthThreshold = 0.2f;
	
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
	
	ArrayList<Sprite> Pipes = new  ArrayList<Sprite>();	
	
	public Game_Scene()
	{
		super(MainActivity.Camera);
		
		
		//Text _text1 = new Text(100, 100, MainActivity.font_BosaNova22, "Hello, AndEngine!\n\nGoodbye.", null);
		//_text1.setColor(127/255f, 127/255f, 255/255f); //÷вет текста будет сиреневый
		//this.attachChild(_text1);
		
		setBackgroundEnabled(true);
		Sprite BGSprite = new Sprite(0,  0, MainActivity.main.BG_TR, new VertexBufferObjectManager());   
		SpriteBackground sb = new SpriteBackground(BGSprite);
		this.setBackground(sb);
		
		bird = new AnimatedSprite(0, 0, MainActivity.main.Bird_TR, new VertexBufferObjectManager());
		bird.setX(BIRD_STARTING_X);
		bird.setY(BIRD_STARTING_Y);
		
		//bird = new Rectangle(BIRD_STARTING_X ,BIRD_STARTING_Y , 50, 50, new VertexBufferObjectManager()) 
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
		bird.animate(200);
		//bird.setColor(Color.RED);		
		//registerTouchArea(bird);
	}
	
	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		Vy = pushSpeed;		 
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
				float sino = (float)Math.sin(Pipes.size()*2*Math.PI/pipesInRotation);
				float signo = Math.signum(sino);
				Sprite newPipe = new Sprite(0, 0, MainActivity.main.Pipe_TR, new VertexBufferObjectManager());
				newPipe.setX(Math.round(MainActivity.CAMERA_WIDTH * 0.9));
				newPipe.setY(Math.round(MainActivity.CAMERA_HEIGHT * 
						(sizeGrowth * signo + PIPE_TO_SCREEN_POSITION + PIPE_TO_SCREEN_HEIGHT * sino)));
				/*Rectangle newPipe = new Rectangle(Math.round(MainActivity.CAMERA_WIDTH * 0.9), 
					Math.round(MainActivity.CAMERA_HEIGHT * 
						(PIPE_TO_SCREEN_POSITION + PIPE_TO_SCREEN_HEIGHT * Math.sin(Pipes.size()*2*Math.PI/pipesInRotation))) , 
							50, 100, new VertexBufferObjectManager());
				*/
				attachChild(newPipe);
				//newPipe.setColor(Color.GREEN);
				//Rectangle newPipe2 = new Rectangle(Math.round(MainActivity.CAMERA_WIDTH * 0.9),
				Sprite newPipe2 = new Sprite(0, 0, MainActivity.main.Pipe_TR, new VertexBufferObjectManager());
				newPipe2.setX(Math.round(MainActivity.CAMERA_WIDTH * 0.9));
				newPipe2.setY(Math.round(MainActivity.CAMERA_HEIGHT * 
						(-sizeGrowth * signo + 1 - PIPE_TO_SCREEN_POSITION + PIPE_TO_SCREEN_HEIGHT * sino)));
				newPipe2.setRotation(180);//(float) Math.PI);
//					Math.round(MainActivity.CAMERA_HEIGHT * 
//						(1 - PIPE_TO_SCREEN_POSITION + PIPE_TO_SCREEN_HEIGHT * Math.sin(Pipes.size()*2*Math.PI/pipesInRotation))) ,
//							50, 100, new VertexBufferObjectManager());
				attachChild(newPipe2);
				//newPipe2.setColor(Color.GREEN);
				Pipes.add(newPipe2);
				Pipes.add(newPipe);
				
				if(sizeGrowth <= sizeGrowthThreshold) sizeGrowth += sizeGrowthStep;
			}
			//-----
			
			// move pipes
			for(int pi=0; pi<Pipes.size(); pi++)
			{
				Sprite currPipe = Pipes.get(pi); 
				currPipe.setX(currPipe.getX() + pipeSpeed * pSecondsElapsed);
			}
			//-------------
			
			// bird collision check
			for(int pi=0; pi<Pipes.size(); pi++)
			{
				Sprite currPipe = Pipes.get(pi);
				float px = currPipe.getX();
				float py = currPipe.getY();
				float pw = currPipe.getWidth();
				float ph = currPipe.getHeight();

				float bx = bird.getX();
				float by = bird.getY();
				float bw = bird.getWidth() * 0.66f;
				float bh = bird.getHeight() * 0.66f;				
				//if(Math.abs(currPipe.getX() - bird.getX()) < (currPipe.getWidth() + bird.getWidth())/2 
				//		&& Math.abs(currPipe.getY() - bird.getY()) < (currPipe.getHeight() + bird.getHeight())/2)
				if(
						(
						( (px > bx && px < bx+bw) || (px+pw > bx && px+pw < bx+bw)) 
						&&
					( (py > by && py < by+bh) || (py+ph > by && py+ph < by+bh))
						)
						||
						(by<=0 || by >= MainActivity.CAMERA_HEIGHT)
				)
				{
					GamePlayState = GAME_OVERING_STATE;
					deathTimer = 0;
				}
				
				if (bx > px) score = pi+1;
			}
			//---
			
			bird.setRotation((float) (Math.atan2(Vy, -pipeSpeed)*180/Math.PI / 2));
			
			break; // end running-state
			
			case GAME_OVERING_STATE:
				if (score>hiscore) hiscore = score;
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
		sizeGrowth = 0;
	}
	
	public void EndingPlay()
	{
		score = 0;
		for(int pi = Pipes.size()-1; pi>=0; pi--) 
		{
			Sprite currPipe = Pipes.remove(pi);			
			//currPipe.dispose();
			detachChild(currPipe);
		}		
	}
	
}

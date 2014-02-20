package com.example.andenginetest1;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.IDisposable.AlreadyDisposedException;
import org.andengine.util.color.Color;

public class MainMenu_Scene extends CameraScene {

	public MainMenu_Scene()
	{
		super(MainActivity.Camera);
		setBackgroundEnabled(false);
		final Rectangle _rec = new Rectangle(10, 10, 400, 400, new VertexBufferObjectManager()) 
		{
			public boolean onAreaTouched(org.andengine.input.touch.TouchEvent pSceneTouchEvent, 
					float pTouchAreaLocalX, float pTouchAreaLocalY) 
			{
				MainState.HideMainScene();
				return true;
			};			
		};
		
		attachChild(_rec);
		_rec.setColor(Color.BLUE);
		registerTouchArea(_rec);
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
}

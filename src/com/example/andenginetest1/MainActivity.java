package com.example.andenginetest1;

import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.engine.camera.*;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
 
import android.view.KeyEvent;
 
public class MainActivity extends SimpleBaseGameActivity{
	
	public TiledTextureRegion Bird_TR;
	public TextureRegion Pipe_TR;
	
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
		 /**
         * Указываем путь до графики. В данном случае графика будет загружаться из папки assets/gfx/
         */
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
 
        /**
         * Создаем Атлас, в который будем загружать графику.
         * TextureOptions - задаёт режим вывода графики на экран.
         * Если вы планируете масштабировать спрайты, изменять прозрачность и спрайты
         * имеют изначально размыте края, то лучше ставить BILINEAR_PREMULTIPLYALPHA
         * Тут нужно эскпериментировать
         * Бывает:
         *      NEAREST
         *      NEAREST_PREMULTIPLYALPHA
         *      BILINEAR
         *      BILINEAR_PREMULTIPLYALPHA
         *      и др.
         */
        TextureManager tm = mEngine.getTextureManager();
        BitmapTextureAtlas Texture1 = new BitmapTextureAtlas(tm, 128, 64, TextureOptions.NEAREST_PREMULTIPLYALPHA);
        BitmapTextureAtlas Texture2 = new BitmapTextureAtlas(tm,  64, 128, TextureOptions.NEAREST_PREMULTIPLYALPHA);
 
        /**
         * Создаем регион (область) для спрайта в этом атласе.
         * Нужно указать координаты региона в Атласе (0,0 в данном случае)
         */
        
        Bird_TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset (Texture1, this, "bird_winged_anim_clear_bg.png", 0, 0, 2, 1);
        Pipe_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset (Texture2, this, "pipe.png", 0, 0);
 
        /**
         * Теперь всё готово. Загружаем Атлас
         */
        tm.loadTexture(Texture1);
        tm.loadTexture(Texture2);
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
package com.obscourse.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyDemo extends ApplicationAdapter {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;
	public static final String TITLE = "Don't Fly Underground";
	public static Preferences prefs;
	private com.obscourse.game.States.GameStateManager gsm;
	private SpriteBatch batch;
	private Music music;

	@Override
	public void create () {
		music = Gdx.audio.newMusic(Gdx.files.internal("DipteraSonata.mp3"));
		music.setLooping(true);
		music.setVolume(0.1f);
		music.play();
		batch = new SpriteBatch();
		gsm = new com.obscourse.game.States.GameStateManager();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		gsm.push(new com.obscourse.game.States.MenuState(gsm));
		prefs = Gdx.app.getPreferences("OverHere");
		if(!prefs.contains("HighScore"))
		{
			prefs.putInteger("HighScore", 0);
			prefs.flush();
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}

	@Override
	public void pause() {
		super.pause();
		gsm.peek().pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void dispose () {
		music.dispose();
		batch.dispose();
	}
}

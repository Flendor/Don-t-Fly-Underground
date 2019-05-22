package com.obscourse.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.obscourse.game.Sprites.Bird;
import com.obscourse.game.FlappyDemo;

public class MenuState extends State {
    private Texture background;
    private BitmapFont yourBitmapFontName;
    private GlyphLayout Layout, Layout2, Layout3;
    private Bird bird;
    private static final int GROUNDDOWN_Y_OFFSET = -65;
    private static final int GROUNDUP_Y_OFFSET = -40;
    private Texture groundUp, groundDown;
    private Vector2 groundDownpos1, groundUppos1;
    public MenuState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird(50,250,0);
        background = new Texture("bg1.png");
        groundDown = new Texture("ground1.png");
        groundUp = new Texture("groundUp1.png");
        yourBitmapFontName = new BitmapFont(Gdx.files.internal("font.fnt"));
        yourBitmapFontName.setColor(1,1,1,0.5f);
        yourBitmapFontName.getData().setScale(0.5f);
        yourBitmapFontName.setUseIntegerPositions(false);
        Layout = new GlyphLayout(yourBitmapFontName, "Tap to play");
        Layout2 = new GlyphLayout(yourBitmapFontName, "Then tap to" );
        Layout3 = new GlyphLayout(yourBitmapFontName, "reverse gravity");
        cam.setToOrtho(false, com.obscourse.game.FlappyDemo.WIDTH/2, com.obscourse.game.FlappyDemo.HEIGHT/2);
        groundUppos1 = new Vector2(cam.position.x - cam.viewportWidth/2, FlappyDemo.HEIGHT/2 + GROUNDUP_Y_OFFSET);
        groundDownpos1 = new Vector2(cam.position.x - cam.viewportWidth/2, GROUNDDOWN_Y_OFFSET);
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()){
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void update(float dt) {
        handleInput();
        bird.update(dt,0);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, cam.position.x - (cam.viewportWidth/2), 0, FlappyDemo.WIDTH/2, FlappyDemo.HEIGHT/2);
        yourBitmapFontName.draw(sb, Layout, cam.position.x - Layout.width/2, cam.position.y * 1.65f);
        yourBitmapFontName.draw(sb, Layout2, cam.position.x - Layout2.width/2, cam.position.y * 0.7f);
        yourBitmapFontName.draw(sb, Layout3, cam.position.x - Layout3.width/2, cam.position.y * 0.55f);
        sb.draw(groundDown, groundDownpos1.x, groundDownpos1.y);
        sb.draw(groundUp, groundUppos1.x, groundUppos1.y);
        sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        bird.dispose();
        groundDown.dispose();
        groundUp.dispose();
        System.out.println("Menu State Disposed");
    }

    @Override
    public void resume() {
        bird.dispose();
        bird = new Bird(50,250,0);
    }
}


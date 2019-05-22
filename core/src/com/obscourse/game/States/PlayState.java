package com.obscourse.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.obscourse.game.FlappyDemo;
import com.obscourse.game.Sprites.Bird;
import com.obscourse.game.Sprites.Tube;


public class PlayState extends State {
    private Array<Tube> tubes;
    private static final int TUBE_SPACING = 150;
    private static final int TUBE_COUNT = 4;
    private Bird bird;
    private static final int GRAVITY = -15;
    private int movement = 85;
    private int max_movement = 160;
    private Texture background;
    private static final int GROUNDDOWN_Y_OFFSET = -65;
    private static final int GROUNDUP_Y_OFFSET = -40;
    private Texture groundUp, groundDown, ground2Down, ground2Up;
    private Vector2 groundDownpos1, groundDownpos2, groundUppos1, groundUppos2, ground2Downpos1, ground2Downpos2, ground2Uppos1, ground2Uppos2;
    private Music collision;
    private int score;
    private String yourScoreName;
    private BitmapFont yourBitmapFontName;
    private BitmapFont yourBitmapFontName2;
    private int ntube;
    private GlyphLayout Layout;
    private boolean isPlaying;
    private GlyphLayout Layout2;
    private GlyphLayout Layout3;
    private GlyphLayout Layout4;
    private Sound Dying;

    PlayState(GameStateManager gsm) {
        super(gsm);
        Dying = Gdx.audio.newSound(Gdx.files.internal("DyingSound.mp3"));
        background = new Texture("bg1.png");
        groundDown = new Texture("ground1.png");
        groundUp = new Texture("groundUp1.png");
        ground2Down = new Texture("ground2.png");
        ground2Up = new Texture("groundUp2.png");
        groundUppos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, FlappyDemo.HEIGHT / 2 + GROUNDUP_Y_OFFSET);
        groundUppos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + groundUp.getWidth(), FlappyDemo.HEIGHT / 2 + GROUNDUP_Y_OFFSET);
        groundDownpos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, GROUNDDOWN_Y_OFFSET);
        groundDownpos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + groundDown.getWidth(), GROUNDDOWN_Y_OFFSET);
        ground2Downpos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, GROUNDDOWN_Y_OFFSET);
        ground2Downpos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + groundDown.getWidth(), GROUNDDOWN_Y_OFFSET);
        ground2Uppos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, FlappyDemo.HEIGHT / 2 + GROUNDUP_Y_OFFSET);
        ground2Uppos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + groundUp.getWidth(), FlappyDemo.HEIGHT / 2 + GROUNDUP_Y_OFFSET);
        cam.setToOrtho(false, com.obscourse.game.FlappyDemo.WIDTH / 2, com.obscourse.game.FlappyDemo.HEIGHT / 2);
        bird = new Bird(50, 250, GRAVITY);
        tubes = new Array<Tube>();
        tubes.add(new Tube(300));
        for (int i = 2; i <= TUBE_COUNT; i++)
            tubes.add(new Tube((i - 1) * (TUBE_SPACING + Tube.TUBE_WIDTH) + 300));
        ntube = 0;
        collision = Gdx.audio.newMusic(Gdx.files.internal("Collision.mp3"));
        score = 0;
        yourScoreName = "0";
        yourBitmapFontName2 = new BitmapFont(Gdx.files.internal("font.fnt"));
        yourBitmapFontName2.setColor(0.75f, 0.75f, 0.75f, 1);
        yourBitmapFontName2.getData().setScale(0.6f);
        yourBitmapFontName2.setUseIntegerPositions(false);
        yourBitmapFontName = new BitmapFont(Gdx.files.internal("font.fnt"));
        yourBitmapFontName.setColor(1, 1, 1, 0.5f);
        yourBitmapFontName.getData().setScale(0.5f);
        yourBitmapFontName.setUseIntegerPositions(false);
        Layout = new GlyphLayout(yourBitmapFontName, "0");
        Layout2 = new GlyphLayout(yourBitmapFontName, "0");
        Layout3 = new GlyphLayout(yourBitmapFontName, "0");
        Layout4 = new GlyphLayout(yourBitmapFontName2, "0");
        isPlaying = true;
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched())
            bird.jump();
    }

    @Override
    public void update(float dt) {
        if (dt == 0)
            return;
        if (isPlaying) {
            handleInput();
            updateGround();
            bird.update(dt, movement);
            cam.position.x = bird.getPosition().x + 80;
            for (int i = 0; i < tubes.size; i++) {
                Tube tube = tubes.get(i);
                if (tube.getIsTop()) {
                    if (cam.position.x - cam.viewportWidth / 2 > tube.getPosTopTube().x + tube.getTopTube().getWidth())
                        tube.reposition(tube.getPosTopTube().x + (Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT);
                } else {
                    if (cam.position.x - cam.viewportWidth / 2 > tube.getPosBotTube().x + tube.getBottomTube().getWidth())
                        tube.reposition(tube.getPosBotTube().x + (Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT);
                }
                if (tube.collides(bird.getBounds())) {
                    if (score > FlappyDemo.prefs.getInteger("HighScore")) {
                        FlappyDemo.prefs.putInteger("HighScore", score);
                        FlappyDemo.prefs.flush();
                    }
                    isPlaying = false;
                    Dying.play(0.3f);
                }
            }
            if (tubes.get(ntube).getIsTop()) {
                if (bird.getPosition().x >= tubes.get(ntube).getPosTopTube().x) {
                    score++;
                    yourScoreName = Integer.toString(score);
                    ntube = (ntube + 1) % 4;
                    if (movement < max_movement)
                        movement = movement + 3;
                }
            } else if (bird.getPosition().x >= tubes.get(ntube).getPosBotTube().x) {
                score++;
                yourScoreName = Integer.toString(score);
                ntube = (ntube + 1) % 4;
                if (movement < max_movement)
                    movement = movement + 3;
            }
            cam.update();
            if (bird.getPosition().y <= groundDown.getHeight() + GROUNDDOWN_Y_OFFSET - 10) {
                bird.getPosition().y = groundDown.getHeight() + GROUNDDOWN_Y_OFFSET - 10;
            }
            if (bird.getPosition().y >= FlappyDemo.HEIGHT / 2 + GROUNDUP_Y_OFFSET - bird.getTexture().getRegionHeight() + 10) {
                bird.getPosition().y = FlappyDemo.HEIGHT / 2 + GROUNDUP_Y_OFFSET - bird.getTexture().getRegionHeight() + 10;
            }
        } else {
            if (Gdx.input.justTouched())
                gsm.set(new MenuState(gsm));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, cam.position.x - (cam.viewportWidth / 2), 0, FlappyDemo.WIDTH / 2, FlappyDemo.HEIGHT / 2);
        sb.draw(groundDown, groundDownpos1.x, groundDownpos1.y);
        sb.draw(groundDown, groundDownpos2.x, groundDownpos2.y);
        sb.draw(groundUp, groundUppos1.x, groundUppos1.y);
        sb.draw(groundUp, groundUppos2.x, groundUppos2.y);
        for (Tube tube : tubes) {
            if (tube.getIsTop())
                sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            if (tube.getIsBot())
                sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }
        sb.draw(ground2Down, ground2Downpos1.x, ground2Downpos1.y);
        sb.draw(ground2Down, ground2Downpos2.x, ground2Downpos2.y);
        sb.draw(ground2Up, ground2Uppos1.x, ground2Uppos1.y + 12);
        sb.draw(ground2Up, ground2Uppos2.x, ground2Uppos2.y + 12);
        sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
        if (isPlaying) {
            Layout.setText(yourBitmapFontName, yourScoreName);
            yourBitmapFontName.draw(sb, Layout, cam.position.x - Layout.width / 2, cam.position.y * 1.65f);
        }
        if (!isPlaying) {
            Layout4.setText(yourBitmapFontName2, "YOU LOST");
            yourBitmapFontName2.draw(sb, Layout4, cam.position.x - Layout4.width / 2, cam.position.y * 1.75f);
            Layout2.setText(yourBitmapFontName, "High Score: " + Integer.toString(FlappyDemo.prefs.getInteger("HighScore")));
            yourBitmapFontName.draw(sb, Layout2, cam.position.x - Layout2.width / 2, cam.position.y * 1.35f);
            Layout.setText(yourBitmapFontName, "Score: " + Integer.toString(score));
            yourBitmapFontName.draw(sb, Layout, cam.position.x - Layout.width / 2, cam.position.y * 1.15f);
            Layout3.setText(yourBitmapFontName, "Tap to play again");
            yourBitmapFontName.draw(sb, Layout3, cam.position.x - Layout3.width / 2, cam.position.y * 0.55f);
        }
        sb.end();
    }

    private void updateGround() {
        if (cam.position.x - cam.viewportWidth / 2 > groundDownpos1.x + groundDown.getWidth())
            groundDownpos1.add(groundDown.getWidth() * 2, 0);
        if (cam.position.x - cam.viewportWidth / 2 > groundDownpos2.x + groundDown.getWidth())
            groundDownpos2.add(groundDown.getWidth() * 2, 0);
        if (cam.position.x - cam.viewportWidth / 2 > groundUppos1.x + groundUp.getWidth())
            groundUppos1.add(groundUp.getWidth() * 2, 0);
        if (cam.position.x - cam.viewportWidth / 2 > groundUppos2.x + groundUp.getWidth())
            groundUppos2.add(groundUp.getWidth() * 2, 0);
        if (cam.position.x - cam.viewportWidth / 2 > ground2Downpos1.x + ground2Down.getWidth())
            ground2Downpos1.add(ground2Down.getWidth() * 2, 0);
        if (cam.position.x - cam.viewportWidth / 2 > ground2Downpos2.x + ground2Down.getWidth())
            ground2Downpos2.add(ground2Down.getWidth() * 2, 0);
        if (cam.position.x - cam.viewportWidth / 2 > ground2Uppos1.x + ground2Up.getWidth())
            ground2Uppos1.add(ground2Up.getWidth() * 2, 0);
        if (cam.position.x - cam.viewportWidth / 2 > ground2Uppos2.x + ground2Up.getWidth())
            ground2Uppos2.add(ground2Up.getWidth() * 2, 0);
    }

    @Override
    public void dispose() {
        background.dispose();
        bird.dispose();
        groundDown.dispose();
        groundUp.dispose();
        ground2Down.dispose();
        ground2Up.dispose();
        collision.dispose();
        for (Tube tube : tubes)
            tube.dispose();
        Dying.dispose();
        System.out.println("Play State Disposed");
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        bird.dispose();
        bird = new Bird(50, 250, 0);
    }

}


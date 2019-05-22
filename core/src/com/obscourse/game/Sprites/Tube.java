package com.obscourse.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.obscourse.game.FlappyDemo;

import java.util.Random;

public class Tube
{
    public static final int TUBE_WIDTH = 52;
    private static final int FLUCTUATION = 200;
    private static final int TUBE_GAP = 100;
    private static final int LOWEST_OPENING = 62;
    private Texture topTube, bottomTube;
    private Vector2 posTopTube, posBotTube;
    private Random rand;
    private boolean okTop = false;
    private boolean okBot = false;
    private Rectangle boundsTop, boundsBot;
    private int topy, boty;
    public Tube(float x)
    {
        rand = new Random();
        topTube=new Texture("TopTube1.png");
        bottomTube=new Texture("BotTube1.png");
        posTopTube = new Vector2();
        posBotTube = new Vector2();
        boundsTop = new Rectangle();
        boundsBot = new Rectangle();
        boundsTop.setSize(topTube.getWidth(),topTube.getHeight());
        boundsBot.setSize(bottomTube.getWidth(),bottomTube.getHeight());
        reposition(x);
    }
    public boolean getIsTop() { return okTop; }

    public boolean getIsBot() { return okBot; }

    public Vector2 getPosTopTube() {
        return posTopTube;
    }

    public Texture getTopTube() {
        return topTube;
    }

    public Texture getBottomTube() {
        return bottomTube;
    }

    public Vector2 getPosBotTube() {
        return posBotTube;
    }

    public void reposition(float x)
    {
        okTop = false;
        okBot = false;
        if(rand.nextInt(2) == 1) {
            topy = rand.nextInt(FLUCTUATION) +75;
            while(topy >= FlappyDemo.HEIGHT/4)
                topy = rand.nextInt(FLUCTUATION) + 75;
            posTopTube.set(x, topy);
            boundsTop.setPosition(posTopTube.x, posTopTube.y);
            okTop = true;
            if(rand.nextInt(2)==1){
                if(posTopTube.y - TUBE_GAP< LOWEST_OPENING)
                {   posTopTube.y = topy + TUBE_GAP;
                    boundsTop.setPosition(posTopTube.x, posTopTube.y);}
                posBotTube.set(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());
                boundsBot.setPosition(posBotTube.x, posBotTube.y);
                okBot = true;
            }
        }
        else
        {
            boty = rand.nextInt(FLUCTUATION) + 130 - bottomTube.getHeight();
            while(boty + bottomTube.getHeight()<= FlappyDemo.HEIGHT/4)
                boty = rand.nextInt(FLUCTUATION) + 130 - bottomTube.getHeight();
            posBotTube.set(x, boty);
            boundsBot.setPosition(posBotTube.x, posBotTube.y);
            okBot = true;
            if(rand.nextInt(2)==1)
            {
                if(FlappyDemo.HEIGHT/2 - (posBotTube.y + TUBE_GAP + bottomTube.getHeight()) < 55)
                {   posBotTube.y = boty - TUBE_GAP;
                    boundsBot.setPosition(posBotTube.x, posBotTube.y);}
                posTopTube.set(x, posBotTube.y + TUBE_GAP + bottomTube.getHeight());
                boundsTop.setPosition(posTopTube.x, posTopTube.y);
                okTop = true;
            }
        }

    }
    public boolean collides(Rectangle player)
    {
        return player.overlaps(boundsTop) || player.overlaps(boundsBot);
    }
    public void dispose()
    {
        topTube.dispose();
        bottomTube.dispose();
    }
}


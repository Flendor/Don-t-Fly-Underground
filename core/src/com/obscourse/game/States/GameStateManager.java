package com.obscourse.game.States;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameStateManager {
    private java.util.Stack<State> states;
    public GameStateManager(){
        states=new java.util.Stack<State>();
    }
    public void push(State state) {
        states.push(state);
    }
    public void pop(){
        states.pop().dispose();
    }
    void set(State state){
        states.pop().dispose();
        states.push(state);
    }
    public State peek()
    {
        return states.peek();
    }
    public void update(float dt){
        states.peek().update(dt);
    }
    public void render (SpriteBatch sb){
        states.peek().render(sb);
    }
}


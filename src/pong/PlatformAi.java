package pong;

import pong.Platform;

public abstract class PlatformAi
{
    /* --- Construction and final properties --- */

    private final Platform paddle;
    private final Game game;
    
    protected PlatformAi(Platform paddle, Game game)
    {
        this.paddle = paddle;
        this.game = game;
    }

    public Game getGame()
    {
        return game;
    }
    
    public Platform getPaddle()
    {
        return paddle;
    }

    /* --- Update --- */
    
    /*
     * Updates the state of the paddle based on the amount of time (in seconds) passed since the
     * previous update.
     */
    public abstract void update(double deltaTime);
    
}

package pong;

import static pong.Config.*;

public class DefaultAi extends PlatformAi
{
    /* --- Construction and final properties --- */
    
    private static final double BetweenUpdateTime = 0.2;
    
    public DefaultAi(Platform platform, Game game)
    {
        super(platform, game);
    }

    /* --- Update --- */
    
    private double PassedLastUpdateTime = 0;
    
    @Override
    public void update(double deltaTime)
    {
    	PassedLastUpdateTime += deltaTime;
        
        if (PassedLastUpdateTime < BetweenUpdateTime) {
            return; /* Wait a while longer. */
        }
        
        PassedLastUpdateTime -= BetweenUpdateTime;
        
        double distanceFromPlatform = getPaddle().getX() - getGame().getBall().getX();
        
        /*
         * Do nothing if the ball is not moving towards us.
         */
        if (Math.signum(distanceFromPlatform) != Math.signum(getGame().getBall().getBallSpeed())) {
            getPaddle().setMovement(Platform.Movement.NONE);
            return;
        }

        /*
         * Find out where the ball is heading for and move in that direction (this does not look
         * ahead past collisions).
         */
        double targetY = getGame().getBall().getY() + distanceFromPlatform * Math.tan(getGame().getBall().getBallAngle());
        boolean currentPlatform = targetY >= getPaddle().getY() && targetY + BallSize <= getPaddle().getY() + PlatformHeight;
        if (currentPlatform) {
            getPaddle().setMovement(Platform.Movement.NONE);
        } else if (targetY < getPaddle().getY()) {
            getPaddle().setMovement(Platform.Movement.UP);
        } else if (targetY > getPaddle().getY()) {
            getPaddle().setMovement(Platform.Movement.DOWN);
        }
    }
}

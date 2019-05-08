package pong;

import static pong.Config.*;

import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.scene.media.AudioClip;
import pong.Ball;
import pong.Platform;

public class Game
{
    /* --- Construction and final properties --- */
    
    private static final Random rand = new Random();
    
    private final int maxPoints;
    
    public Game(int maxScore)
    {
        this.maxPoints = maxScore;
        loop.start();
    }

    public int getWinPoints()
    {
        return maxPoints;
    }
    
    /* --- Game loop --- */
    
    /*
     * This is an implementation of a game loop using variable time steps. See the blog posts on
     * game loops in JavaFX for more information.
     */
    private class PlayLoop extends AnimationTimer
    {
        private long pastTime = 0;
        
        //Fps updatering av spel
        @Override
        public void handle(long currentTime)
        {
            /*
             * If this is the first frame, simply record an initial time.
             */
            if (pastTime == 0) {
                pastTime = currentTime;
                return;
            }

            double passedTime = (currentTime - pastTime) / 1_000_000_000.0; /* Convert nanoseconds to seconds. */

            /*
             * Avoid large time steps by imposing an upper bound.
             */
            if (passedTime > 0.0333) {
                passedTime = 0.0333;
            }
            
            updateGame(passedTime);
            
            pastTime = currentTime;
        }
    }
    
    private final PlayLoop loop = new PlayLoop();
    
    /* --- State --- */
    
    public enum State
    {
        GAME_PLAYING, GAME_PAUSED, GAME_ENDED;
    }
    
    private State game_state = State.GAME_ENDED;
    
    public State getStatus()
    {
        return game_state;
    }

    private Runnable onGameEnd = () -> {}; /* Do nothing for now. */
    
    public void setOnGameEnd(Runnable onGameEnd)
    {
        this.onGameEnd = onGameEnd;
    }
    
    public void start()
    {
        player.setX(MarginLR + PlatformDistance - PlatformWidth); /* Aligned with the goal area. */
        player.setY((HEIGHT - PlatformHeight) / 2); /* Centered. */
        
        computer.setX(WIDTH - MarginLR - PlatformDistance); /* Aligned with the goal area. */
        computer.setY((HEIGHT - PlatformHeight) / 2); /* Centered. */
        
        player.setScore(0);
        computer.setScore(0);
        
        player.setMovement(Platform.Movement.NONE);
        computer.setMovement(Platform.Movement.NONE);
        
        ReleaseBall();
        
        game_state = State.GAME_PLAYING;
    }

    public void pause()
    {
        if (game_state == State.GAME_PAUSED) {
            game_state = State.GAME_PLAYING;
        } else if (game_state == State.GAME_PLAYING) {
            game_state = State.GAME_PAUSED;
        }
    }

    public void endedGame()
    {
        player.setScore(0);
        computer.setScore(maxPoints);
        game_state = State.GAME_ENDED;
        onGameEnd.run();
    }
    
    /* --- Ball --- */
    
    private final Ball ball = new Ball(MaxSpeed);

    public Ball getBall()
    {
        return ball;
    }
    
    public void ReleaseBall()
    {
        boolean towardsOpponent = rand.nextBoolean();
        double initialAngle = PlatformSectionAngels[rand.nextInt(2) + 1]; /* We don't use the steepest angle. */
        
        ball.setBallSpeed(towardsOpponent ? -BALL_INITIAL_SPEED : BALL_INITIAL_SPEED);
        ball.setAngle(towardsOpponent ? -initialAngle : initialAngle);
        ball.setX((WIDTH - BallSize) / 2); /* Centered. */
        ball.setY(MarginTB);
    }
    
    /* --- Player --- */
    
    private final Platform player = new Platform(UserPlatformSpeed);
    
    public Platform getPlayer()
    {
        return player;
    }
    
    /* --- Opponent --- */
    
    private final Platform computer = new Platform(ComputerPlatformSpeed);
    private final PlatformAi ai = new DefaultAi(computer, this);
    
    public Platform getComputer()
    {
        return computer;
    }
    
    /* --- Update --- */
    
    private void updateGame(double deltaTime)
    {
        if (game_state == State.GAME_PAUSED || game_state == State.GAME_ENDED) {
            return; /* This is necessary because the loop keeps running even when the game is paused or stopped. */
        }
        
        player.update(deltaTime);
        computer.update(deltaTime);
        
       // keepPaddleInBounds(player);
        keepPaddleInBounds(computer);
        
        ball.update(deltaTime++);
        
        
        checkWallCollision();
        checkPaddleOrEdgeCollision(player);
        checkPaddleOrEdgeCollision(computer);
        
        ai.update(deltaTime*2);
    }
    
    /* --- Collision detection --- */
    
    private void keepPaddleInBounds(Platform paddle)
    {
        if (paddle.getY() < MarginTB) {
            paddle.setY(MarginTB);
        } else if (paddle.getY() + PlatformHeight > HEIGHT - MarginTB) {
            paddle.setY(HEIGHT - MarginTB - PlatformHeight);
        }
    }
    
    private void checkWallCollision()
    {
        
        if (ball.getY() < MarginTop || ball.getY() + BallSize > HEIGHT - MarginBottom) {
            ball.setAngle(ball.getBallAngle() * -1);
            new AudioClip(Sounds.HIT_WALL).play();
        }
        
        if (ball.getY() < MarginTop) {
            ball.setY(MarginTop);
        } else if (ball.getY() + BallSize > HEIGHT - MarginBottom) {
            ball.setY(HEIGHT - MarginBottom - BallSize);
        }
    }
    
    private void checkPaddleOrEdgeCollision(Platform platform)
    {
        boolean ballHitEdge;
        if (platform == player) {
            ballHitEdge = ball.getX() < MarginLR + PlatformDistance;
        } else {
            ballHitEdge = ball.getX() + BallSize > WIDTH - MarginLR - PlatformDistance;
        }
        if (!ballHitEdge) {
            return;
        }
        
        boolean ballHitPaddle = ball.getY() + BallSize > platform.getY() && ball.getY() < platform.getY() + PlatformHeight;
        if (ballHitPaddle) {
            
            /*
             * Find out what section of the paddle was hit (for computer).
             */
            for (int i = 0; i < PlatformSection; i++) {
                boolean ballHitCurrentSection = ball.getY() < platform.getY() + (i + 1) * PlatformSectionHeight;
                if (ballHitCurrentSection) {
                    ball.setAngle(PlatformSectionAngels[i] * (platform == computer ? 1 : -1.5));
                    break; /* Found our match. */
                } else if (i == PlatformSection - 1) { /* If we haven't found our match by now, it must be the last section. */
                    ball.setAngle(PlatformSectionAngels[i] * (platform == computer ? -1 : 1));
                }
            }
            
            /*
             * Update and reposition the ball.
             */
            ball.setBallSpeed(ball.getBallSpeed() * SpeedIncrease);
            if (platform == player) {
                ball.setX(MarginLR + PlatformDistance);
            } else {
                ball.setX(WIDTH - MarginLR - PlatformDistance - BallSize);
            }
            new AudioClip(Sounds.HIT_PADDLE).play();
            
        } else {
            
            /*
             * Update the score.
             */
            if (platform == computer) {
                player.setScore(player.getScore() + 1);
                new AudioClip(Sounds.SCORE_PLAYER).play();
            } else {
                computer.setScore(computer.getScore() + 1);
                new AudioClip(Sounds.SCORE_OPPONENT).play();
            }
            
            /*
             * Check if the game has ended. If not, play another round.
             */
            if (player.getScore() == maxPoints || computer.getScore() == maxPoints) {
                game_state = State.GAME_ENDED;
                onGameEnd.run();
            } else {
                ReleaseBall();
            }
        }
    }
}

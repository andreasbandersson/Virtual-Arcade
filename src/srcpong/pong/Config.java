package pong;

import static pong.Utilities.degreesToRadians;

/*
 * Note: the units used in this class (and by extension the rest of the source code) are pixels
 * (size) and pixels per second (speed), unless stated otherwise. |All sizes are to be understood as
 * unscaled sizes, unless stated otherwise.|
 */
public class Config
{   
    private Config() {}
    
    /* --- Size and layout --- */
    
    /*
     * Total size of the game, including margins. This size is fixed but the game will be scaled to
     * fill the available space.
     */
    public static final double WIDTH = 600;
    public static final double HEIGHT = 400;
    
    /*
     * Distans mellan kanten och bollen när den träffar
     */
    public static final double MarginTop = 10;
    public static final double MarginBottom = 60;
    public static final double MarginTB = 60;

    public static final double MarginLR = 10;
    
    /*
     * Size of the goal areas. These are special edge areas within the playing field. A point is
     * scored when the ball enters a goal area. The paddles are placed just inside the goal areas.
     * The inner edge of a paddle is aligned with the inner edge of the goal area it defends.
     */
    
    //Avståndet mellan fönsterkanten och plattformarna
    public static final double PlatformDistance = 30;

    /*
     * Size of the margins around the text. These margins are independent from the margins of the
     * playing field.
     */
    
    //Avståndet mellan "Welcome" texten och kanten
    public static final double TxtMarginTB = 30;
    
    /*
     * Size of the space between the player and opponent scores.
     */
    public static final double ScoreDistance = 90;

    /* --- Ball --- */
    
    /*
     * Size of the ball.
     */
    public static final double BallSize = 10;
    
    /*
     * Initial speed of the ball. This speed is negative, so the ball moves towards the player by
     * default.
     */
    public static final double BALL_INITIAL_SPEED = 400;
    
    /*
     * The speed of the ball is multiplied by this factor every time the ball hits a paddle. This
     * factor is negative because the direction of the ball changes as well.
     */
    public static final double SpeedIncrease = -5;
    
    /*
     * The maximum speed of the ball, in any direction.
     */
    public static final double MaxSpeed = 500;

    /* --- Paddle --- */
    
    /*
     * The speeds at which the paddles travel.
     */
    public static final double UserPlatformSpeed = 400;
    public static final double ComputerPlatformSpeed = 400;
    
    /*
     * Size of the paddles. Note that the height of a paddle is expressed in sections. The height of
     * a section is currently the same as the size of the ball.
     */
    public static final double PlatformWidth = 10;
    public static final int PlatformSection = 6;
    public static final double PlatformSectionHeight = BallSize; 
    public static final double PlatformHeight = PlatformSection * PlatformSectionHeight;
    
    /*
     * Every section of a paddle has an angle associated with it. This is the angle at which the
     * ball is reflected when the ball hits the paddle at this section. The angles given here are
     * for the player paddle. Multiply them by -1 to get the angles for the opponent paddle.
     */
    public static final double[] PlatformSectionAngels = new double[] {degreesToRadians(-65),
                                                                       degreesToRadians(-45),
                                                                       degreesToRadians(-25),
                                                                       degreesToRadians(0),
                                                                       degreesToRadians(0),
                                                                       degreesToRadians(25),
                                                                       degreesToRadians(45),
                                                                       degreesToRadians(65)};
    
    /* --- Game --- */
    
    /*
     * The score that signals the end of the game.
     */
    public static final int WinScore = 5;   
}

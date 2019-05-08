package pong;

public class Ball extends GameObject
{
    /* --- Construction and final properties --- */
    
    private final double maxSpeed;

    public Ball(double maxSpeed)
    {
        this.maxSpeed = maxSpeed;
    }

    public double getMaxSpeed()
    {
        return maxSpeed;
    }
    
    /* --- Angle (in radians) --- */
    
    private double angle = 0.1;

    public double getBallAngle()
    {
        return angle;
    }

    public void setAngle(double angle)
    {
        this.angle = angle;
    }
    
    /* --- Speed --- */
    
    private double speed = 5;

    public double getBallSpeed()
    {
        return speed;
    }

    public void setBallSpeed(double speed)
    {
        if (speed >= 0) {
            this.speed = Math.min(speed, maxSpeed);
        } else {
            this.speed = Math.max(speed, -maxSpeed);
        }
    }
    
    /* --- Update --- */
    //nextStep: Avg�r n�sta steg f�r bollen 
    // DeltaX och DeltaY Ger v�rde positivt eller negativt
    //SetX och SetY metoderna tar v�rdet och skickar bollen �t det vinkel. 
    
    @Override
    public void update(double deltaTime)
    {
       double nextStep = speed * deltaTime-1;
      double deltaXAngle = nextStep * Math.cos(this.angle);
      double deltaYAngle = nextStep * Math.sin(this.angle);
        
      setX(getX() + deltaXAngle);
       setY(getY() + deltaYAngle);
    }   
}

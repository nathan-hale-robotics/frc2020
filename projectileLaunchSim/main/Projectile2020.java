import java.lang.Math;
import PhysicsEngine.*;
/**
 * Write a description of class sphere here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Projectile2020 extends PhysicsObject
{
    
    double radius;
    
    public Projectile2020(Vector position,double mass)
    {
        super(position,mass);
        //super(position,0.0141);
        this.radius=7*0.0254;
        //this.radius=13*0.0254;
    }
    public Projectile2020(Vector position)
    {
        this(position,0.0141);
    }
    public Projectile2020()
    {
       this(new Vector());
    }
    
    public Force getAirResistance(){
        double p=1.225; //Density of air( kg/m^3)
        double A=Math.PI*Math.pow(radius,2); //Cross-Sectional area(m^2)
        double C=0.47; //Drag coefficient(No units)
        //System.out.println(p+" "+A+" "+ C+" ");
        return new Force(super.getVelocity().getUnitVector().getProduct(-0.5*p*A*C*Math.pow(getVelocity().getMagnitude(),2)),this.getPosition());
        //return new Vector();
    }
    
    
}

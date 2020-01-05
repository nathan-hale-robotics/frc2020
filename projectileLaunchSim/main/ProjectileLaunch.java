import PhysicsEngine.*;
import java.util.ArrayList;
/**
 * This is a demonstration of my physics engine. This program simulates the dropping of differently massed spheres from 5 meters, with air resistance taken into account.
 *
 * @Nicholas
 * @1.0
 */


public class ProjectileLaunch
{
    public static void main(String[] args){
    System.out.println();
    for(double mass=0;mass<=0.02;mass+=0.0001){
        
        Projectile2020 ball = new Projectile2020(new Vector(),mass);
        
        //Print results of simulation 
        System.out.printf("%.3f s to fall 5 m with %.1f g ball\n",simulateFreefall(ball,5),1000*ball.getMass());
        if (String.format("%.4f",ball.getMass()).equals(String.format("%.4f",0.0141))){
            System.out.println("14.1 g is the actual mass of our projectile.");
        }
    }
    }
    public static double simulateFreefall(PhysicsObject ball,double height){
        FlatPlanet field=new FlatPlanet();
        Universe sim= new Universe(new ArrayList<PhysicsObject>());
        sim.addChild(field);
        field.addChild(ball);
        
        //System.out.println(ball.getPosition());
        
        field.setAirResistance(true);
        //Executing simulation
        for(ball.getPosition().set(0,0,5);ball.getPosition().getZ()>0;sim.addTime(0.001)){
            
            ball.addForce(ball.getGravity(field));
            ball.addForce(ball.getAirResistance());
            //System.out.println(sim.getTime()+" "+ball.getPosition().getZ()+" "+ball.getVelocity().getZ()+" "+ball.getAcceleration().getZ());
            //System.out.println(ball.getGravity(field)+" "+ball.getAirResistance());
            sim.update();
            
        }
        return sim.getTime();
    }
}

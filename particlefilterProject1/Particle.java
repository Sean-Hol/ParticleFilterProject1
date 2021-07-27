package particlefilterProject1;

import java.lang.reflect.Array;
import java.util.Random;

/** The object for a single particle
 *
 * @author Sean-Hol
 */
public class Particle {
    
    private float x;    //x pos of particle
    private float y;    //y pos of particle
    private float yew;  //angle the particle is facing in radians
    private float weight = 1f;
    private float moveNoise;
    private float turnNoise;
    private float senseNoise;
    private Random random = new Random();
    private float minWorldX;
    private float minWorldY;
    private float maxWorldX;
    private float maxWorldY;

    public Particle(float x, float y, float yew, float minWorldX, float minWorldY, float maxWorldX, float maxWorldY) {
        this.x = x;
        this.y = y;
        this.yew = yew;
        this.minWorldX = minWorldX;
        this.minWorldY = minWorldY;
        this.maxWorldX = maxWorldX;
        this.maxWorldY = maxWorldY;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
  
    
    public void setNoises(float moveNoise, float turnNoise, float senseNoise) {
        this.moveNoise = moveNoise;
        this.turnNoise = turnNoise;
        this.senseNoise = senseNoise;
    }
            
    public void step(float dist, float turn) {
        
        yew = loopBack(yew + turn + ((float)random.nextGaussian() * turnNoise), 0, 2);
        x = loopBack(x +(float)Math.cos(yew * (float)(Math.PI))*(dist+((float)random.nextGaussian()* moveNoise)), 0, maxWorldX);
        y = loopBack(y +(float)Math.sin(yew* (float)(Math.PI))*(dist+((float)random.nextGaussian()* moveNoise)), 0, maxWorldY);
    }
       
    public float loopBack(float counter, float lowerBoundary, float upperBoundary) {
        if (counter > upperBoundary) {
            counter -= upperBoundary;
        }
        else if (counter < lowerBoundary) {
            counter += upperBoundary;
        }
        return counter;
    }
    
    public float weightCalc(float targetDist, float landmarks[]) {
        float d = this.dist(landmarks);
        weight *= (float)Math.exp(-((float)Math.pow(d - targetDist, 2)) / ((float)Math.pow(senseNoise, 2) * 2.0)) / (float)Math.sqrt(2.0 * Math.PI * Math.pow(senseNoise, 2));       
        return weight; 
    }
    
    public float dist(float[] landmarks) {
        float dist = 99999f;
        float d;
        for (int i=0; i<landmarks.length; i=i+2) {
            d = (float)Math.sqrt((((float)Array.get(landmarks,i)-this.x)*((float)Array.get(landmarks,i) - this.x) + ((float)Array.get(landmarks,i+1) - this.y)* ((float)Array.get(landmarks,i+1) - this.y)));
            if (dist > d) {
                dist = d;
            }
        }
        return dist;
    }

    @Override
    public String toString() {
        return "Particle{" + "x=" + x + ", y=" + y + ", yew=" + yew + ", weight=" + weight + '}';
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getYew() {
        return yew;
    }

    public float getMoveNoise() {
        return moveNoise;
    }

    public float getTurnNoise() {
        return turnNoise;
    }

    public float getSenseNoise() {
        return senseNoise;
    }
    
    
}

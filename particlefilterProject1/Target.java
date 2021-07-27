package particlefilterProject1;

import java.math.*;
import java.lang.reflect.Array;
import java.util.Random;

/** Class for the Object being tracked
 *
 * @author Sean-Hol
 */
public class Target {
    private float x;
    private float y;
    private float yew;
    private float distance;

    private Random random = new Random();
    private float minWorldX;
    private float minWorldY;
    private float maxWorldX;
    private float maxWorldY;
    private Integer[][] Map;
    private float blocksize;

    public Target(float x, float y, float yew, float minWorldX, float minWorldY, float maxWorldX, float maxWorldY, Integer[][] Map, float blocksize) {
        this.x = x;
        this.y = y;
        this.yew = yew;
        this.minWorldX = minWorldX;
        this.minWorldY = minWorldY;
        this.maxWorldX = maxWorldX;
        this.maxWorldY = maxWorldY;
        this.Map = Map;
        this.blocksize = blocksize;
    }
    
    public float dist(float[] landmarks) {
        float d;
        float dist = 99999f;
        for (int i=0; i<landmarks.length/2; i++) {
            d = (float)Math.sqrt((((float)Array.get(landmarks,2*i)-this.x)*((float)Array.get(landmarks,2*i) - this.x) + ((float)Array.get(landmarks,2*i+1) - this.y)* ((float)Array.get(landmarks,2*i+1) - this.y)));
            if (dist > d) {
                dist = d;
            }
        }
        this.distance = dist;
        return dist;
    }
    
    public float[] step(float dist, float turn) {
        boolean free = false;
        float distUsed = dist;
        float turnUsed = turn;
        float yewNew = yew;
        float xNew = 0;
        float yNew = 0;
        while (!free) {
            yewNew = loopBack(yew + turnUsed, 0, 2);
            xNew = loopBack(x + (float)Math.cos(yewNew * (float)(Math.PI))*(distUsed), minWorldX, maxWorldX);
            yNew = loopBack(y +(float)Math.sin(yewNew * (float)(Math.PI))*(distUsed), minWorldY, maxWorldY);
            int xBlock = (int)Math.floor(xNew/blocksize);
            int yBlock = (int)Math.floor(yNew/blocksize);
                if (Map[yBlock][xBlock] == 2){
                    free = false;
                    distUsed = ((float)random.nextFloat()*100);
                    turnUsed = (float)random.nextFloat()*1;
                } else {
                    free = true;
                }
        }
        x = xNew;
        y = yNew;
        yew = yewNew;
        float stats[] = {distUsed,turnUsed};
        System.out.println(distUsed + " " + turnUsed);
        return stats;
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

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    
    
            
}

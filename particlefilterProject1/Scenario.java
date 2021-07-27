package particlefilterProject1;

import java.awt.Canvas;
import java.awt.Graphics;
import javax.swing.JFrame;
import java.awt.Color;
import java.util.Random;

/** Defines the scenario for the particle filter and map interaction
 *
 * @author Sean-Hol
 */
public class Scenario {
    
    private float minWorldX;
    private float minWorldY;
    private float maxWorldX;
    private float maxWorldY;
    private Target target;
    private Random random = new Random();
    private PFilter particleFilter;
    private Integer[][] StoreMap;
    private float moveNoise;
    private float turnNoise;
    private float senseNoise;
    private Integer landmarkNum;
    private float[] landmarks;
    private float blocksize;
    

    public Scenario() {
        this.minWorldX = 0;
        this.minWorldY = 0;
        this.maxWorldX = 1200;
        this.maxWorldY = 800;
        this.particleFilter = new PFilter(maxWorldX, maxWorldY, 8000);
        this.moveNoise = 5f;
        this.turnNoise = 0.05f;
        this.senseNoise = 5f;        
        particleFilter.declareNoise(moveNoise, turnNoise, senseNoise);
        Integer[][] Map = {{2,2,1,2,2,1,2,2,2,2,2,2},
                           {2,0,1,0,1,0,0,0,2,1,0,1},
                           {2,1,0,0,0,0,1,0,0,0,0,2},
                           {2,2,2,0,1,0,0,2,2,2,1,2},
                           {2,0,0,1,0,0,0,1,0,2,0,2},
                           {2,1,0,0,0,1,0,2,2,2,1,2},
                           {2,0,0,1,0,2,1,0,0,2,2,2},
                           {1,2,2,2,1,2,2,2,2,1,2,2}};
//        Integer[][] Map = {{0,0,1,0,0,1,0,0,0,0,0,0},
//                           {0,0,1,0,1,0,0,0,0,1,0,1},
//                           {0,0,0,0,0,0,0,0,0,0,0,0},
//                           {0,0,0,0,1,0,0,0,0,0,0,0},
//                           {0,0,0,1,0,0,0,1,0,0,0,0},
//                           {0,1,0,0,0,1,0,0,0,0,1,0},
//                           {0,0,0,0,0,0,0,0,0,0,0,0},
//                           {1,0,0,0,1,0,0,0,0,1,0,0}};
        this.landmarkNum = 20;
        this.landmarks = new float[landmarkNum*2];
        blocksize = 100;
        float markerLoc = blocksize/2;
        Integer markCounter = 0;
        for (int i=0;i<Map.length; i++){
            for (int j=0; j<Map[i].length; j++) {
                if (Map[i][j] == 1) {
                    landmarks[2*markCounter+1] = ((blocksize * (i)) + (blocksize*(float)random.nextFloat()));
                    landmarks[2*markCounter] = ((blocksize * (j)) + (blocksize*(float)random.nextFloat()));
                    markCounter++;
                }
            }
        }  
        this.StoreMap = Map;
        this.target = new Target(600f, 450f, 0f, minWorldX, minWorldY, maxWorldX, maxWorldY, Map, blocksize);

    }

    public Integer[][] getMap() {
        return StoreMap;
    }

    public float getBlocksize() {
        return blocksize;
    }


    public PFilter getParticleFilter() {
        return particleFilter;
    }

    public Target getTarget() {
        return target;
    }

    public float[] getLandmarks() {
        return landmarks;
    }
    
    public void step(float dist, float turn){
        
        float[] stats = getTarget().step(dist, turn);
        getParticleFilter().step(stats[0], stats[1]);
    }
    
    public void mazewalls() {
        for (int i=0; i<particleFilter.getParticleNum();i++){
            int xBlock = (int)Math.floor(particleFilter.getParticles()[i].getX()/blocksize);
            int yBlock = (int)Math.floor(particleFilter.getParticles()[i].getY()/blocksize);
            if (StoreMap[yBlock][xBlock] == 2){
                particleFilter.getParticles()[i].setWeight(0f);
            }

        }
    }
    
    
    
    
    
    
}

package particlefilterProject1;

import java.util.Random;

/** Methods for the Particle filter as a whole
 *
 * @author Sean-Hol
 */
public class PFilter {
    
    private Particle[] particles;
    private float worldX;
    private float worldY;
    private Random random = new Random();
    private int particleNum;

    public PFilter(float worldX, float worldY, int particleNum) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.particleNum = particleNum;
        particles = new Particle[particleNum];
        for (int i=0; i<particleNum; i++){
            particles[i] = new Particle((float)random.nextFloat()*worldX, (float)random.nextFloat()*worldY, (float)random.nextFloat()*0,0,0, worldX, worldY);
        }
    }
    
    public void declareNoise(float moveNoise, float turnNoise, float senseNoise) {
        for (int i=0; i<particleNum; i++){
            particles[i].setNoises(moveNoise, turnNoise, senseNoise);
        }
    }
    
    public void weigh(float targetDist, float[] landmarks) {
        for (int i=0; i<particles.length; i++) {
            particles[i].weightCalc(targetDist, landmarks);
        }
        
    }
    
    public void resample(){
        //resampling wheel method
        Particle[] newParts = new Particle[particleNum];
        float beta = 0f;
        //A random particle index is chosen
        Integer counter  = (int) (random.nextFloat()*particleNum);
        //beta is a random value between 0 and double the highest weight. 
        //The particle that aligns with beta after subtracting subsequent weights is selected to have a particle mapped to it
        for (int i=0; i<particleNum; i++){
            beta += random.nextFloat()*2f*bestPart().getWeight();
            while (beta > particles[counter].getWeight()){
                beta -= particles[counter].getWeight();
                counter = loopBack(counter+1, 0, particleNum-1);
            }
            newParts[i] = new Particle(particles[counter].getX(),particles[counter].getY(),particles[counter].getYew(),0,0,worldX,worldY); 
            newParts[i].setNoises(particles[counter].getMoveNoise(), particles[counter].getTurnNoise(), particles[counter].getSenseNoise());
            newParts[i].setWeight(particles[counter].getWeight());
        }
        particles = newParts;
    }
    
    public Particle bestPart(){
        Particle b = particles[0];
        for (int i=0; i<particleNum; i++){
            if (particles[i].getWeight() >= b.getWeight()) {
                b = particles[i];
            }
        }
        return b;
    }
    
    public void step(float dist, float turn){
        System.out.println(dist + " " + turn);
        for (int i=0; i<particleNum; i++){
            particles[i].step(dist, turn);
        }
    }
    
    
    
    public int loopBack(int counter, int lowerBoundary, int upperBoundary) {
        if (counter > upperBoundary) {
            counter -= upperBoundary;
        }
        else if (counter < lowerBoundary) {
            counter += upperBoundary;
        }
        return counter;
    }

    public int getParticleNum() {
        return particleNum;
    }

    public Particle[] getParticles() {
        return particles;
    }
    
    
}

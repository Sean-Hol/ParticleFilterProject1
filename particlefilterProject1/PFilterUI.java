package particlefilterProject1;

import java.awt.Canvas;
import java.awt.Graphics;
import javax.swing.JFrame;
import java.awt.Color;
import java.lang.reflect.Array;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/** The PFilterUI runs the scenario and creates the UI
 *
 * @author Sean-Hol
 */
public class PFilterUI extends JFrame{
    private Scenario s;
    private int counter;
    private Random random = new Random();
    private float xMean, yMean, xStd, yStd;
    public boolean succ = false;
    private Integer[][] Map;
    
    public PFilterUI(Scenario s, int counter) {
        this.s = s;
        this.counter = counter;
        this.Map = s.getMap();
        Canvas canvas = new Canvas(){
        public void paint(Graphics g){
            //Draws the map
            g.setColor(Color.white);
            for (int i=0;i<Map.length; i++){
                for (int j=0; j<Map[i].length; j++) {
                    if (s.getMap()[i][j] == 2) {
                        g.fillRect((int)(j*s.getBlocksize()), (int)(i*s.getBlocksize()), (int)s.getBlocksize(), (int)s.getBlocksize());
                    }
                }
            } 
            //Draws the object
            g.setColor(Color.red);
            g.fillOval((int)s.getTarget().getX()-15, (int)s.getTarget().getY()-15, 30, 30);
            //Draws the particles
            g.setColor(Color.white);
            for(int i=0; i< s.getParticleFilter().getParticleNum(); i++){
                g.fillOval((int)s.getParticleFilter().getParticles()[i].getX()-1, (int)s.getParticleFilter().getParticles()[i].getY()-1, 2, 2);           
            }
            //Draws landmarks
            for (int i=0; i<s.getLandmarks().length/2; i++) {
                g.setColor(Color.green);
                g.fillOval((int)s.getLandmarks()[2*i]-6, (int)s.getLandmarks()[2*i+1]-6, 12, 12);
            }
            if (counter == 20) {
                bestGuess();
                //Checks whether the Object is within the 95% confidence interval for the particles on the 20th step. If within the interval, a green oval showing the interval appears.
                int p = ((int)Math.pow((s.getTarget().getX() - xMean), 2) / (int)Math.pow(xStd, 2)) + ((int)Math.pow((s.getTarget().getY() - yMean), 2) / (int)Math.pow(yStd, 2));
                if (p <= 1) {
                    g.setColor(Color.green);
                } else {
                    g.setColor(Color.orange);
                }
                g.fillOval((int)(xMean - 1.96*xStd), (int)(yMean - 1.96*yStd), (int)(1.96*xStd)*2, (int)(1.96*yStd)*2);
            }
        }
        
        };
            JFrame frame = new JFrame("Filter Step " + counter);
            canvas.setSize(1200, 800);
            canvas.setBackground(Color.black);
            frame.add(canvas);
            frame.pack();
            frame.setVisible(true);
    }


    
        public static void main(String args[]) throws Exception{ 
                //Creates a scenario and uses the UI object to display it 20 times
                Scenario s = new Scenario();
                int counter = 0;
                PFilterUI f = new PFilterUI(s, counter);
                while (counter < 20){
                    Thread.sleep(1000);
                    f.runScenario();
                    counter++;
                    PFilterUI f1 = new PFilterUI(s, counter);
                }
        }
        
 
        
        
        public void runScenario() {
            //weight
            s.getParticleFilter().weigh(s.getTarget().dist(s.getLandmarks()), s.getLandmarks());
            s.mazewalls();
            //resample
            s.getParticleFilter().resample();
            //Move with noise
            s.step(((float)random.nextFloat()*100), (float)random.nextFloat()*2);
        }
    
        public void bestGuess() {
            //Finds the Mean and Standard deviation for the particles on the map.
            float xTotal =0;
            float yTotal =0;
            
            for (int i=0; i < s.getParticleFilter().getParticleNum(); i++){
                xTotal += s.getParticleFilter().getParticles()[i].getX();
                yTotal += s.getParticleFilter().getParticles()[i].getY();
            }
            float xMean = xTotal/s.getParticleFilter().getParticleNum();
            float yMean = yTotal/s.getParticleFilter().getParticleNum();
            float xVar =0;
            float yVar =0;
            for (int i=0; i < s.getParticleFilter().getParticleNum(); i++){
                xVar += (Math.pow(s.getParticleFilter().getParticles()[i].getX() - xMean, 2)/s.getParticleFilter().getParticleNum());
                yVar += (Math.pow(s.getParticleFilter().getParticles()[i].getY() - yMean, 2)/s.getParticleFilter().getParticleNum());
            }
            float xStd = (float) Math.sqrt(xVar);
            float yStd = (float) Math.sqrt(yVar);
            this.xMean = xMean;
            this.yMean = yMean;
            this.xStd = xStd;
            this.yStd = yStd;
            
        }
        
        
}

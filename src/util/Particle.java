package util;

public class Particle extends net.sourceforge.jswarm_pso.Particle{
    public static int particleNum;

    // This method must be a parameter-less method because the library function uses reflection
    public Particle() {
        super(particleNum);
    }
}

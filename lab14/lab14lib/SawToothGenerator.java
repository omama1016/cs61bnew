package lab14lib;

import edu.princeton.cs.algs4.StdAudio;

/**
 * ClassName: SawToothGenerator
 * Package: lab14lib
 * Description:
 *
 * @Author xyz
 * @Create 2023/11/28 14:49
 * @Version 1.0
 */
public class SawToothGenerator implements Generator{

    private int period;
    private int state;

    public SawToothGenerator(int period){
        this.period = period;
    }

    @Override
    public double next() {
        state = (state + 1) % period;
        return 2.0 / period * state - 1;
    }
}

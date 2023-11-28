package lab14;

import lab14lib.Generator;

/**
 * ClassName: AcceleratingSawToothGenerator
 * Package: lab14lib
 * Description:
 *
 * @Author xyz
 * @Create 2023/11/28 15:14
 * @Version 1.0
 */
public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private int state;
    private double factor;


    public AcceleratingSawToothGenerator(int period, double factor){
        this.period = period;
        this.factor = factor;
    }

    @Override
    public double next() {
        state = (state + 1) % period;
        if (state == 0) period *= factor;
        return 2.0 / period * state - 1;
    }
}

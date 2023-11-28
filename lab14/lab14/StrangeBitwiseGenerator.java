package lab14;

import lab14lib.Generator;

/**
 * ClassName: StrangeBitwiseGenerator
 * Package: lab14lib
 * Description:
 *
 * @Author xyz
 * @Create 2023/11/28 15:30
 * @Version 1.0
 */
public class StrangeBitwiseGenerator implements Generator {
    private int period;
    private int state;

    public StrangeBitwiseGenerator(int period){
        this.period = period;
    }

    @Override
    public double next() {
        state = state + 1;
//        int weirdState = state & (state >>> 3) % period;

        int weirdState = state & (state >> 3) & (state >> 8) % period;

        return 2.0 / period * weirdState - 1;
    }
}

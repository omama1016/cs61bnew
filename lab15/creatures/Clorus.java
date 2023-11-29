package creatures;

import huglife.*;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * ClassName: Clorus
 * Package: creatures
 * Description:
 *
 * @Author xyz
 * @Create 2023/11/29 9:10
 * @Version 1.0
 */
public class Clorus extends Creature {


    private double energy;
    private int r;
    private int g;
    private int b;
    public Clorus(double e){
        super("clorus");
        this.r = 0;
        this.g = 0;
        this.b = 0;
        this.energy = e;
    }

    public Clorus(){this(1);}
    @Override
    public void move() {
        energy -= 0.03;
    }

    @Override
    public void attack(Creature c) {
        energy += c.energy();
    }

    @Override
    public Creature replicate() {
        Clorus child = new Clorus(energy*0.5);
        energy = energy * 0.5;
        return child;
    }

    @Override
    public void stay() {
        energy -= 0.01;
    }

    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");
        if (empties.size() == 0) return new Action(Action.ActionType.STAY);
        else if (plips.size() >= 1) {
            Direction moveDir = HugLifeUtils.randomEntry(plips);
            return new Action(Action.ActionType.ATTACK, moveDir);
        }
        else if(energy >= 1) {
            Direction moveDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.REPLICATE, moveDir);
        }
        else {
            Direction moveDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.MOVE, moveDir);
        }

    }

    @Override
    public Color color() {
        return new Color(34, 0, 231);
    }
}

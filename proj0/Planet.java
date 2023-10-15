/**
 * ClassName: Planet
 * Package: PACKAGE_NAME
 * Description:
 *
 * @Author xyz
 * @Create 2023/10/15 9:21
 * @Version 1.0
 */
public class Planet {

    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;


    public static final double G = 6.67e-11;

    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        this.xxPos = xP;
        this.yyPos = yP;
        this.xxVel = xV;
        this.yyVel = yV;
        this.mass = m;
        this.imgFileName = img;
    }

    public Planet(Planet p) {
        this.xxPos = p.xxPos;
        this.yyPos = p.yyPos;
        this.xxVel = p.xxVel;
        this.yyVel = p.yyVel;
        this.mass = p.mass;
        this.imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p) {
        double dx = p.xxPos - this.xxPos;
        double dy = p.yyPos - this.yyPos;

        return Math.sqrt(dx * dx + dy * dy);
    }

    public double calcForceExertedBy(Planet p) {
        double r = calcDistance(p);
        return G * this.mass * p.mass / (r * r);
    }

    public double calcForceExertedByX(Planet p) {
        double r = calcDistance(p);
        double F = calcForceExertedBy(p);
        double dx = p.xxPos - this.xxPos;
        return F * dx / r;
    }

    public double calcForceExertedByY(Planet p) {
        double r = calcDistance(p);
        double F = calcForceExertedBy(p);
        double dy = p.yyPos - this.yyPos;
        return F * dy / r;
    }

    public double calcNetForceExertedByX(Planet[] ps) {
        double nfx = 0.0;
        for (Planet p : ps) {
            if (!this.equals(p)) {
                nfx += this.calcForceExertedByX(p);
            }
        }
        return nfx;
    }

    public double calcNetForceExertedByY(Planet[] ps) {
        double nfy = 0.0;
        for (Planet p : ps) {
            if (!this.equals(p)) {
                nfy += this.calcForceExertedByY(p);
            }
        }
        return nfy;
    }


    public void update(double dt, double fX, double fY) {
        double ax = fX / this.mass;
        double ay = fY / this.mass;
        this.xxVel += ax * dt;
        this.yyVel += ay * dt;
        this.xxPos += this.xxVel * dt;
        this.yyPos += this.yyVel * dt;
    }

    public void draw() {
        StdDraw.picture(this.xxPos, this.yyPos, "images/" + this.imgFileName);
    }
}

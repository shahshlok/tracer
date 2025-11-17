public class Cuboid {
    private double l;
    private double w;
    private double h;
    private String color;

    public Cuboid(double l, double w, double h, String color) {
        this.l = l;
        this.w = w;
        this.h = h;
        this.color = color;
    }

    public Cuboid() {
        this.l = 1;
        this.w = 1;
        this.h = 1;
        this.color = "white";
    }

    public double getL() {
        return l;
    }

    public double getW() {
        return w;
    }

    public double getH() {
        return h;
    }

    public String getColor() {
        return color;
    }

    public double getSurfaceArea() {
        double lw = l * w;
        double lh = l * h;
        double wh = w * h;
        return lw + lh + wh;
    }

    public double getVolume() {
        return l * w * h;
    }

    public void displayInfo() {
        System.out.println("Cuboid: color=" + color);
        System.out.println("l=" + l + " w=" + w + " h=" + h);
        System.out.println("surface area=" + getSurfaceArea());
        System.out.println("volume=" + getVolume());
    }

    public static void main(String[] args) {
        Cuboid a = new Cuboid();
        Cuboid b = new Cuboid(8, 3.5, 5.9, "green");

        a.displayInfo();
        b.displayInfo();
    }
}


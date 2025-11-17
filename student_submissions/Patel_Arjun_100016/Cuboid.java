public class Cuboid {
    double l;
    double w;
    double h;
    String color;

    public Cuboid(double l, double w, double h, String color) {
        this.l = l;
        this.w = w;
        this.h = h;
        this.color = color;
    }

    public Cuboid() {
        l = 1;
        w = 1;
        h = 1;
        color = "white";
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
        return 2 * (l * w + l * h + w * h);
    }

    public double getVolume() {
        return l * w * h;
    }

    public void displayInfo() {
        System.out.println("Color: " + color);
        System.out.println("l=" + l + ", w=" + w + ", h=" + h);
        System.out.println("Surface area: " + getSurfaceArea());
        System.out.println("Volume: " + getVolume());
    }

    public static void main(String[] args) {
        Cuboid c1 = new Cuboid();
        Cuboid c2 = new Cuboid(8, 3.5, 5.9, "green");

        c1.displayInfo();
        c2.displayInfo();
    }
}


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
        this(1.0, 1.0, 1.0, "white");
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
        System.out.println("Length: " + l);
        System.out.println("Width: " + w);
        System.out.println("Height: " + h);
        System.out.println("Surface Area: " + getSurfaceArea());
        System.out.println("Volume: " + getVolume());
    }

    public static void main(String[] args) {
        Cuboid cuboid1 = new Cuboid();
        Cuboid cuboid2 = new Cuboid(8, 3.5, 5.9, "green");

        System.out.println("Cuboid 1:");
        cuboid1.displayInfo();

        System.out.println("Cuboid 2:");
        cuboid2.displayInfo();
    }
}


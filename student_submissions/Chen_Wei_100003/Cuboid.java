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
        this(1, 1, 1, "white");
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
        return 2 * (lw + lh + wh);
    }

    public double getVolume() {
        return l * w * h;
    }

    public void displayInfo() {
        System.out.printf("Color: %s%n", color);
        System.out.printf("Length: %.2f%n", l);
        System.out.printf("Width: %.2f%n", w);
        System.out.printf("Height: %.2f%n", h);
        System.out.printf("Surface Area: %.2f%n", getSurfaceArea());
        System.out.printf("Volume: %.2f%n", getVolume());
    }

    public static void main(String[] args) {
        Cuboid first = new Cuboid();
        Cuboid second = new Cuboid(8.0, 3.5, 5.9, "green");

        System.out.println("First cuboid information");
        first.displayInfo();

        System.out.println("Second cuboid information");
        second.displayInfo();
    }
}


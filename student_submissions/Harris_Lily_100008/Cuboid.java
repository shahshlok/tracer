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
        return 2 * (l * w + l * h + w * h);
    }

    public double getVolume() {
        return l * w * h;
    }

    public void displayInfo() {
        System.out.println("Color: " + color);
        System.out.println("Dimensions: " + l + " x " + w + " x " + h);
        System.out.println("Surface Area: " + getSurfaceArea());
        System.out.println("Volume: " + getVolume());
    }

    public static void main(String[] args) {
        Cuboid def = new Cuboid();
        Cuboid green = new Cuboid(8.0, 3.5, 5.9, "green");

        System.out.println("Default:");
        def.displayInfo();

        System.out.println("Green:");
        green.displayInfo();
    }
}


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
        return l * w * h;
    }

    public double getVolume() {
        return 2 * (l * w + l * h + w * h);
    }

    public void displayInfo() {
        System.out.println("Cuboid color " + color);
        System.out.println("l=" + l + " w=" + w + " h=" + h);
        System.out.println("Surface area=" + getSurfaceArea());
        System.out.println("Volume=" + getVolume());
    }

    public static void main(String[] args) {
        Cuboid def = new Cuboid();
        Cuboid green = new Cuboid(8, 3.5, 5.9, "green");

        def.displayInfo();
        green.displayInfo();
    }
}


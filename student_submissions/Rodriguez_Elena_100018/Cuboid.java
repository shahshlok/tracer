public class Cuboid {
    private double l;
    private double w;
    private double h;
    private String color;

    public Cuboid(double length, double width, double height, String c) {
        this.l = length;
        this.w = width;
        this.h = height;
        this.color = c;
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
        System.out.println("Length: " + l + ", Width: " + w + ", Height: " + h);
        System.out.println("Surface Area: " + getSurfaceArea());
        System.out.println("Volume: " + getVolume());
    }

    public static void main(String[] args) {
        Cuboid first = new Cuboid();
        Cuboid second = new Cuboid(8, 3.5, 5.9, "green");

        first.displayInfo();
        second.displayInfo();
    }
}


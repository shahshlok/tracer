public class Cuboid {
    private double l;
    private double w;
    private double h;
    private String color;

    public Cuboid(double length, double width, double height, String color) {
        this.l = length;
        this.w = width;
        this.h = height;
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
        return 2 * (l * w + l * h + w * h);
    }

    public double getVolume() {
        return l * w * h;
    }

    public void displayInfo() {
        System.out.println("Color is " + color);
        System.out.println("Length " + l + " Width " + w + " Height " + h);
        System.out.println("Surface " + getSurfaceArea());
        System.out.println("Volume " + getVolume());
    }

    public static void main(String[] args) {
        Cuboid a = new Cuboid();
        Cuboid b = new Cuboid(8, 3.5, 5.9, "green");

        a.displayInfo();
        b.displayInfo();
    }
}


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
        return l * w + l * h + h * w;
    }

    public double getVolume() {
        return l * w * h;
    }

    public void displayInfo() {
        System.out.println("Cuboid");
        System.out.println("color=" + color);
        System.out.println("l=" + l + " w=" + w + " h=" + h);
        System.out.println("surface=" + getSurfaceArea());
        System.out.println("volume=" + getVolume());
    }

    public static void main(String[] args) {
        Cuboid a = new Cuboid();
        Cuboid b = new Cuboid(8, 3.5, 5.9, "green");

        a.displayInfo();
        b.displayInfo();
    }
}


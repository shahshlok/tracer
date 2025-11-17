Create a class named `Cuboid` to represent cuboid objects and contains:

- Three double attributes `l`, `w`, and `h` specifying the length, width, and height of the cuboid.
- A `String` attribute `color` that specifies the color of the cuboid.
- A constructor (with 4 arguments) that creates a cuboid with specified values.
- A constructor (with no arguments) that sets `l`, `w`, and `h` to 1 and `color` to `"white"`.
  This constructor should invoke the 4-argument constructor using `this`.

Your program should have these methods:

- Getter methods for all fields.
- `getSurfaceArea()`: returns the surface area of the cuboid, which is `2(lw + lh + wh)`.
- `getVolume()`: returns the cuboid volume, which is `l * w * h`.
- `displayInfo()`: displays the color, dimensions, surface area, and volume of this cuboid.

Write a test program that creates two objects of the `Cuboid` class — the first object should have default values and the second object must be green of length 8, width 3.5, and height 5.9. Print the dimensions, color, surface area, and volume of each object as shown in the sample run.

package sample;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import sample.Main.Theme;

import java.util.Arrays;
import java.util.List;

public class VehicleView {
    private Vehicle v;
    private enum colorName {
        RANDOM, RED, YELLOW, BLUE, GREEN, ORANGE, PURPLE, PINK, GREY, NAVY, BROWN, BLACK, WHITE, CYAN
    }
    private static final List<colorName> COLORS = Arrays.asList(colorName.values());

    public VehicleView(Vehicle v) {
        this.v = v;
    }
    
    public Rectangle getRec() {
        int length = 1 * Controller.UNIT_LENGTH;
        int width = v.getLength() * Controller.UNIT_LENGTH + 2*(v.getLength()-1);
        Rectangle rec = new Rectangle(length, width);
        if (v.getOrientation() == Vehicle.Orientation.HORIZONTAL) {
            rec.getTransforms().add(new Rotate(-90, 25, 25));
        }
        Image img = new Image("File:./src/Images/car.png");
        if (Main.currentTheme == Theme.CARS) {
       		img = new Image("File:./src/Images/car.png");
            if (v.getLength() == 3) {
                img = new Image("File:./src/Images/truck.png");
            }
        } else if (Main.currentTheme == Theme.PLANE) {
            img = new Image("File:./src/Images/Copter.png");
            if (v.getLength() == 3) {
                img = new Image("File:./src/Images/Plane.png");
            }
        } else if (Main.currentTheme == Theme.ANIMALS) {
            img = new Image("File:./src/Images/Animal.png");
            if (v.getLength() == 3) {
                img = new Image("File:./src/Images/Snake.png");
            }
        }
        rec.setEffect(this.getColorizer());
        rec.setFill(new ImagePattern(img));
        return rec;
        
//        Image img = new Image("File:./src/Images/car.png");
//        if (v.getLength() == 3) {
//            img = new Image("File:./src/Images/truck.png");
//        }
//        rec.setEffect(this.getColorizer());
//        rec.setFill(new ImagePattern(img));
//        return rec;
    }
    
    public ColorAdjust getColorizer() {
        int index = v.getCarId();
        ColorAdjust colorizer = new ColorAdjust();
        colorName color = (v.getCarId() < COLORS.size()) ? COLORS.get(index) : colorName.RANDOM;

        switch (color) {
            case RED:
                colorizer.setHue(0.0);
                colorizer.setSaturation(0.8);
                return colorizer;
            case ORANGE:
                colorizer.setHue(0.16667);
                colorizer.setSaturation(0.9);
                colorizer.setBrightness(0.1);
                return colorizer;
            case YELLOW:
                colorizer.setHue(0.33);
                colorizer.setSaturation(0.7);
                return colorizer;
            case GREEN:
                colorizer.setHue(0.66);
                colorizer.setSaturation(0.7);
                colorizer.setBrightness(-0.25);
                return colorizer;
            case BLUE:
                colorizer.setHue(-0.66);
                colorizer.setSaturation(0.8);
                return colorizer;
            case PINK:
                colorizer.setHue(-0.3);
                colorizer.setSaturation(1);
                return colorizer;
            case PURPLE:
                colorizer.setHue(-0.3);
                colorizer.setSaturation(1);
                colorizer.setBrightness(-0.5);
                return colorizer;
            case GREY:
                colorizer.setBrightness(-0.5);
                return colorizer;
            case NAVY:
                colorizer.setHue(-0.7);
                colorizer.setSaturation(0.7);
                colorizer.setBrightness(-0.5);
                return colorizer;
            case BROWN:
                colorizer.setHue(0.0);
                colorizer.setSaturation(0.6);
                colorizer.setBrightness(-0.4);
                return colorizer;
            case BLACK:
                colorizer.setBrightness(-0.8);
                colorizer.setSaturation(0.1);
                return colorizer;
            case WHITE:
                colorizer.setHue(0.2);
                colorizer.setSaturation(0.8);
                colorizer.setBrightness(0.8);
                return colorizer;
            case CYAN:
                colorizer.setHue(1);
                colorizer.setSaturation(1);
                return colorizer;
            case RANDOM:
            default:
                // Attempt to generate non-red colors
                double hue;
                while (Math.abs((hue = Math.random() * 2.0 - 1)) < 0.02) {
                }
                colorizer.setHue(hue);
                colorizer.setSaturation(Math.random());
                return colorizer;
        }
    }
}

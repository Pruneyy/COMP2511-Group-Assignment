package sample;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.util.Arrays;
import java.util.List;

public class VehicleView {
    private Vehicle v;
    private enum colorNames {
        RED, YELLOW, BLUE, GREEN, ORANGE, PURPLE, PINK, GREY, NAVY, BROWN, BLACK, WHITE, CYAN 
    }
    private static final List<colorNames> COLORS = Arrays.asList(colorNames.values());

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
        Image img = new Image("./sample/car.png");
        if (v.getLength() == 3) {
            img = new Image("./sample/truck.png");
        }
        rec.setEffect(this.getColorizer());
        rec.setFill(new ImagePattern(img));
        return rec;
    }

    public ColorAdjust getColorizer() {
        int color = v.getCarId() - 1;
        ColorAdjust colorizer = new ColorAdjust();
        switch (COLORS.get(color)) {
            case RED:
                colorizer.setHue(0.0);
                colorizer.setSaturation(0.8);
                //colorizer.setBrightness(-0.25);
                return colorizer;
            case ORANGE:
                colorizer.setHue(0.16667);
                colorizer.setSaturation(0.7);
                //colorizer.setBrightness(-0.25);
                return colorizer;
            case YELLOW:
                colorizer.setHue(0.33);
                colorizer.setSaturation(0.7);
                //colorizer.setBrightness(0.1);
                return colorizer;
            case GREEN:
                colorizer.setHue(0.66);
                colorizer.setSaturation(0.7);
                colorizer.setBrightness(-0.25);
                return colorizer;
            case BLUE:
                colorizer.setHue(-0.66);
                colorizer.setSaturation(0.8);
                //colorizer.setBrightness(-0.25);
                return colorizer;
            case PINK:
                colorizer.setHue(-0.3);
                colorizer.setSaturation(1);
                return colorizer;
            case PURPLE:
                colorizer.setHue(-0.55);
                colorizer.setSaturation(1);
                return colorizer;
            case GREY:
                colorizer.setBrightness(-0.5);
                return colorizer;
            case NAVY:
                colorizer.setHue(-0.7);
                colorizer.setSaturation(1);
                colorizer.setBrightness(-0.25);
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
            default:
                // Attempt to generate non-red colors
                double hue;
                while (Math.abs((hue = Math.random())) < 0.2) {
                }
                colorizer.setHue(hue);
                colorizer.setSaturation(Math.random());
                return colorizer;
        }
    }
}

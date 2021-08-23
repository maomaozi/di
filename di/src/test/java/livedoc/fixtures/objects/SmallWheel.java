package livedoc.fixtures.objects;

@WheelType(type = WheelType.Type.SMALL)
public class SmallWheel implements Wheel {
    private static final String name = "Small";

    @Override
    public String getType() {
        return name;
    }
}

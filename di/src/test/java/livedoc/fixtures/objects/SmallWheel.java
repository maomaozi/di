package livedoc.fixtures.objects;

import javax.inject.Named;

@Named("smallWheel")
@WheelType(type = WheelType.Type.SMALL)
public class SmallWheel implements Wheel {
    private static final String name = "Small";

    @Override
    public String getType() {
        return name;
    }
}

package livedoc.fixtures.objects;

import javax.inject.Named;

@Named("bigWheel")
@WheelType(type = WheelType.Type.BIG)
public class BigWheel implements Wheel {
    private static final String name = "Big";

    @Override
    public String getType() {
        return name;
    }
}

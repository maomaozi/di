package livedoc.fixtures.objects;

import javax.inject.Inject;

import static livedoc.fixtures.objects.WheelType.Type.BIG;

public class Bike {

    private final Wheel wheel;

    @Inject
    public Bike(@WheelType(type = BIG) Wheel wheel) {
        this.wheel = wheel;
    }

    public String getWheelType() {
        return wheel.getType();
    }
}

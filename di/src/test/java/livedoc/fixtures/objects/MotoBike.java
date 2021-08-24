package livedoc.fixtures.objects;

import javax.inject.Inject;
import javax.inject.Named;

public class MotoBike {

    private final Wheel wheel;

    @Inject
    public MotoBike(@Named("smallWheel") Wheel wheel) {
        this.wheel = wheel;
    }

    public String getWheelType() {
        return wheel.getType();
    }
}

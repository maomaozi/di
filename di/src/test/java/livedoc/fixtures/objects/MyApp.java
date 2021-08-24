package livedoc.fixtures.objects;

import com.mmaozi.di.annotations.Main;

import javax.inject.Inject;
import javax.inject.Singleton;

import static livedoc.fixtures.objects.WheelType.Type.SMALL;

@Singleton
public class MyApp {

    private final Wheel wheel;
    private String result;

    @Inject
    public MyApp(@WheelType(type = SMALL) Wheel wheel) {
        this.wheel = wheel;
    }

    public String getResult() {
        return result;
    }

    @Main
    public void main(String prefix) {
        result = String.format("%s %s", prefix, wheel.getType());
    }
}

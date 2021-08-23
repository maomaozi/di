package livedoc.fixtures.objects;

import javax.inject.Inject;

public class Car {

    private final Engine engine;

    @Inject
    public Car(@Gas Engine engine) {
        this.engine = engine;
    }

    public String getEngineName() {
        return engine.getName();
    }
}

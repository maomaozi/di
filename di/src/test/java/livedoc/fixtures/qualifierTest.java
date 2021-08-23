package livedoc.fixtures;

import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import livedoc.fixtures.objects.Car;

@FuShengTest
public class qualifierTest extends BaseFixture {
    private Car car;

    public void registerClasses(String classes) throws ClassNotFoundException {
        for (String className : classes.split(",")) {
            registerClass(className);
        }
    }

    public void getCarInstance() throws ClassNotFoundException {
        car = (Car) getInstance("Car");
    }

    public String getEngineNameInCar() {
        return car.getEngineName();
    }
}

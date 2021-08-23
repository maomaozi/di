package livedoc.fixtures;

import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import livedoc.fixtures.objects.Bike;
import livedoc.fixtures.objects.Car;

@FuShengTest
public class QualifierTest extends BaseFixture {
    private Car car;
    private Bike bike;

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

    public void getBikeInstance() throws ClassNotFoundException {
        bike = (Bike) getInstance("Bike");
    }

    public String getBikeWheelType() {
        return bike.getWheelType();
    }
}

package livedoc.fixtures;

import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import livedoc.fixtures.objects.Bike;
import livedoc.fixtures.objects.Car;
import livedoc.fixtures.objects.MotoBike;

@FuShengTest
public class QualifierTest extends BaseFixture {
    private Car car;
    private Bike bike;
    private MotoBike motoBike;

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

    public void getMotoBikeInstance() throws ClassNotFoundException {
        motoBike = (MotoBike) getInstance("MotoBike");
    }


    public String getBikeWheelType() {
        return bike.getWheelType();
    }

    public String getMotoBikeWheelType() {
        return motoBike.getWheelType();
    }
}

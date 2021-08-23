package livedoc.fixtures.objects;

@Gas
public class GasEngine implements Engine {
    private static final String name = "Gas";

    @Override
    public String getName() {
        return name;
    }
}

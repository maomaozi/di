package livedoc.fixtures.objects;

@Diesel
public class DieselEngine implements Engine {
    private static final String name = "Diesel";

    @Override
    public String getName() {
        return name;
    }
}

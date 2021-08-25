package livedoc.fixtures;

import com.thoughtworks.fusheng.integration.junit5.FuShengTest;

@FuShengTest
public class ContainerTest extends BaseFixture {

    public String tryGetUnRegisteredClass(String className) {
        try {
            getInstance(className);
        } catch (Exception ex) {
            return ex.getClass().getSimpleName();
        }

        return "no exception";
    }
}

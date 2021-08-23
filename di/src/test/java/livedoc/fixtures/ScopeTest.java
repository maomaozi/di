package livedoc.fixtures;

import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import livedoc.fixtures.objects.Buzz;

@FuShengTest
public class ScopeTest extends BaseFixture {

    private Buzz buzz1;
    private Buzz buzz2;

    public void getFirstBuzz() throws ClassNotFoundException {
        buzz1 = (Buzz) getInstance("Buzz");
    }

    public void getSecondBuzz() throws ClassNotFoundException {
        buzz2 = (Buzz) getInstance("Buzz");
    }

    public void assertBuzzEquals() {
        if (buzz1 == buzz2) {
            throw new RuntimeException("Buzz equals");
        }
    }

    public void assertFooEquals() {
        if (buzz1.getFoo() != buzz2.getFoo()) {
            throw new RuntimeException("Foo in buzz not equals");
        }
    }
}

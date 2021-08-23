package livedoc.fixtures.objects;


import javax.inject.Inject;
import javax.inject.Singleton;

public class Buzz {
    private final Foo foo;

    @Inject
    Buzz(@Singleton Foo foo) {
        this.foo = foo;
    }

    public Foo getFoo() {
        return foo;
    }
}

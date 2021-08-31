package livedoc.fixtures.objects;


import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Buzz {
    private final Foo foo;

    @Inject
    Buzz(Foo foo) {
        this.foo = foo;
    }
}

package livedoc.fixtures;


import javax.inject.Inject;


public class Bar {
    private Foo foo;

    @Inject
    Bar(Foo foo) {
        this.foo = foo;
    }

    public Foo getFoo() {
        return foo;
    }
}

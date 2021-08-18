package livedoc.fixtures.objects;

import javax.inject.Inject;

public class Circus {
    private Circus circus;

    @Inject
    Circus(Circus circus) {
        this.circus = circus;
    }
}

package livedoc.fixtures.objects;

import javax.inject.Inject;

public class Truck {

    private final Seat seat;

    @Inject
    public Truck(Seat seat) {
        this.seat = seat;
    }

    public String getSeatName() {
        return seat.getName();
    }
}

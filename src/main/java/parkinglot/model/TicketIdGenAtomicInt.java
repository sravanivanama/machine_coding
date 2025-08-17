package parkinglot.model;

import java.util.concurrent.atomic.AtomicInteger;

public class TicketIdGenAtomicInt {

    // Use of Atomic variables helps us in dealing with concurrency issues
    private AtomicInteger ticketId = new AtomicInteger();

    public Integer getTicketId(){
        return ticketId.incrementAndGet();
    }
}

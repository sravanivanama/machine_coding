package parkinglot.model;

public class TicketIdGenerator {
    static int id;

    public static synchronized int incrementId(){
        ++id;
        return id;

    }

}

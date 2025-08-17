package parkinglot.exception;

public class TicketAlreadyExists extends Exception{
    public TicketAlreadyExists(String message){
        super(message);
    }
}

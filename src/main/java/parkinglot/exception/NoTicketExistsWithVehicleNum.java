package parkinglot.exception;

public class NoTicketExistsWithVehicleNum extends Exception{
    public NoTicketExistsWithVehicleNum(String message){
        super(message);
    }
}

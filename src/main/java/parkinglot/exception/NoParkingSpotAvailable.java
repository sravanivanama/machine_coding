package parkinglot.exception;

public class NoParkingSpotAvailable extends Exception{
    public NoParkingSpotAvailable(String message){
        super(message);
    }
}

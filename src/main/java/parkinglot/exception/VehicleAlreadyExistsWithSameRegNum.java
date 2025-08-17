package parkinglot.exception;

public class VehicleAlreadyExistsWithSameRegNum extends Exception{
    public VehicleAlreadyExistsWithSameRegNum(String message){
        super(message);
    }
}

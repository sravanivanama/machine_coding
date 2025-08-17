package parkinglot.model;

import parkinglot.exception.InvalidVehicleType;

import java.util.List;

public class ParkingLot {
    /* A parking lot can have multiple floors

     */
    int numOfFloors;
    ParkingFloor[] parkingFloors;


    public ParkingLot(int floors, int totalSlots) throws InvalidVehicleType {
        this.numOfFloors = floors;
        parkingFloors = new ParkingFloor[floors];
        for(int i=0;i<floors;i++){
            parkingFloors[i] = new ParkingFloor(i+1, totalSlots);
        }
    }

    public ParkingFloor[] getParkingFloors(){
        return parkingFloors;
    }

}

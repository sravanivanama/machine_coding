package parkinglot.model;

import parkinglot.enums.VehicleType;

public class TruckParkingSpot extends ParkingSpot {

    public TruckParkingSpot(int spotId){
        super(spotId);
        this.vehicleType = VehicleType.TRUCK;
    }
}

package parkinglot.model;

import parkinglot.enums.VehicleType;

public class CarParkingSpot extends ParkingSpot {
    public CarParkingSpot(int spotId){
        super(spotId);
        this.vehicleType = VehicleType.CAR;
    }
}

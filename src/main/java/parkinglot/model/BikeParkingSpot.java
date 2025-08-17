package parkinglot.model;

import parkinglot.enums.VehicleType;

public class BikeParkingSpot extends ParkingSpot {
    public BikeParkingSpot(int spotId){
        super(spotId);
        this.vehicleType = VehicleType.BIKE;
    }
}

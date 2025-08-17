package parkinglot.model;

import parkinglot.enums.VehicleType;
import parkinglot.exception.InvalidVehicleType;

import java.util.Arrays;
import java.util.stream.Collectors;

public class VehicleFactory {

    public static void vehicleFactory(VehicleType type, ParkingSpot[] parkingSpots, int idx) throws InvalidVehicleType {
        switch (type) {
            case CAR:
                parkingSpots[idx] = new CarParkingSpot(idx);
                break;
            case BIKE:
                parkingSpots[idx] = new BikeParkingSpot(idx);
                break;
            case TRUCK:
                parkingSpots[idx] = new TruckParkingSpot(idx);
                break;
            default:
                throw new InvalidVehicleType("Received Invalid VehicleType: "+ type+". Expected: "+ Arrays.stream(VehicleType.values()).map(Enum::toString).collect(Collectors.joining(",")));

        }
    }
}

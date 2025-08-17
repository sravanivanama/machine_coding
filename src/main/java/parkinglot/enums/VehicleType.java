package parkinglot.enums;

import parkinglot.exception.InvalidVehicleType;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum VehicleType {
    CAR, BIKE, TRUCK;

    public static VehicleType getType(String vehicle) throws InvalidVehicleType {
        switch(vehicle.toUpperCase()) {
            case "CAR":
                    return CAR;
            case "BIKE": return BIKE;
            case "TRUCK": return TRUCK;
            default: throw new InvalidVehicleType("Received InvalidVehicleType: "+ vehicle+". Expected values: "+ Arrays.stream(VehicleType.values()).map(Enum::toString).collect(Collectors.joining(",")));
        }
    }
}

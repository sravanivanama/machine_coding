package parkinglot.pricing;

import parkinglot.enums.VehicleType;
import parkinglot.exception.InvalidVehicleType;
import parkinglot.model.ParkingTicket;

public interface PricingStrategy {
    double calculateFare(ParkingTicket parkingTicket) throws InvalidVehicleType;
}

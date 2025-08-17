package parkinglot.pricing;

import parkinglot.enums.VehicleType;
import parkinglot.exception.InvalidVehicleType;
import parkinglot.model.ParkingTicket;

import java.util.logging.Logger;

import static parkinglot.constants.NumericConstants.HOURS;
import static parkinglot.constants.PricingFare.*;

public class SlabPricingStrategy implements PricingStrategy{

    private static final Logger LOGGER = Logger.getLogger("SlabPricingStrategy");
    @Override
    public double calculateFare(ParkingTicket parkingTicket) throws InvalidVehicleType {
        double hourlyFare = getBasicFareByVehicleType(parkingTicket.getVehicle().getVehicleType());
        long endTime = System.currentTimeMillis();
        double durationInHrs = Math.ceil((double)(endTime - parkingTicket.getStartTime())/HOURS);
        double fare = hourlyFare + (durationInHrs - 1) * INCREMENT_FEE;
        LOGGER.info("Total fare for Parking Ticket: "+parkingTicket+" is: "+fare);
        return fare;
    }

    private double getBasicFareByVehicleType(VehicleType type) throws InvalidVehicleType {
        switch (type){
            case CAR:  return CAR_FEE;
            case BIKE: return BIKE_FEE;
            case TRUCK: return TRUCK_FEE;
            default:
                throw new InvalidVehicleType("Invalid VehicleType received to calculate fare: "+type);
        }
    }
}

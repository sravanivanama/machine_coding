package parkinglot.pricing;

import parkinglot.constants.PricingFare;
import parkinglot.enums.VehicleType;
import parkinglot.model.ParkingTicket;

import java.util.logging.Logger;

import static parkinglot.constants.NumericConstants.HOURS;
import static parkinglot.constants.PricingFare.INCREMENT_FEE;

public class BasicFarePricingStrategy implements PricingStrategy{

    private static final Logger LOGGER = Logger.getLogger("BasicFarePricingStrategy");
    @Override
    public double calculateFare(ParkingTicket parkingTicket){
        long endTime = System.currentTimeMillis();
        double durationInHrs = Math.ceil((double)(endTime - parkingTicket.getStartTime())/HOURS);
        double fare = PricingFare.BASIC_FARE * durationInHrs;
        LOGGER.info("Total fare for Parking Ticket: "+parkingTicket+" is: "+fare);
        return fare;
    }
}

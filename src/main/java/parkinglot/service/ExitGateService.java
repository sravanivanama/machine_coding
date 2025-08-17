package parkinglot.service;

import parkinglot.dao.ParkingTicketDao;
import parkinglot.enums.VehicleType;
import parkinglot.exception.InvalidTicket;
import parkinglot.exception.InvalidVehicleType;
import parkinglot.model.ParkingSpot;
import parkinglot.model.ParkingTicket;
import parkinglot.pricing.BasicFarePricingStrategy;
import parkinglot.pricing.PricingStrategy;

import java.util.logging.Logger;

import static parkinglot.constants.NumericConstants.HOURS;
import static parkinglot.constants.PricingFare.*;

public class ExitGateService {
    ParkingTicketDao parkingTicketDao;
    PricingStrategy  strategy;
    public ExitGateService(ParkingTicketDao parkingTicketDao, PricingStrategy strategy){
        this.parkingTicketDao = parkingTicketDao;
        this.strategy = strategy;
    }

    private static final Logger LOGGER = Logger.getLogger("ExitGateService");
    public double unparkVehicle(int ticketId) throws InvalidVehicleType, InvalidTicket {
        ParkingTicket ticket = parkingTicketDao.findTicketById(ticketId);
        LOGGER.info("Start of unparking vehicle for ticket: "+ticket);
        ParkingSpot spot = ticket.getSpot();
        spot.occupySpot();
        ticket.invalidateTicket();
        return strategy.calculateFare(ticket);

    }


}

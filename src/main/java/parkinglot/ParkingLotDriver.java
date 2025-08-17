package parkinglot;

import parkinglot.dao.ParkingTicketDao;
import parkinglot.enums.VehicleType;
import parkinglot.exception.*;
import parkinglot.model.ParkingLot;
import parkinglot.model.ParkingTicket;
import parkinglot.pricing.BasicFarePricingStrategy;
import parkinglot.pricing.PricingStrategy;
import parkinglot.service.ExitGateService;
import parkinglot.service.ParkingLotService;

public class ParkingLotDriver {
    public static void main(String[] args) throws InvalidVehicleType, NoParkingSpotAvailable, VehicleAlreadyExistsWithSameRegNum, TicketAlreadyExists, InvalidTicket {
        ParkingLot parkingLot = new ParkingLot(2, 6);
        ParkingTicketDao parkingTicketDao = new ParkingTicketDao();
        ParkingLotService parkingLotService = new ParkingLotService(parkingLot, parkingTicketDao);
        parkingLotService.getAvailableParkingSpots(VehicleType.CAR);
        parkingLotService.getAvailableParkingSpots(VehicleType.BIKE);
        ParkingTicket ticket = parkingLotService.issueTicket("KA-01-HH-1234", "CAR");
        ParkingTicket ticket1 = parkingLotService.issueTicket("KA-01-HH-9999","BIKE");
        parkingLotService.getAvailableParkingSpots(VehicleType.CAR);
        PricingStrategy strategy = new BasicFarePricingStrategy();
        ExitGateService exitGateService = new ExitGateService(parkingTicketDao, strategy);
        exitGateService.unparkVehicle(ticket.getTicketId());
        exitGateService.unparkVehicle(ticket1.getTicketId());
        parkingLotService.getAvailableParkingSpots(VehicleType.CAR);
        parkingLotService.getAvailableParkingSpots(VehicleType.BIKE);

    }
}

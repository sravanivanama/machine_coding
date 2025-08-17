package parkinglot.service;

import parkinglot.dao.ParkingTicketDao;
import parkinglot.enums.VehicleType;
import parkinglot.exception.InvalidVehicleType;
import parkinglot.exception.NoParkingSpotAvailable;
import parkinglot.exception.TicketAlreadyExists;
import parkinglot.exception.VehicleAlreadyExistsWithSameRegNum;
import parkinglot.model.*;

import java.util.*;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ParkingLotService {
    private static final Logger LOGGER = Logger.getLogger("ParkingLotService");
    /*
    Intilaize parkinglot with x no. of floors and each floor having y car, z bike, a trucks
     */
    ParkingLot parkingLot;
    ParkingTicketDao parkingTicketDao;

    public ParkingLotService(ParkingLot parkingLot, ParkingTicketDao parkingTicketDao){
        this.parkingLot = parkingLot;
        this.parkingTicketDao = parkingTicketDao;
    }

    public Map<ParkingFloor, List<ParkingSpot>> getAvailableParkingSpots(VehicleType vehicleType) {
        ParkingFloor[] parkingFloors = parkingLot.getParkingFloors();
        Map<ParkingFloor, List<ParkingSpot>> spots = Arrays.stream(parkingFloors)
                .collect(Collectors.toMap(
                        floor -> floor, floor -> floor.getAvailableParkingSpots(vehicleType)));
        Map<ParkingFloor, List<ParkingSpot>> availableSpots = spots.entrySet().stream().filter(floor -> floor.getValue().size() > 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        int totalCount = availableSpots.values().stream().mapToInt(List::size).sum();
        LOGGER.info("No. of parking spots available for type: "+ vehicleType.toString()+" is: "+totalCount);
        return availableSpots;
    }

    public ParkingSpot blockSpot(Map<ParkingFloor, List<ParkingSpot>> availableSpots, VehicleType type) throws NoParkingSpotAvailable {
        // Get Nearest spot to the entrygate which is situated near to floor 0

        Optional<ParkingFloor> parkingFloorOptional = availableSpots.keySet().stream().min(Comparator.comparingInt(ParkingFloor::getFloorId));
        if(parkingFloorOptional.isPresent()){
            ParkingFloor floor = parkingFloorOptional.get();
            List<ParkingSpot> spotsOnFloor = availableSpots.get(floor);
            if(spotsOnFloor != null && !spotsOnFloor.isEmpty()){
                Optional<ParkingSpot> spotOptional = spotsOnFloor.stream().min(Comparator.comparingInt(ParkingSpot::getSpotId));
                if(spotOptional.isPresent()){
                    ParkingSpot spot = spotOptional.get();
                    spot.occupySpot();
                    return spot;
                } else {
                    throw new NoParkingSpotAvailable("No Parking spots available for vehicle: "+type.toString());
                }
            } else {
                throw new NoParkingSpotAvailable("No Parking spots available for vehicle: "+type.toString());
            }
        } else {
            throw new NoParkingSpotAvailable("No Parking spots available for vehicle: "+type.toString());
        }

    }

    public ParkingTicket issueTicket(String vehicleNumber, String vhcle) throws InvalidVehicleType, NoParkingSpotAvailable, VehicleAlreadyExistsWithSameRegNum, TicketAlreadyExists {
        VehicleType type = VehicleType.getType(vhcle);
        Vehicle vehicle = new Vehicle(vehicleNumber, type);
        Map<ParkingFloor, List<ParkingSpot>> availableParkingSpots = getAvailableParkingSpots(type);
        ParkingSpot spot = blockSpot(availableParkingSpots, type);
        ParkingTicket parkingTicket = new ParkingTicket(vehicle, System.currentTimeMillis(), spot);
        parkingTicketDao.addParkingTicket(parkingTicket);
        LOGGER.info("Parked vehicle: "+vehicleNumber+" of type: "+vhcle+ ". Parking Ticket Details: "+parkingTicket.toString());
        return parkingTicket;
    }

}

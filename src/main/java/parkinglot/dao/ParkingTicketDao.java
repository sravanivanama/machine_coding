package parkinglot.dao;

import java.util.*;
import java.util.stream.Collectors;

import parkinglot.exception.InvalidTicket;
import parkinglot.exception.NoTicketExistsWithVehicleNum;
import parkinglot.exception.TicketAlreadyExists;
import parkinglot.exception.VehicleAlreadyExistsWithSameRegNum;
import parkinglot.model.ParkingTicket;
import parkinglot.model.Vehicle;

public class ParkingTicketDao {
    public static Map<Integer, ParkingTicket> parkingTicketMap = new HashMap<>();

    public boolean addParkingTicket(ParkingTicket parkingTicket) throws TicketAlreadyExists, VehicleAlreadyExistsWithSameRegNum {
        Map<Integer, ParkingTicket> validTicketMap = getValidParkingTickets().stream()
                .collect(Collectors.toMap(ParkingTicket::getTicketId, ticket-> ticket));
        if(validTicketMap.containsKey(parkingTicket.getTicketId())){
            throw new TicketAlreadyExists("Parking Ticket already exists with ticket id: "+parkingTicket.getTicketId());
        }
        if(isVehicleAlreadyParked(parkingTicket.getVehicle(), new ArrayList<>(validTicketMap.values()))){
            throw new VehicleAlreadyExistsWithSameRegNum("Vehicle Already exisst with same reg. number: "+parkingTicket.getVehicle().getVehicleNumber());
        }
        parkingTicketMap.put(parkingTicket.getTicketId(), parkingTicket);
        return true;
    }

    public ParkingTicket findTicketById(int ticketId) throws InvalidTicket {
        if(!parkingTicketMap.containsKey(ticketId) || !parkingTicketMap.get(ticketId).isValidTicket()){
            throw new InvalidTicket("Invalid ticket received during unparking a vehicle: "+ticketId);
        }

        return parkingTicketMap.get(ticketId);
    }

    public ParkingTicket findTicketByVehicleNum(String vehicleNumber) throws NoTicketExistsWithVehicleNum {
        Optional<ParkingTicket> parkingTicketOptional = parkingTicketMap.values().stream()
                .filter(ticket -> ticket.getVehicle().getVehicleNumber().equalsIgnoreCase(vehicleNumber)).findFirst();
        if(parkingTicketOptional.isPresent()){
            return parkingTicketOptional.get();
        } else {
            throw new NoTicketExistsWithVehicleNum("No ParkingTicket exists with vehicle number: "+vehicleNumber);
        }
    }

    public List<ParkingTicket> getValidParkingTickets(){
        return parkingTicketMap.values().stream().filter(ParkingTicket::isValidTicket).collect(Collectors.toList());
    }

    public boolean isVehicleAlreadyParked(Vehicle vehicle, List<ParkingTicket> tickets) {
        return tickets.stream().anyMatch(ticket -> ticket.getVehicle().getVehicleNumber().equals(vehicle.getVehicleNumber()));
    }
}

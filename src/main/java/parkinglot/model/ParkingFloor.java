package parkinglot.model;

import parkinglot.constants.Constants;
import parkinglot.constants.NumericConstants;
import parkinglot.enums.VehicleType;
import parkinglot.exception.InvalidVehicleType;

import java.util.*;
import java.util.stream.Collectors;

import static parkinglot.constants.NumericConstants.*;

public class ParkingFloor {
    /*
     A Parking Floor has multiple Parking Slots
     */
    int floorId;
    int spots;

    ParkingSpot[] parkingSpots;

    public ParkingFloor(int floorId, int slots) throws InvalidVehicleType {
        this.floorId = floorId;
        this.spots = slots;
        this.parkingSpots = new ParkingSpot[spots];
        divideSlots(spots);
        initSlots();
    }
    public int getFloorId(){
        return floorId;
    }
    public ParkingSpot[] getParkingSpots(){
        return parkingSpots;
    }

    private void divideSlots(int slots){
        VehicleType[] vehicleTypes = Constants.vehicleTypes;
        int equalSlot = slots/vehicleTypes.length;
        for(VehicleType type: vehicleTypes) {
            Constants.slotsPerVehicle.put(type, equalSlot);
        }
        if(slots% vehicleTypes.length != 0){
            for(int i=0;i<slots% vehicleTypes.length;i++){
                Constants.slotsPerVehicle.put(vehicleTypes[i],
                        Constants.slotsPerVehicle.getOrDefault(vehicleTypes[i], 0)+1);
            }
        }
    }
    public void initSlots() throws InvalidVehicleType {
        int curr = 0;
        for(Map.Entry<VehicleType, Integer> m: Constants.slotsPerVehicle.entrySet()){
            VehicleType type = m.getKey();
            int slots = m.getValue();
            for(int i=curr;i<slots+curr;i++){
                VehicleFactory.vehicleFactory(type, parkingSpots, i);
            }
            curr += slots;
        }
    }

    public List<ParkingSpot> getAvailableParkingSpots(VehicleType vehicleType) {
        if(parkingSpots != null) {
            return Arrays.stream(parkingSpots).filter(spot -> spot.isEmpty() && spot.vehicleType.equals(vehicleType)).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public String toString(){
        return "Parking floor: id: "+floorId;
    }

}

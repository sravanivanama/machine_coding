package parkinglot.model;

import parkinglot.enums.VehicleType;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class ParkingSpot {
    /*
     We have parking spots for different types of vehicles -> It currently support Car, Bike, Truck
     */
    private final Lock lock = new ReentrantLock();
    VehicleType vehicleType;
    int spotId;
    boolean isEmpty;

    public  ParkingSpot(int spotId){
        this.spotId = spotId;
        isEmpty = true;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public boolean isEmpty(){
        return isEmpty;
    }

    public synchronized void occupySpot(){
        isEmpty = false;
    }

    public synchronized void occupySpots(){
        try {
            lock.lock();
            isEmpty = false;
        } finally {
            lock.unlock();
        }

    }

    public void freeSpot(){
        isEmpty = true;
    }


    public int getSpotId(){
        return spotId;
    }

    @Override
    public String toString(){
        return "Parking Spot: spotId: "+ spotId+", vehicleType: "+vehicleType.toString();
    }
}



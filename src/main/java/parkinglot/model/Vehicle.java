package parkinglot.model;

import parkinglot.enums.VehicleType;

public class Vehicle {
    String vehicleNumber;
    VehicleType vehicleType;

    public Vehicle(String vehicleNumber, VehicleType vehicleType){
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
    }

    public void setVehicleNumber(String vehicleNumber){
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleNumber(){
        return vehicleNumber;
    }

    public void setVehicleType(VehicleType vehicleType){
        this.vehicleType = vehicleType;
    }
    public VehicleType getVehicleType(){
        return vehicleType;
    }
    @Override
    public String toString() {
        return "Vehicle: no.:" + vehicleNumber + ", type: " + vehicleType.toString();
    }
}

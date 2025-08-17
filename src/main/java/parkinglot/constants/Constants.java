package parkinglot.constants;

import parkinglot.enums.VehicleType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static parkinglot.enums.VehicleType.*;

public class Constants {
    public static Map<VehicleType, Integer> slotsPerVehicle = new HashMap<>();

    public static final VehicleType[] vehicleTypes = new VehicleType[]{CAR, BIKE, TRUCK};

}

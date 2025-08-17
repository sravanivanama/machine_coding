package parkinglot.model;

public class ParkingTicket {

    int ticketId;
    Vehicle vehicle;
    long startTime;

    boolean isValid;

    ParkingSpot spot;

    public ParkingTicket(Vehicle vehicle, long startTime, ParkingSpot spot){
        this.spot = spot;
        this.vehicle = vehicle;
        this.startTime = startTime;
        this.ticketId = TicketIdGenerator.incrementId();
        this.isValid = true;
    }

    public ParkingSpot getSpot(){
        return spot;
    }

    public Vehicle getVehicle(){
        return vehicle;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void invalidateTicket(){
        isValid = false;
    }

    public boolean isValidTicket(){
        return isValid;
    }

    public int getTicketId() {
        return ticketId;
    }

    @Override
    public String toString(){
        return "Ticket: "+ vehicle.toString()+ ", "+spot.toString()+"Time: "+startTime;
    }
}

# FeedBack

1. I missed adding DAO/Repository classes i.e. maintaining active central repository to get things back
2. Missed clarifying requirements:

    a. Assumed that slots per floor would be fixed, GET THIS CLARIFIED FROM INTERVIEWER
    
    b. Also ask for number of CAR, BIKE, TRUCK SLOTS -> if no. of slots are dynamic, how to distribte vehicles per type
        
        i. Static  -> Equal distibution
        ii. Custom -> Taking from input itself that car:6, bike: 8, truck:6
        iii. Dynamic -> Slots would be generic, based on size we can allocate...lte's say if bike sare full and car is empty, then bike can take car space

3. Also all the models where needed did not have ids. -> Like ParkingTicket did not have ticketId
4. Missed edge cases like, 
   
        i. Duplicate vehicle registration number in parking lot
        ii. InvalidTicket given while unparking
5. Pricing Strategy -> This is just hardcoded in code, needs to be pluggable and should have some strategy.

         i. Basic fare for all the vehicles -> like 20rs/hr
        ii. (SLAB - BASED)Fare based on vehicle type and duration -> Take it as input or either store it in DB

6. ParkingSpot -> to have cleaner APIs like freeSpot(), occupySpot() instead of setAvailable etc...
7. toString overrides missing in few classes and also instead of logs -> these rounds expect System.out
8. blockSpot -> always assumes there is a spot ->potential NPE
9. concurrency issues

# Followup Questions

1. Allocation policy: What rule do you follow when several floors/slots are available? (expected: deterministic, e.g., lowest floor then lowest slot)

         1. Ans: I'm preferring lowest available floor and then the lowest available slot in it.

2. Ticketing: How would you support unpark by id string if the process restarts? (expected: persistence or id generation + repository, maybe simple file store)

         1. We could go for storing it in DB/files

3. Pricing: If pricing changes (weekend surge, slabs like 0–2 hrs fixed, then per hour), how would you plug it in without touching business logic?

         1. Maybe I would simply add 1 more strategy pattern, it is extensibility without modification

4. Concurrency: Two entry gates concurrently parking—how do you avoid double-booking the same slot? (expected: lock per slot or CAS on availability; possibly a ReentrantLock on ParkingSpot)
   1. To prevent concurrent access of parking/booking of same spot/slot,
       I will use synchronization over issueTicket method, specifically over occupySpot method since this is where two threads come here and after checks of validTicket...
   2. Another option is to use locks like ReentrantLock on occupySpots

5. Extensibility: Add EV charging spots (different fee + availability)—where do you change code?
   1. I'll add EV ChargingSpot as interface and let all the classes implement it
   2. 
6. If tomorrow we add EV charging slots with different pricing, what would you change?
   1. Same as 5

7. How would you make pricing rules dynamic based on day of week or time of day?
   1. Same as 3

8. If two entry gates are operating in parallel, how do you prevent double booking the same spot?
   1. Same as 4

9. If the system restarts, how do you preserve parked vehicle state and tickets? 
   1. Same as 2

10. How would you handle thousands of floors without making slot search O(n) each time?

          1. I create a Priorityqueue(Lowest floor, lowest slot per floor) and add all the available spots each time, now when I get a request for park, I simply remove the forst element from queue.
          2. Adding an element will take time as it reorders elements by Priority Queue ---
             Second Thoughts -> What if we have millions of slots per each floor -> IN that case, you'll simply have OOM---> Think more on how would you handle such situation
    1. This is good if we have 100's of floors and 1000's of slots, but for 10M this would not be optimized.
    2. 
       1. Bucketed Queues (per floor or per vehicle type)

                1. Keep a TreeSet or PriorityQueue per floor, instead of one global queue.
                      When parking, pick the lowest non-empty floor queue.
                      Complexity reduces to O(log M), where M = slots per floor (≪ total slots).
       2. Index by vehicle type
       
                1. Maintain separate PQs per VehicleType.
                    Faster lookups when a bike comes in (no need to scan car/truck slots). 
       3. Bitset/Segment Tree (for very large scale)
         
                1. Each floor represented as a bitset (0 = free, 1 = occupied).
                   Allocation = find first zero bit (O(1) with CPU intrinsics like Integer.numberOfTrailingZeros).
                   Much faster than PQ for million-scale.
       4. Hybrid approach
         
                1. Use priority queue only at floor level, but within floor use a bitset.
                   Find floor in O(log F), find slot in O(1).
    
11. Vehicle type with zero slots: What happens if a vehicle type is configured with no slots?

          1. Question for interviewer: Was it configured initially as 0 or aren't there any available slots?
          2. If no available slots: It throws exception - NoParkingSpotAvailable 
                  else initially configured as 0, it would still behave the same

12. Slot distribution: Right now you divide slots equally. How would you handle unequal slot distribution (say, 70% bikes, 20% cars, 10% trucks)? 

          1. I would take that as input from cmdLine, then pass it to the constructor of ParkingLot, in this code, I would set in Map with these values 

13. Unparking without ticket: What if the customer loses the ticket but remembers the vehicle number?
    
          1. We could search all the tickets by vehicleNumber, I'll write a method in DAO to getTicketByVehicleNum -- findTicketByVehicleNum 

14. Multiple entrances/exits: How would you prevent two entrances from assigning the same slot at the same time?
    
          1. Will use synchronization/locks to prevent booking same spot at once 

15. Large parking lots: With thousands of floors and millions of slots, will your slot allocation still perform well? If not, how would you optimize?

16. Multi-slot vehicles: How would you handle a vehicle type (like a bus/truck) that needs multiple consecutive slots?

          1. Our design assigns slots such that CAR takes 4 spaces, BIKE takes 2, TRUCK takes 8 -> we specifically do not modify...
             Polished version -> Currently each slot is configured for a type(like car slot taking 4 spaces, bike taking 1 etc...), But in case it is unit-size slot, 
            I can redesign and assign consecutive slots for required vehicle

17. Concurrency: If two threads try to park/unpark the same vehicle at once, what happens? How would you ensure correctness?

          1. aways we make sure there is synchronization/locks and the variable isEmpty is made volatile so that every thread always has latest data(consistent)
            wrong understanding of ques -> what if 2 threads try to park same vehicle....duplicate parking -> Create a concurrent map (check if vehicle exists, otheriwse add it)

18. Reporting: How would you implement a feature to get current availability (per vehicle type, per floor)?

          1. I already have support for VehicleType -> getAvailableParkingSpots which returns per type, per floor

19. Extensibility: Suppose tomorrow we need “EV Charging” slots or “Reserved Slots for VIPs.” How would your design adapt?

20. Persistence: Right now everything is in memory. How would you add DB/file persistence without rewriting business logic?

          1. I will simply add JDBC Connector logic and instead of updating and reading from Map, I will write DB query and replace it there
          2. If it's file, same thing, write and read from file instead of map

21. Search: If I want to query “Where is my car?” by registration number, how would you implement it?

          1. Check 13

22. Clean-up: How do you prevent stale tickets (vehicle already exited) from staying in memory forever?
       
          1. I would have a CRON JOB which runs daily at the silent hours(via API to which triggers) to clean the tickets which are invalidated from in memory


# Figure out
    1. Extensibility -> Something like adding EV Charging Spots -> I just have idea of sing Factory pattern and create interface and implement it,
        but not quite sure on how  to implement pricing specifically for this. 
    2. Large scale search -> how to search for a spot in < O(n) time as you might have tens/thousand of floors and millions of slots per floor
    3. Multi slot vehicles -> So is it something like a SLOT can implicitly take only bike and for CAR or TRUCK we should have 2 slots ??
    4. Concurrency: park/unpark same vehicle at once, How to handle correctness -> always we make sure there is synchronization/locks  
        and the variable isEmpty is made volatile so that every thread always has latest data(consistent)

# code for large scale -> Slot allocation

```java
import java.util.*;

class Floor {
    private long[] slots;  // bitset representation
    private int totalSlots;

    public Floor(int totalSlots) {
        this.totalSlots = totalSlots;
        int words = (totalSlots + 63) / 64;  // each long = 64 slots
        slots = new long[words];  // initialized to 0 = all free
    }

    // Allocate first free slot
    public int allocate() {
        for (int wordIndex = 0; wordIndex < slots.length; wordIndex++) {
            long word = slots[wordIndex];
            if (~word != 0) { // means not all 1s (some slot is free)
                int bit = Long.numberOfTrailingZeros(~word); // first 0
                int slotId = wordIndex * 64 + bit;
                if (slotId < totalSlots) {
                    slots[wordIndex] |= (1L << bit); // mark as occupied
                    return slotId;
                }
            }
        }
        throw new RuntimeException("No free slots available");
    }

    // Free a slot
    public void free(int slotId) {
        int wordIndex = slotId / 64;
        int bit = slotId % 64;
        slots[wordIndex] &= ~(1L << bit); // mark as free
    }

    public boolean isFull() {
        for (long word : slots) {
            if (~word != 0) return false;
        }
        return true;
    }
}

public class ParkingBitsetDemo {
    public static void main(String[] args) {
        Floor floor = new Floor(130); // 130 slots

        int s1 = floor.allocate(); // should give slot 0
        int s2 = floor.allocate(); // slot 1
        int s3 = floor.allocate(); // slot 2
        System.out.println("Allocated slots: " + s1 + ", " + s2 + ", " + s3);

        floor.free(1); // free slot 1
        int s4 = floor.allocate(); // should re-allocate slot 1
        System.out.println("Re-allocated slot: " + s4);
    }
}

```
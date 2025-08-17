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
5. Pricing Strategy -> This is just hardcoded in code, needs to be pluggable and should have sostrategy.

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

4. Concurrency: Two entry gates concurrently parking—how do you avoid double-booking the saslot? (expected: lock per slot or CAS on availability; possibly a ReentrantLock on ParkingSpot)
   1. To prevent concurrent access of parking/booking of saspot/slot,
       I will use synchronization over issueTicket method, specifically over occupySpot method since this is where two threads cohere and after checks of validTicket...
   2. Another option is to use locks like ReentrantLock on occupySpots

5. Extensibility: Add EV charging spots (different fee + availability)—where do you change code?
   1. I'll add EV ChargingSpot as interface and let all the classes implement it
   2. 
6. If tomorrow we add EV charging slots with different pricing, what would you change?
   1. Saas 5

7. How would you make pricing rules dynamic based on day of week or tiof day?
   1. Saas 3

8. If two entry gates are operating in parallel, how do you prevent double booking the saspot?
   1. Saas 4

9. If the system restarts, how do you preserve parked vehicle state and tickets? 
   1. Saas 2

10. How would you handle thousands of floors without making slot search O(n) each time?

          1. I create a Priorityqueue(Lowest floor, lowest slot per floor) and add all the available spots each time, now when I get a request for park, I simply remove the forst element from queue.
          2. Adding an element will take tias it reorders elements by Priority Queue ---
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

14. Multiple entrances/exits: How would you prevent two entrances from assigning the saslot at the satime?
    
          1. Will use synchronization/locks to prevent booking saspot at once 

15. Large parking lots: With thousands of floors and millions of slots, will your slot allocation still perform well? If not, how would you optimize?

16. Multi-slot vehicles: How would you handle a vehicle type (like a bus/truck) that needs multiple consecutive slots?

          1. Our design assigns slots such that CAR takes 4 spaces, BIKE takes 2, TRUCK takes 8 -> we specifically do not modify...
             Polished version -> Currently each slot is configured for a type(like car slot taking 4 spaces, bike taking 1 etc...), But in case it is unit-size slot, 
            I can redesign and assign consecutive slots for required vehicle

17. Concurrency: If two threads try to park/unpark the savehicle at once, what happens? How would you ensure correctness?

          1. aways we make sure there is synchronization/locks and the variable isEmpty is made volatile so that every thread always has latest data(consistent)
            wrong understanding of ques -> what if 2 threads try to park savehicle....duplicate parking -> Create a concurrent map (check if vehicle exists, otheriwse add it)

18. Reporting: How would you implement a feature to get current availability (per vehicle type, per floor)?

          1. I already have support for VehicleType -> getAvailableParkingSpots which returns per type, per floor

19. Extensibility: Suppose tomorrow we need “EV Charging” slots or “Reserved Slots for VIPs.” How would your design adapt?

20. Persistence: Right now everything is in memory. How would you add DB/file persistence without rewriting business logic?

          1. I will simply add JDBC Connector logic and instead of updating and reading from Map, I will write DB query and replace it there
          2. If it's file, sathing, write and read from file instead of map

21. Search: If I want to query “Where is my car?” by registration number, how would you implement it?

          1. Check 13

22. Clean-up: How do you prevent stale tickets (vehicle already exited) from staying in memory forever?
       
          1. I would have a CRON JOB which runs daily at the silent hours(via API to which triggers) to clean the tickets which are invalidated from in memory


# Figure out
    1. Extensibility -> Something like adding EV Charging Spots -> I just have idea of sing Factory pattern and create interface and implement it,
        but not quite sure on how  to implement pricing specifically for this. 
    2. Large scale search -> how to search for a spot in < O(n) tias you might have tens/thousand of floors and millions of slots per floor
    3. Multi slot vehicles -> So is it something like a SLOT can implicitly take only bike and for CAR or TRUCK we should have 2 slots ??
    4. Concurrency: park/unpark savehicle at once, How to handle correctness -> always we make sure there is synchronization/locks  
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
            if (~word != 0) { // means not all 1s (soslot is free)
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

# Interviewer Follup questions in detail
Interviewer (me):

Your current ParkingLot system is fully in-memory. Suppose Flipkart wants to roll this out across multiple mall locations, where crashes/restarts can’t lose data.
How would you add persistence (say DB/file) — but make sure your business logic (like slot allocation, ticketing) doesn’t need rewriting?

Interviewee:
There is already a ParkingTicketDao class which has methods like addParkingTicket, findTicketById, findTicketByVehicleNum... Here instead of using a in-memory Map, I will simply introduce DB Connection and add DB query to it, which is not disturbing any core business logic

Interviewer:
Nice — you spotted that your DAO already abstracts the data access 👍 That’s the right instinct.
But now imagine I’m the interviewer poking holes:

(interviewer):

Okay, you say “just replace the map with DB queries” — but won’t that mean your service layer now directly depends on JDBC/SQL code? What if tomorrow Flipkart moves from MySQL → Cassandra → Redis? Wouldn’t you have to rewrite DAO methods each time? How will you ensure your business logic truly stays untouched?
👉 How would you refine your answer here?

Interviewee:
I would make sure that DAO is accessing constants like GET_QUERY_BY_ID, GET_QUERY_BY_VEHICLE_NUM, and under the hood I would simply modify these queries, and my actual code in DAL doesn't gets touched

Interviewer:
Good try 👌 — you’re moving in the right direction, but let push a bit more like an interviewer:

Hmm, so your idea is to keep SQL queries in constants and swap them when DB changes. But what if tomorrow Flipkart moves to NoSQL (say Cassandra), where there is no SQL? Just swapping queries won’t work.
How will your design keep the business logic isolated even across fundamentally different storage types?

👉 Want to take a crack at answering that before I give you a polished version?

Interviewee:
Let's say we are swapping the storage type, I haven't worked with Cassandra, But I can answer by keeping the knowledge of Nosql(MongoDB)-> It also has simple queries like db.users.find() and sometimes requires forming a object with predefined fields, It is also like forming a query in SQL... so the strategy remians same
Interviewer

That’s a reasonable first instinct 👍 — you’re saying “queries differ, but structure (DAO interface) remains.” That’s partially right, but an interviewer may push back further:

(interviewer):

Okay, but notice — in MongoDB you don’t do joins, schemas aren’t enforced, and queries are quite different in shape compared to SQL. If you directly embed Mongo queries in your DAO, you’ll again tie the service layer to Mongo.
What if Flipkart wants to run the sacode against either SQL or NoSQL (maybe for A/B rollout)? How will you design so that your ParkingLot service doesn’t even know which backend is used?

👉 Want to give it one more try before I give you the polished “expected” design answer?

Interviewee:
I will create a new interface for DB interactions and then have separate classes implement them, Like sql, nosql (cassandra, mongodb etc), I would use a strategy to decide which to pick amongst them, this way my code is not directly dependent on sql/nosql queries, but on interface which has get, save methods??

Interviewer

🔥 Exactly — that’s the polished expected answer. You’ve landed on it!

The interviewer wants to see if you separate what the app wants to do (e.g., findTicketById, saveTicket) from how it’s done (SQL, Mongo, Cassandra).

So:

Define an interface ParkingTicketRepository (methods like save(Ticket), findById(String), findByVehicleNum(String)).

Have multiple implementations: SqlParkingTicketRepository, MongoParkingTicketRepository, CassandraParkingTicketRepository.

At runtime, use a factory/strategy to decide which one to inject based on config.

That way:
✅ Business logic → talks only to the interface.
✅ Storage can change (SQL, NoSQL, in-memory) → business layer doesn’t care.
✅ Extensible for future (e.g., Redis cache layer, file-based persistence).

Do you want to now act as interviewer and push a bit further — e.g., “how will you unit test this without a real DB”?

Interviewee:
yes
Interviewer

Interviewer (me):

Okay, sounds good. But let’s say we’re writing unit tests for your ParkingLot. We don’t want tests to spin up a real DB or Mongo.
How will you test your service logic without depending on an actual database?

🎤 Your turn — answer like in interview.

Interviewee:
I would like to create a MockService and have the mocks created
Interviewer

💡 Nice instinct — mocks are exactly the direction expected. But the interviewer might push for clarity:

If you answer “I’ll create a MockService”, they’ll likely follow up:

👉 “Okay, but how does that tie into your repository abstraction? Walk through concretely.”

A polished version could be:

Since my business layer depends only on the ParkingTicketRepository interface, in tests I don’t need a real DB implementation.

I can create a Mock / Stub implementation of ParkingTicketRepository (using libraries like Mockito, or just a simple in-memory HashMap implementation).

Then I inject that mock into the service under test.

This way, unit tests verify only business logic (e.g., slot allocation, ticket expiry) without external dependencies.

Later, I can add integration tests with real DB to verify queries.

⚡ Interviewer follow-up (me):

Let’s say you already have an in-memory implementation today. How will you ensure that doesn’t “leak” into production accidentally when you introduce real DB repositories?

Would you like to try answering that one?

Interviewee:
I would make sure by testing it out that it is actually referring db's(via Mocks) and not in memory
Interviewer

Good start 👍 but an interviewer might push back:

👉 “Testing is fine, but how do you enforce at design or deployment level that production never accidentally uses the in-memory mock?”

A stronger refinement could be:

Use dependency injection (Spring or Guice).

Provide multiple bean implementations of ParkingTicketRepository — e.g. InMemoryRepository, SqlRepository, CassandraRepository.

Use profiles/configuration to control which bean is active:

test profile → loads InMemoryRepository.

prod profile → loads SqlRepository.

That way, it’s impossible for mock to “leak” into prod, since prod configs won’t even load that bean.

⚡ My next interviewer-style probe to you:
Suppose you have two DBs at the sati(say SQL for historical data, Cassandra for real-time). How would your repository abstraction handle that? Would you create two repos, or one repo that knows both?
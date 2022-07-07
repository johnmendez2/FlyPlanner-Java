package F28DA_CW2;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class FlyPlannerTest {

	FlyPlannerImpl fi;

	@Before
	public void initialize() {
		fi = new FlyPlannerImpl();
		try {
			fi.populate(new FlightsReader());
		} catch (FileNotFoundException | FlyPlannerException e) {
			e.printStackTrace();
		}
	}

	// Add your own tests here!
	//
	// You can get inspiration from the tests in FlyPlannerProvidedTest
	// that uses the provided data set but also from the
	// leastCostCustomTest test that uses a custom-made graph

	@Test
	public void leastCostExclTest() {
		try {
			LinkedList<String> exclude = new LinkedList<String>();
			exclude.add("LHR");
			exclude.add("PRG");
			exclude.add("LGW");
			TripImpl i = fi.leastCost("BOM", "LCY", exclude);
			System.out.println(i.getStops());
			System.out.println(i.totalCost());
			assertEquals(435, i.totalCost());
		} catch (FlyPlannerException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void leastHopTest() {
		try {
			TripImpl i = fi.leastHop("BOM", "LHR");
			System.out.println(i.totalHop());
			assertEquals(1, i.totalHop());
		} catch (FlyPlannerException e) {
			fail();
		}
	}
	
	@Test
	public void leastCostTest() {
		try {
			TripImpl i = fi.leastCost("ONT", "DXB");
			assertEquals(4, i.totalHop());
			assertEquals(831, i.totalCost());
		} catch (FlyPlannerException e) {
			fail();
		}
	}
	
	@Test
	public void directlyConnectedTest() {
		AirportImpl lhr = fi.airport("LHR");
		Set<AirportImpl> s = fi.directlyConnected(lhr);
		assertEquals(138, s.size());
	}

	@Test
	public void setDirectlyConnectedTest() {
		int sum = fi.setDirectlyConnected();
		assertEquals(9436, sum);
	}

	@Test
	public void betterConnectedInOrderEDITest() {
		fi.setDirectlyConnected();
		fi.setDirectlyConnectedOrder();
		AirportImpl edi = fi.airport("EDI");
		Set<AirportImpl> betterConnected = fi.getBetterConnectedInOrder(edi);
		assertEquals(30, betterConnected.size());
	}

	@Test
	public void betterConnectedInOrderDXBTest() {
		fi.setDirectlyConnected();
		fi.setDirectlyConnectedOrder();
		AirportImpl dxb = fi.airport("DXB");
		Set<AirportImpl> betterConnected = fi.getBetterConnectedInOrder(dxb);
		assertEquals(11, betterConnected.size());
	}

	@Test
	public void leastCostTimeTest() {
		try {
			TripImpl i = fi.leastCost("EDI", "DXB");
			assertEquals(1495, i.connectingTime());
			assertEquals(1931, i.totalTime());
		} catch (FlyPlannerException e) {
			fail();
		}
	}

	@Test
	public void leastCostMeetUpTest() {
		try {
			String meetUp = fi.leastCostMeetUp("EDI", "DXB");
			assertEquals("IST", meetUp);
		} catch (FlyPlannerException e) {
			fail();
		}
	}

	@Test
	public void leastTimeMeetUpTest() {
		try {
			String meetUp = fi.leastTimeMeetUp("NCL", "NTL", "0600");
			assertEquals("CDG", meetUp);
		} catch (FlyPlannerException e) {
			fail();
		}
	}
	
	@Test
	public void leastCostCustomTest() {
		FlyPlannerImpl fp = new FlyPlannerImpl();
		HashSet<String[]> airports = new HashSet<String[]>();
		String[] a1= {"A1","City1","AirportName1"}; airports.add(a1);
		String[] a2= {"A2","City2","AirportName2"}; airports.add(a2);
		String[] a3= {"A3","City2","AirportName3"}; airports.add(a3);
		HashSet<String[]>  flights = new HashSet<String[]>();
		String[] f1= {"F1","A1","1000","A2","1100","500"}; flights.add(f1);
		String[] f2= {"F2","A1","1000","A3","1100","50"}; flights.add(f2);
		String[] f3= {"F3","A3","1000","A2","1100","50"}; flights.add(f3);
		fp.populate(airports, flights);
		try {
			System.out.println(fp);
			TripImpl lc = fp.leastCost("A1", "A2");
			assertEquals(100,lc.totalCost());
		} catch (FlyPlannerException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void leastHopCustomTest() {
		FlyPlannerImpl fp = new FlyPlannerImpl();
		HashSet<String[]> airports = new HashSet<String[]>();
		String[] a1= {"A1","City1","AirportName1"}; airports.add(a1);
		String[] a2= {"A2","City2","AirportName2"}; airports.add(a2);
		String[] a3= {"A3","City2","AirportName3"}; airports.add(a3);
		HashSet<String[]>  flights = new HashSet<String[]>();
		String[] f1= {"F1","A1","1000","A2","1100","50"}; flights.add(f1);
		String[] f2= {"F2","A1","1000","A3","1100","50"}; flights.add(f2);
		String[] f3= {"F3","A3","1000","A2","1100","50"}; flights.add(f3);
		fp.populate(airports, flights);
		try {
			System.out.println(fp);
			TripImpl lc = fp.leastHop("A1", "A2");
			assertEquals(1, lc.totalHop());
		} catch (FlyPlannerException e) {
			e.printStackTrace();
			fail();
		}
	}


}

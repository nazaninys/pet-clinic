package org.springframework.samples.petclinic.owner;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Assume;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(Theories.class)
public class PetTest {

	private static Visit visit1,visit2,visit3;
	Pet pet;

	@Before
	public  void setup() {
		pet = new Pet();
	}


	@DataPoints
	public static List<List<Visit>> visits() {
		visit1 = new Visit();
		visit2 = new Visit();
		visit3 = new Visit();

		visit1.setDate(LocalDate.of(2021,6,17));
		visit2.setDate(LocalDate.of(2021,1,1));
		visit3.setDate(LocalDate.of(2021,10,15));

		List<List<Visit>> visits = new ArrayList<>();
		visits.add(Arrays.asList(visit1,visit2,visit3));
		visits.add(Arrays.asList(visit2,visit3,visit1));
		visits.add(Arrays.asList(visit3,visit2,visit1));
		visits.add(Arrays.asList(visit3,visit1,visit2));
		visits.add(Arrays.asList(visit2,visit1,visit3));
		visits.add(Arrays.asList(visit1,visit3,visit2));
		return visits;

	}


	@Theory
	public void testGetVisitSortedDescending(List<Visit> visits){

		Assume.assumeNotNull(visits);

		List<LocalDate> sortedList = Arrays.asList(visit3.getDate(),visit1.getDate(),visit2.getDate());
		visits.forEach(pet::addVisit);
		List<LocalDate> actualDates = new ArrayList<>();
		for(Visit visit : pet.getVisits())
			actualDates.add(visit.getDate());

		Assert.assertEquals(sortedList,actualDates);

	}


}

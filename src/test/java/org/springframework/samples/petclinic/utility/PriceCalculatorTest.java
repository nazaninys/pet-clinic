package org.springframework.samples.petclinic.utility;

import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.visit.Visit;

import org.junit.Assert;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import static org.mockito.Mockito.*;
import org.junit.Test;


public class PriceCalculatorTest {

	private static int INFANT_YEARS = 2;
	private static double DELTA = 1e-15;


	private void setPetBirthDate(Pet pet,int age) {
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.YEAR, -1 * age);
		LocalDate birthDate = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		when(pet.getBirthDate()).thenReturn(birthDate);
	}

	private void setVisit(Pet pet,int age,int daysFromLastVisit){
		List<Visit> visits = new ArrayList<>();
		Visit visit = spy(Visit.class);
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DATE, -1 * daysFromLastVisit);
		LocalDate visitDate = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		when(visit.getDate()).thenReturn(visitDate);
		visits.add(visit);
		when(pet.getVisitsUntilAge(age)).thenReturn(visits);
	}

	private List<Pet> createPetListWithDesiredSize(int size) {
		List <Pet> pets = new ArrayList<>();
		for (int i=0; i< size; i++) {
			Pet pet = mock(Pet.class);
			pets.add(pet);
		}
		return pets;
	}

	@Test
	public void testNotInfantPetNoVisitDiscountCounterLessThanMin(){
		List<Pet> pets = createPetListWithDesiredSize(1);
		setPetBirthDate(pets.get(0),INFANT_YEARS + 1);
		Assert.assertEquals(12.0,PriceCalculator.calcPrice(pets,10.0,10.0),DELTA);
	}

	@Test
	public void testNoVisitNotInfantDiscountCounterEqMin(){
		List<Pet> pets = createPetListWithDesiredSize(10);
		for(Pet pet:pets){
			setPetBirthDate(pet,INFANT_YEARS + 1);
		}
		Assert.assertEquals(238,PriceCalculator.calcPrice(pets,10.0,10.0),DELTA);
	}

	@Test
	public void testVisitInfantBoundaryOnlyOneVisit(){
		List<Pet> pets = createPetListWithDesiredSize(6);
		for(Pet pet:pets){
			setPetBirthDate(pet,INFANT_YEARS);
		}
		setVisit(pets.get(5),INFANT_YEARS,110);
		PriceCalculator.calcPrice(pets,10.0,10.0);
		Assert.assertEquals(359.2,PriceCalculator.calcPrice(pets,10.0,10.0),DELTA);
	}

	@Test
	public void testVisitBoundaryInfant(){
		List<Pet> pets = createPetListWithDesiredSize(6);
		for(Pet pet:pets){
			setPetBirthDate(pet,INFANT_YEARS - 1);
		}

		setVisit(pets.get(5),INFANT_YEARS - 1,100);
		PriceCalculator.calcPrice(pets,10.0,10.0);
		Assert.assertEquals(359.2,PriceCalculator.calcPrice(pets,10.0,10.0),DELTA);
	}
}

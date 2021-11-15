package org.springframework.samples.petclinic.model.priceCalculators;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;

import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


public class SimplePriceCalculatorTests {

	private static final double DELTA = 1e-15;

	SimplePriceCalculator calculator;

	@Before
	public void setup(){

		calculator = new SimplePriceCalculator();
	}

	@Test
	public void testCalcPrice(){
		List<Pet> petsList = createPets();
		Assert.assertEquals(petsList.size(),1);
		UserType userType = UserType.GOLD;
		Assert.assertEquals(20.0,calculator.calcPrice(petsList,10,10,userType),DELTA);

	}
	@Test
	public void testCalcPriceWithNewUserRarePet(){
		List<Pet> petsList = createRarePets();
		Assert.assertEquals(petsList.size(),1);
		UserType userType = UserType.NEW;
		Assert.assertEquals(20.9,calculator.calcPrice(petsList,10,10,userType),DELTA);

	}

	private List<Pet> createPets(){
		List<Pet> petsList = new ArrayList<>();
		Pet petSpy = spy(Pet.class);
		PetType petType = spy(PetType.class);
		when(petType.getRare()).thenReturn(false);
		when(petSpy.getType()).thenReturn(petType);
		petsList.add(petSpy);
		return petsList;

	}
	private List<Pet> createRarePets(){
		List<Pet> petsList = new ArrayList<>();
		Pet petSpy = spy(Pet.class);
		PetType petType = spy(PetType.class);
		when(petSpy.getType()).thenReturn(petType);
		petsList.add(petSpy);
		return petsList;

	}


}

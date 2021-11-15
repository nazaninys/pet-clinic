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

	private List<Pet> createPetStub(){
		List<Pet> petsList = new ArrayList<>();
		Pet petSpy = spy(Pet.class);
		petsList.add(petSpy);
		return petsList;
	}

	private void setPetRare(boolean isRare, Pet pet) {
		when(pet.getType()).thenReturn(mock(PetType.class));
		if (isRare)
			when(pet.getType().getRare()).thenReturn(true);
		else
			when(pet.getType().getRare()).thenReturn(false);
	}


	@Test
	public void testCalcPricePetNotRareNotNew(){
		List<Pet> petsList = createPetStub();
		setPetRare(false, petsList.get(0));
		Assert.assertEquals(petsList.size(),1);
		UserType userType = UserType.GOLD;
		Assert.assertEquals(20.0,calculator.calcPrice(petsList,10,10,userType),DELTA);

	}

	@Test
	public void testCalcPriceWithNewUserRarePet(){
		List<Pet> petsList = createPetStub();
		setPetRare(true, petsList.get(0));
		Assert.assertEquals(petsList.size(),1);
		UserType userType = UserType.NEW;
		Assert.assertEquals(20.9,calculator.calcPrice(petsList,10,10,userType),DELTA);

	}



}

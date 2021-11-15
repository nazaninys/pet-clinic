package org.springframework.samples.petclinic.model.priceCalculators;

import org.hibernate.validator.constraints.br.TituloEleitoral;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.junit.*;
import org.mockito.MockitoAnnotations;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;
import static org.junit.Assert.*;
import java.util.*;


import static org.mockito.Mockito.*;

public class CustomerDependentPriceCalculatorTest {
	private CustomerDependentPriceCalculator customerDependentPriceCalculator;
	private UserType userType;
	private double DELTA = 1e-15;
	private static int INFANT_YEARS = 2;
	private static double RARE_INFANCY_COEF = 1.4;
	private static double COMMON_INFANCY_COEF = 1.2;
	private static double BASE_RARE_COEF = 1.2;
	private static int DISCOUNT_MIN_SCORE = 10;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		customerDependentPriceCalculator = new CustomerDependentPriceCalculator();
	}

	private List<Pet> createPetListWithDesiredSize(int size) {
		List <Pet> pets = new ArrayList<>();
		for (int i=0; i< size; i++) {
			Pet pet = mock(Pet.class);
			pets.add(pet);
		}
		return pets;
	}

	private void setPetDate(boolean isInfant, Pet pet) {
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		if (isInfant)
			c.add(Calendar.YEAR, -1);
		else
			c.add(Calendar.YEAR, -1 * (INFANT_YEARS + 2));
		Date birthDate = c.getTime();
		when(pet.getBirthDate()).thenReturn(birthDate);
	}

	private void setPetRare(boolean isRare, Pet pet) {
		when(pet.getType()).thenReturn(mock(PetType.class));
		if (isRare)
			when(pet.getType().getRare()).thenReturn(true);
		else
			when(pet.getType().getRare()).thenReturn(false);
	}

	@Test
	public void testCalcPriceWhenNoLoopAndDiscountFalseAndUserTypeGold() {
		List<Pet> pets = createPetListWithDesiredSize(0);
		userType = UserType.GOLD;
		double totalPrice = 0, baseCharge = 1;
		totalPrice = (totalPrice * userType.discountRate) + baseCharge;
		assertEquals(totalPrice, customerDependentPriceCalculator.calcPrice(pets, baseCharge, 0, userType), DELTA);
	}

	@Test
	public void testCalcPriceWithNotRareAndNotInfantPetAndDiscounterCondTrueAndTypeNotNew() {
		List<Pet> pets = createPetListWithDesiredSize(10);
		for (Pet pet: pets) {
			setPetDate(false, pet);
			setPetRare(false, pet);
		}
		userType = UserType.SILVER;
		double totalPrice = 0, basePricePerPet = 1, baseCharge = 2;
		for (int i=0; i<10; i++)
			totalPrice += basePricePerPet;
		totalPrice = (totalPrice + baseCharge) * userType.discountRate;
		assertEquals(totalPrice, customerDependentPriceCalculator.calcPrice(pets, baseCharge, basePricePerPet, userType), DELTA);
	}

	@Test
	public void testCalcPriceWhenPet1RareAndNotInfantAndPet2NotRareAndInfantAndConditionsFalse() {
		List<Pet> pets = createPetListWithDesiredSize(2);
		setPetDate(false, pets.get(0));
		setPetRare(true, pets.get(0));
		setPetDate(true, pets.get(1));
		setPetRare(false, pets.get(1));
		double totalPrice = 0, basePricePerPet = 1, price = 0;
		userType = UserType.SILVER;
		totalPrice += basePricePerPet * BASE_RARE_COEF;
		price = basePricePerPet;
		price = price * COMMON_INFANCY_COEF;
		totalPrice += price;
		assertEquals(totalPrice, customerDependentPriceCalculator.calcPrice(pets, 0, basePricePerPet, userType), DELTA);

	}

	@Test
	public void testCalPricePetsRareAndInfantAndDiscounterTrueAndUserTypeNew() {
		List<Pet> pets = createPetListWithDesiredSize(5);
		for (Pet pet: pets) {
			setPetDate(true, pet);
			setPetRare(true, pet);
		}
		userType = UserType.NEW;
		double totalPrice = 0, basePricePerPet = 2, baseCharge = 1;
		for (int i=0; i < pets.size(); i++) {
			double price = 0;
			price = basePricePerPet * BASE_RARE_COEF;
			price = price * RARE_INFANCY_COEF;
			totalPrice += price;
		}
		totalPrice = (totalPrice * userType.discountRate) + baseCharge;
		assertEquals(totalPrice, customerDependentPriceCalculator.calcPrice(pets, baseCharge, basePricePerPet, userType), DELTA);
	}


}

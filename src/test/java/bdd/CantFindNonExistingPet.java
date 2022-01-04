package bdd;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.*;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CantFindNonExistingPet {
	@Mock
	PetTimedCache pets;
	@Mock
	OwnerRepository owners;
	@Mock
	Logger criticalLogger;

	private PetService petService;
	private Pet pet;
	private Pet foundPet;
	private int id;

	@Before()
	public void setup() {
		MockitoAnnotations.initMocks(this);
		petService = new PetService(pets, owners, criticalLogger);
	}

	@Given("A pet is introduced with id {int}")
	public void a_pet_is_introduced_with_id(Integer int1) {
		pet = mock(Pet.class);
		when(pet.getId()).thenReturn(int1);
		id = int1;
	}

	@Given("The pet is not in our list")
	public void the_pet_is_not_in_our_list() {
		when(pets.get(321)).thenReturn(mock(Pet.class));
		when(pets.get(id)).thenReturn(null);
	}

	@When("We try to find the pet")
	public void we_try_to_find_the_pet() {
		foundPet = petService.findPet(id);
	}

	@Then("It cannot be found in the list")
	public void it_cannot_be_found_in_the_list() {
		assertNull(foundPet);
		assertNotEquals(foundPet, pet);
	}


}

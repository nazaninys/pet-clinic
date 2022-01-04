package bdd;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.*;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FindPetSteps {

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


	@Given("There is a pet with id {int}")
	public void there_is_a_pet_with_id(Integer int1) {
		pet = mock(Pet.class);
		when(pets.get(int1)).thenReturn(pet);
		id = int1;
	}

	@When("Manager wants to find it by id")
	public void manager_wants_to_find_it_by_id() {
		foundPet = petService.findPet(id);
	}

	@Then("The pet is found successfully")
	public void the_pet_is_found_successfully() {
		assertEquals(pet, foundPet);
	}
}

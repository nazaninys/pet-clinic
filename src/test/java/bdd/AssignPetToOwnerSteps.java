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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AssignPetToOwnerSteps {
	@Mock
	PetTimedCache pets;
	@Mock
	OwnerRepository owners;
	@Mock
	Logger criticalLogger;

	private PetService petService;
	private Pet pet;
	private Owner owner;

	@Before()
	public void setup() {
		MockitoAnnotations.initMocks(this);
		petService = new PetService(pets, owners, criticalLogger);
	}

	@Given("There exists an owner with id {int}")
	public void there_exists_an_owner_with_id(Integer int1) {
		owner = mock(Owner.class);
	}

	@Given("There exist a pet with id {int}")
	public void there_exist_a_pet_with_id(Integer int1) {
		pet = mock(Pet.class);
	}

	@When("Owner wants to own the pet")
	public void owner_wants_to_own_the_pet() {
		petService.savePet(pet, owner);
	}

	@Then("The pet is saved to owner's list")
	public void the_pet_is_saved_to_owner_s_list() {
		verify(owner).addPet(pet);
	}

	@Then("The pet is saved to repository")
	public void the_pet_is_saved_to_repository() {
		verify(pets).save(pet);
	}
}

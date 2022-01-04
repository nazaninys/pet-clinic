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


import static org.mockito.Mockito.*;


public class FindOwnerSteps {
	@Mock
	PetTimedCache pets;
	@Mock
	OwnerRepository owners;
	@Mock
	Logger criticalLogger;

	private PetService petService;
	private Owner owner;
	private Owner foundOwner;
	private int id;

	@Before()
	public void setup() {
		MockitoAnnotations.initMocks(this);
		petService = new PetService(pets, owners, criticalLogger);
	}


	@Given("There is an owner with id {int}")
	public void there_is_an_owner_with_id(Integer int1) {
		owner = mock(Owner.class);
		when(owners.findById(int1)).thenReturn(owner);
		id = int1;
	}

	@When("Manager wants to find him by id")
	public void manager_wants_to_find_him_by_id() {
		foundOwner = petService.findOwner(id);
	}

	@Then("The owner is found successfully")
	public void the_owner_is_found_successfully() {
		assertEquals(owner, foundOwner);
	}

}

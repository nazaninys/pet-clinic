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
import static org.mockito.Mockito.*;


public class NewPetIsAddedToOwnerPetsSteps {
	@Mock
	PetTimedCache pets;
	@Mock
	OwnerRepository owners;
	@Mock
	Logger criticalLogger;

	private PetService petService;
	private Owner owner;

	@Before()
	public void setup() {
		MockitoAnnotations.initMocks(this);
		petService = new PetService(pets, owners, criticalLogger);
	}

	@Given("When there is an owner called {string}")
	public void when_there_is_an_owner_called(String string) {
		owner = mock(Owner.class);
		when(owner.getFirstName()).thenReturn(string);
	}

	@When("She wants to have a new pet")
	public void she_wants_to_have_a_new_pet() {
		petService.newPet(owner);
	}

	@Then("A pet should be added to her pets")
	public void a_pet_should_be_added_to_her_pets() {
		verify(owner).addPet(any(Pet.class));
	}

}

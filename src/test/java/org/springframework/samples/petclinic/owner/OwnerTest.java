package org.springframework.samples.petclinic.owner;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import org.mockito.*;
import java.util.*;
import static org.mockito.Mockito.*;

public class OwnerTest {
	private Owner owner;
	private Pet petMock;

	private void callAddPet() {
		owner.addPet(petMock);
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		owner =  new Owner();
		petMock = spy(Pet.class);
	}

	@After
	public void teardown() {
		owner = null;
		petMock = null;
	}

	//state verification
	@Test
	public void testOwnerSetsWhenAddPet() {
		callAddPet();
		assertEquals(owner, petMock.getOwner());
	}

	//state verification
	@Test
	public void testExistedPetNotAdd() {
		when(petMock.isNew()).thenReturn(false);
		callAddPet();
		Set<Pet> pets = owner.getPetsInternal();
		assertFalse(pets.contains(petMock));
	}


	//state verification
	@Test
	public void testNewPetAdd() {
		callAddPet();
		Set<Pet> pets = owner.getPetsInternal();
		assertTrue(pets.contains(petMock));
	}

	//behaviour verification
	@Test
	public void testAddPetAddsToPetsList() {
		Set <Pet> pets = spy(Set.class);
		owner.setPetsInternal(pets);
		callAddPet();
		verify(pets).add(petMock);
	}


	//state verification
	@Test
	public void testOwnerChangesWhenPetAddsToNewOwner() {
		callAddPet();
		Owner newOwner = new Owner();
		newOwner.addPet(petMock);
		assertEquals(newOwner, petMock.getOwner());
	}

	//behaviour verification
	@Test
	public void testSetOwnerCallsWhenPetAddsToNewOwner() {
		callAddPet();
		Owner newOwner = new Owner();
		newOwner.addPet(petMock);
		verify(petMock, times(2)).setOwner(any(Owner.class));
	}


	//behaviour verification
	@Test
	public void testSetOwnerCallsWithDifferentConditions() {
		when(petMock.isNew()).thenReturn(true).thenReturn(false);
		callAddPet();
		callAddPet();
		verify(petMock, times(2)).setOwner(owner);
	}

}

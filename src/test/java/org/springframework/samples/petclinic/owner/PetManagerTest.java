package org.springframework.samples.petclinic.owner;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.samples.petclinic.utility.SimpleDI;

import static org.mockito.Mockito.*;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;


@ExtendWith(MockitoExtension.class)
class PetManagerTest {

	@Mock
	PetTimedCache pets;

	@Mock
	OwnerRepository owners;

	@Mock
	Logger criticalLogger;

	@InjectMocks
	PetManager petManager;


	@Test
	public void testFindOwner() {

		Owner owner = new Owner();
		when(owners.findById(123)).thenReturn(owner);
		assertEquals(owner, petManager.findOwner(123));
	}

	@Test
	public void testNewPet() {
		Owner ownerMock = mock(Owner.class);
		petManager.newPet(ownerMock);
		verify(ownerMock).addPet(any(Pet.class));

	}

	@Test
	public void testFindPet() {

		Pet pet = new Pet();
		when(pets.get(124)).thenReturn(pet);
		assertEquals(pet, petManager.findPet(124));

	}

	@Test
	public void testPetNotFound() {

		when(pets.get(123)).thenReturn(null);
		assertNull(petManager.findPet(123));

	}

	@Test
	public void testSavePet() {

		Owner ownerMock = mock(Owner.class);
		Pet petMock = mock(Pet.class);
		petManager.savePet(petMock, ownerMock);
		verify(ownerMock).addPet(petMock);
		verify(pets).save(petMock);

	}

}

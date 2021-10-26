package org.springframework.samples.petclinic.owner;

import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.slf4j.Logger;
import org.springframework.samples.petclinic.utility.PetTimedCache;


import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


class PetManagerTest {
	@Mock
	PetTimedCache pets;
	@Mock
	OwnerRepository owners;
	@Mock
	Logger criticalLogger;

	private PetManager petManager;
	private int id = 123;

	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
		petManager = new PetManager(pets, owners, criticalLogger);
	}

	@AfterEach
	void teardown() {
		petManager = null;
	}

	//owners:stub - owner:dummy -
	//state verification
	@Test
	public void testFindOwner() {
		Owner owner = spy(Owner.class);
		when(owners.findById(123)).thenReturn(owner);
		assertEquals(owner, petManager.findOwner(123));
	}


	//ownerMock:mock
	//behavior verification
	@Test
	public void testNewPetCallsOwnerMethod() {
		Owner ownerMock = mock(Owner.class);
		petManager.newPet(ownerMock);
		verify(ownerMock).addPet(any(Pet.class));
	}

	//owner:dummy
	//state verification
	@Test
	public void testNewPetNotNull() {
		Owner owner = spy(Owner.class);
		Pet newPet = petManager.newPet(owner);
		assertNotNull(newPet);
	}

	//pet:dummy - pets:stub
	//state verification
	@Test
	public void testFindPet() {
		Pet pet = spy(Pet.class);
		when(pets.get(124)).thenReturn(pet);
		assertEquals(pet, petManager.findPet(124));
	}

	//pets:stub
	//state verification
	@Test
	public void testFindPetReturnNullIfNotFound() {
		when(pets.get(123)).thenReturn(null);
		assertNull(petManager.findPet(123));
	}

	//pets:mock
	//behavior verification
	@Test
	public void testFindPetCallsPetsMethod() {
		int id = 123;
		petManager.findPet(id);
		verify(pets).get(id);
	}

	//ownerMock:mock - petMock:dummy
	//behavior verification
	@Test
	public void testSavePetCallsOwnerMethod() {
		Owner ownerMock = mock(Owner.class);
		Pet petMock = spy(Pet.class);
		petManager.savePet(petMock, ownerMock);
		verify(ownerMock).addPet(petMock);
	}

	//ownerMock:dummy - petMock:dummy - pets:mock
	//behavior verification
	@Test
	public void testSavePetCallsPetsMethod() {
		Owner ownerMock = spy(Owner.class);
		Pet petMock = spy(Pet.class);
		petManager.savePet(petMock, ownerMock);
		verify(pets).save(petMock);
	}

	//owner:stub - temp:dummy - pets:stub
	//state verification
	@Test
	public void testGetOwnerPets() {
		int id = 123;
		Owner owner = spy(Owner.class);
		List<Pet> petsList = new ArrayList<>();
		doReturn(owner).when(owners).findById(id);
		for (int i=0; i<4; i+=1) {
			Pet temp = spy(Pet.class);
			petsList.add(temp);
		}
		doReturn(petsList).when(owner).getPets();
		assertEquals(petsList, petManager.getOwnerPets(id));
	}


	//petManager1:stub? - owner:stub - temp:dummy
	//state verification
	@Test
	public void testGetOwnerPetsWithPetManagerStub() {
		PetManager petManager1 = spy(new PetManager(pets, owners, criticalLogger));
		Owner owner = spy(Owner.class);
		doReturn(owner).when(petManager1).findOwner(id);
		List<Pet> petsList = new ArrayList<>();
		for (int i=0; i<4; i+=1) {
			Pet temp = spy(Pet.class);
			petsList.add(temp);
		}
		doReturn(petsList).when(owner).getPets();
		assertEquals(petsList, petManager1.getOwnerPets(id));
	}

	//petManager1:spy? - owner:dummy
	//behavior verification
	@Test
	public void testGetOwnerPetsCallsFindOwner() {
		PetManager petManager1 = spy(new PetManager(pets, owners, criticalLogger));
		Owner owner = spy(Owner.class);
		doReturn(owner).when(petManager1).findOwner(anyInt());
		petManager1.getOwnerPets(id);
		verify(petManager1).findOwner(id);
	}

	//owner:stub - owners:stub - temp:stub - tempType:dummy
	//state verification
	@Test
	public void testGetOwnerPetTypes() {
		Owner owner = spy(Owner.class);
		List<Pet> petsList = new ArrayList<>();
		doReturn(owner).when(owners).findById(id);
		Set<PetType> petsTypesList = new HashSet<>();
		for (int i=0; i<4; i+=1) {
			Pet temp = spy(Pet.class);
			PetType tempType = spy(PetType.class);
			doReturn(tempType).when(temp).getType();
			petsTypesList.add(tempType);
			petsList.add(temp);
		}
		doReturn(petsList).when(owner).getPets();
		assertEquals(petsTypesList, petManager.getOwnerPetTypes(id));
	}

	//owner:stub - owners:stub - temp:stub - tempType:dummy
	//state verification
	@Test
	public void testOwnerPetTypesNotReturnRepeatedType() {
		Owner owner = spy(Owner.class);
		List<Pet> petsList = new ArrayList<>();
		doReturn(owner).when(owners).findById(id);
		Set<PetType> petsTypesList = new HashSet<>();
		PetType tempType = spy(PetType.class);
		petsTypesList.add(tempType);
		for (int i=0; i<4; i+=1) {
			Pet temp = spy(Pet.class);
			doReturn(tempType).when(temp).getType();
			petsList.add(temp);
		}
		doReturn(petsList).when(owner).getPets();
		assertEquals(petsTypesList, petManager.getOwnerPetTypes(id));
	}

	//petManager1:stub? - owner:stub - temp:stub - tempType:dummy
	//state verification
	@Test
	public void testGetOwnerPetTypesWithPetManagerStub() {
		PetManager petManager1 = spy(new PetManager(pets, owners, criticalLogger));
		Owner owner = spy(Owner.class);
		doReturn(owner).when(petManager1).findOwner(id);
		List<Pet> petsList = new ArrayList<>();
		Set<PetType> petsTypesList = new HashSet<>();
		for (int i=0; i<4; i+=1) {
			Pet temp = spy(Pet.class);
			PetType tempType = spy(PetType.class);
			doReturn(tempType).when(temp).getType();
			petsTypesList.add(tempType);
			petsList.add(temp);
		}
		doReturn(petsList).when(owner).getPets();
		assertEquals(petsTypesList, petManager1.getOwnerPetTypes(id));
	}

	//petManager1:spy? - owner:dummy
	//behavior verification
	@Test
	public void testGetOwnerPetTypesCallsFindOwner() {
		PetManager petManager1 = spy(new PetManager(pets, owners, criticalLogger));
		Owner owner = spy(Owner.class);
		doReturn(owner).when(petManager1).findOwner(anyInt());
		petManager1.getOwnerPetTypes(id);
		verify(petManager1).findOwner(id);
	}

	//pet:dummy - pets:mock
	//behavior verification
	@Test
	public void testGetVisitsBetweenCallsPetsMethod(){
		LocalDate startDate = LocalDate.of(2000,2,2);
		LocalDate endDate = LocalDate.of(2000,2,5);
		Pet pet = spy(Pet.class);
		doReturn(pet).when(pets).get(id);
		petManager.getVisitsBetween(id,startDate,endDate);
		verify(pets).get(id);
		verify(pet).getVisitsBetween(startDate,endDate);
	}

	//pet:mock - pets:stub
	//behavior verification
	@Test
	public void testGetVisitsBetweenCallsPetMethod(){
		LocalDate startDate = LocalDate.of(2000,2,2);
		LocalDate endDate = LocalDate.of(2000,2,5);
		Pet pet = mock(Pet.class);
		doReturn(pet).when(pets).get(id);
		petManager.getVisitsBetween(id,startDate,endDate);
		verify(pet).getVisitsBetween(startDate,endDate);
	}

	private Owner makeOwner() {
		Owner owner = spy(Owner.class);
		Pet pet1 = new Pet();
		pet1.setName("Lexi");

		Pet pet2 = new Pet();
		pet2.setName("Lex");


		owner.addPet(pet1);
		owner.addPet(pet2);
		return owner;
	}



}

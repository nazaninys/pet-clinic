package org.springframework.samples.petclinic.owner;

import org.junit.*;
import org.junit.experimental.theories.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;
import static org.mockito.Mockito.*;


@RunWith(Theories.class)
public class OwnerTest {
	private Owner owner;
	private Pet petMock;
	private String petName = "pishul";

	private void callAddPet() {
		owner.addPet(petMock);
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		owner =  new Owner();
		petMock = spy(Pet.class);
		petMock.setName(petName);
	}

	@After
	public void teardown() {
		owner = null;
		petMock = null;
	}

	@Test
	public void testOwnerSetsWhenAddPet() {
		callAddPet();
		assertEquals(owner, petMock.getOwner(), "owner is not set properly");
	}

	@Test
	public void testOwnerChangesWhenPetAddsToNewOwner() {
		callAddPet();
		Owner newOwner = new Owner();
		newOwner.addPet(petMock);
		assertEquals(newOwner, petMock.getOwner(), "owner has not changed");
	}

	@Test
	public void testNewPetAdd() {
		callAddPet();
		Set<Pet> pets = owner.getPetsInternal();
		assertTrue(pets.contains(petMock), "pet is not added");
	}

	@Test
	public void testExistedPetNotAdd() {
		when(petMock.isNew()).thenReturn(false);
		callAddPet();
		Set<Pet> pets = owner.getPetsInternal();
		assertFalse(pets.contains(petMock), "pet with id added");
	}

	@Test
	public void testGetExistingPet() {
		callAddPet();
		Pet foundPet = owner.getPet(petName);
		assertNotNull(foundPet, "Existing pet not found");
	}

	@Test
	public void testGetNonExistingPet() {
		Pet foundPet = owner.getPet(petName);
		assertNull(foundPet, "found wrong pet");
	}

	@Test
	public void testGetPetWhenIgnoreNew() {
		callAddPet();
		Pet foundPet = owner.getPet(petName, true);
		assertNull(foundPet, "returned object when not expected");
	}


	@Test
	public void testGetPetsSorted() {
		Pet pet1 = spy(Pet.class);
		pet1.setName("Lexi");
		Pet pet2 = spy(Pet.class);
		pet2.setName("Luxi");
		Pet pet3 = spy(Pet.class);
		pet3.setName("Luxe");
		Pet pet4 = spy(Pet.class);
		pet4.setName("Coco");
		owner.addPet(pet1);
		owner.addPet(pet2);
		owner.addPet(pet3);
		owner.addPet(pet4);
		List<Pet> expected = Arrays.asList(pet4, pet1, pet3, pet2);
		List<Pet> result = owner.getPets();
		assertEquals(expected, result);
	}


	@DataPoints
	public static boolean[] ignoreNews = {true, false};

	@DataPoint
	public static String name = "pishul";

	@Theory
	public void testNoPetReturnIfNotExist(boolean ignoreNew, String name) {
		Set<String> petNames = new HashSet<>();
		List<Pet> pets = owner.getPets();
		for (Pet pet :  pets)
			petNames.add(pet.getName());
		assumeFalse(petNames.contains(name));
		Pet retPet = owner.getPet(name, ignoreNew);
		assertTrue(retPet == null);
	}

	@Theory
	public void testFindPetIfNotNew(boolean ignoreNew, String name) {
		callAddPet();
		Set<String> petNames = new HashSet<>();
		List<Pet> pets = owner.getPets();
		for (Pet pet :  pets)
			petNames.add(pet.getName().toLowerCase());
		assumeTrue(petNames.contains(name.toLowerCase()));
		when(petMock.isNew()).thenReturn(false);
		Pet retPet = owner.getPet(name, ignoreNew);
		assertEquals(retPet.getName().toLowerCase(), name.toLowerCase());
	}

}

package org.springframework.samples.petclinic.owner;

import org.junit.*;
import org.junit.experimental.theories.*;
import org.junit.jupiter.api.Assumptions;
import org.junit.runner.RunWith;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.*;


@RunWith(Theories.class)
public class OwnerTest {
	private Owner owner;
	private Pet petMock;
	private static String PET_NAME = "pishul";

	private void callAddPet() {
		owner.addPet(petMock);
	}

	private List<Pet> createAndAddMultiplePets() {
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
		return Arrays.asList(pet4, pet1, pet3, pet2);
	}

	private void addPets(List<String> petNames){

		for(String name : petNames){
			owner.addPet(new Pet() {
				{
					setName(name.toLowerCase());
				}

			});
		}
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		owner =  new Owner();
		petMock = spy(Pet.class);
		petMock.setName(PET_NAME);
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
		Pet foundPet = owner.getPet(PET_NAME);
		assertNotNull(foundPet, "Existing pet not found");
	}

	@Test
	public void testGetNonExistingPet() {
		createAndAddMultiplePets();
		Pet foundPet = owner.getPet(PET_NAME);
		assertNull(foundPet, "found wrong pet");
	}

	@Test
	public void testGetPetWhenIgnoreNew() {
		callAddPet();
		Pet foundPet = owner.getPet(PET_NAME, true);
		assertNull(foundPet, "returned object when not expected");
	}


	@Test
	public void testGetPetsSorted() {
		List<Pet> expected = createAndAddMultiplePets();
		List<Pet> result = owner.getPets();
		assertEquals(expected, result, "list is not properly sorted");
	}

	@Test
	public void testGetPetCaseInsensitive() {
		callAddPet();
		String petMockUpperCaseName = petMock.getName().toUpperCase();
		Pet foundPet = owner.getPet(petMockUpperCaseName, false);
		assertEquals(foundPet, petMock, "getPet is case sensitive");
	}

	@DataPoints
	public static boolean[] ignoreNews = {true, false};

	@DataPoints
	public static String[] names  = {"pishul","coco","lexi"};

	@DataPoints
	public static List<List<String>> petNames (){
		List<List<String>> petNames = new ArrayList<>();
		petNames.add(Arrays.asList("dolme","kadoo"));
		petNames.add(Arrays.asList("pishul","pishi","pish"));

		return petNames;
	}

	@Theory
	public void testGetPetNoPetReturnIfNotExist(List<String> petNames, String name) {
		assumeTrue(!petNames.contains(name));
		addPets(petNames);
		Pet retPet = owner.getPet(name,false);
		assertNull(retPet, "pet should not be found");
	}

	@Theory
	public void testGetPetIfNotNew(List<String> petNames, String name, boolean ignoreNew) {
		callAddPet();
		assumeTrue(petNames.contains(name.toLowerCase()));
		assumeTrue(name.equals(petMock.getName()));
		when(petMock.isNew()).thenReturn(false);
		Pet retPet = owner.getPet(name, ignoreNew);
		assertEquals(retPet.getName().toLowerCase(), name.toLowerCase(), "found pet is not what expected");
	}

}

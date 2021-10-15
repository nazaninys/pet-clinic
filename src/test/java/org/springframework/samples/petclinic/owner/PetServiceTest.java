package org.springframework.samples.petclinic.owner;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.*;
import org.slf4j.Logger;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class PetServiceTest {
	private PetService petService;
	private static Pet petToBeFound;
	private Integer petId;
	private Pet result;
	private static Integer id1 = 610;
	private static Integer id2 = 581;
	@Mock
	private PetTimedCache pets;
	@Mock
	private PetRepository repository;
	@Mock
	private OwnerRepository owners;
	@Mock
	private Logger criticalLogger;


	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		petToBeFound = new Pet();
		petToBeFound.setId(id1);
		petService = new PetService(pets, owners, criticalLogger);
		when(repository.findById(id2)).thenReturn(null);
		when(repository.findById((id1))).thenReturn(petToBeFound);
	}

	@After
	public void teardown() {
		petService = null;
		petToBeFound = null;
	}

	public PetServiceTest(Integer id, Pet result) {
		this.petId = id;
		this.result = result;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> parameters() {
		return Arrays.asList (new Object [][] {{id1, petToBeFound}, {id2, null}});
	}

	@Test
	public void testFindPet() {
		assertEquals(petService.findPet(petId), result);
	}

}

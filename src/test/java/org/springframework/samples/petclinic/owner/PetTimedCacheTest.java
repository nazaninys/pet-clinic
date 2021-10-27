package org.springframework.samples.petclinic.owner;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mockito.*;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.internal.util.reflection.FieldSetter;
import static org.mockito.Mockito.*;

public class PetTimedCacheTest {
	private PetRepository repository;
	private PetTimedCache petTimedCache;
	private int id = 123;
	private Pet petToBeFound;

	private void addToActualMap() {
		petTimedCache.get(id);
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		repository = mock(PetRepository.class);
		petToBeFound = spy(Pet.class);
		petToBeFound.setId(id);
		petTimedCache = new PetTimedCache(repository);
		doReturn(petToBeFound).when(repository).findById(id);
	}

	@Test
	public void testGetWhenInRepository() {
		assertEquals(petToBeFound, petTimedCache.get(id));
	}

	@Test
	public void testGetWhenInMap() {
		addToActualMap();
		assertEquals(petToBeFound, petTimedCache.get(id));
	}

	@Test
	public void testGetReturnNullWhenNotExist() {
		assertNull(petTimedCache.get(1));
	}

	@Test
	public void testGetCallRepoWhenNotLocal() {
		petTimedCache.get(id);
		verify(repository).findById(id);
	}

	@Test
	public void testGetNotCallRepoWhenInLocal() {
		addToActualMap();
		petTimedCache.get(id);
		verify(repository, times(1)).findById(id);
	}


}

package org.springframework.samples.petclinic.owner;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(value = PetController.class,
	includeFilters = {
		@ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
		@ComponentScan.Filter(value = PetService.class, type = FilterType.ASSIGNABLE_TYPE),
		@ComponentScan.Filter(value = LoggerConfig.class, type = FilterType.ASSIGNABLE_TYPE),
		@ComponentScan.Filter(value = PetTimedCache.class, type = FilterType.ASSIGNABLE_TYPE),
	}
)
class PetControllerTests {

	private static final int OWNER_ID = 1;
	private static final int PET_ID = 10;
	private static final String PET_TYPE = "dog";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PetRepository pets;

	@MockBean
	private OwnerRepository owners;

	@BeforeEach
	void setup() {

		PetType dog = new PetType();
		dog.setId(3);
		dog.setName(PET_TYPE);

		Pet pet = new Pet();
		pet.setId(PET_ID);

		when(pets.findPetTypes()).thenReturn(Lists.newArrayList(dog));
		when(owners.findById(OWNER_ID)).thenReturn(new Owner());
		when(pets.findById(PET_ID)).thenReturn(pet);

	}

	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/new", OWNER_ID))
			.andExpect(status().isOk())
			.andExpect(content().contentType("text/html;charset=UTF-8"))
			.andExpect(view().name("pets/createOrUpdatePetForm"))
			.andExpect(model().attributeExists("pet"));
	}

	@Test
	void testSuccessProcessCreationForm() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", OWNER_ID)
			.param("name", "Lexi")
			.param("type", PET_TYPE)
			.param("birthDate", "2018-09-10"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@Test
	void testFailToProcessCreationFormIfNameIsEmpty() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", OWNER_ID)
			.param("type", PET_TYPE)
			.param("birthDate", "2018-09-10"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasNoErrors("owner"))
			.andExpect(model().attributeHasErrors("pet"))
			.andExpect(model().attributeHasFieldErrors("pet", "name"))
			.andExpect(model().attributeHasFieldErrorCode("pet", "name", "required"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@Test
	void testFailToProcessCreationFormIfBirthDateIsEmpty() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", OWNER_ID)
			.param("name", "Lexi")
			.param("type",PET_TYPE))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasNoErrors("owner"))
			.andExpect(model().attributeHasErrors("pet"))
			.andExpect(model().attributeHasFieldErrors("pet", "birthDate"))
			.andExpect(model().attributeHasFieldErrorCode("pet", "birthDate", "required"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@Test
	void testFailToProcessCreationFormIfTypeIsEmpty() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", OWNER_ID)
			.param("name", "Lexi")
			.param("birthDate", "2018-09-10"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasNoErrors("owner"))
			.andExpect(model().attributeHasErrors("pet"))
			.andExpect(model().attributeHasFieldErrors("pet", "type"))
			.andExpect(model().attributeHasFieldErrorCode("pet", "type", "required"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", OWNER_ID, PET_ID))
			.andExpect(status().isOk())
			.andExpect(content().contentType("text/html;charset=UTF-8"))
			.andExpect(model().attributeExists("pet"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@Test
	void testSuccessProcessUpdateForm() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", OWNER_ID, PET_ID)
			.param("name", "Lexi")
			.param("type", PET_TYPE)
			.param("birthDate", "2018-09-10"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@Test
	void testFailToProcessUpdateFormIfMandatoryFieldsAreMissing() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", OWNER_ID, PET_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasNoErrors("owner"))
			.andExpect(model().attributeHasErrors("pet"))
			.andExpect(model().attributeHasFieldErrors("pet", "name","type","birthDate"))
			.andExpect(model().attributeHasFieldErrorCode("pet", "type", "required"))
			.andExpect(model().attributeHasFieldErrorCode("pet", "name", "required"))
			.andExpect(model().attributeHasFieldErrorCode("pet", "birthDate", "required"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));
	}


}

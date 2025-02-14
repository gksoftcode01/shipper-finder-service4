package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.AppUserAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.AppUser;
import ai.yarmook.shipperfinder.domain.Country;
import ai.yarmook.shipperfinder.domain.Languages;
import ai.yarmook.shipperfinder.domain.enumeration.GenderType;
import ai.yarmook.shipperfinder.repository.AppUserRepository;
import ai.yarmook.shipperfinder.repository.search.AppUserSearchRepository;
import ai.yarmook.shipperfinder.service.AppUserService;
import ai.yarmook.shipperfinder.service.dto.AppUserDTO;
import ai.yarmook.shipperfinder.service.mapper.AppUserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AppUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AppUserResourceIT {

    private static final Instant DEFAULT_BIRTH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final GenderType DEFAULT_GENDER = GenderType.MALE;
    private static final GenderType UPDATED_GENDER = GenderType.FEMALE;

    private static final Instant DEFAULT_REGISTER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REGISTER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_VERIFIED = false;
    private static final Boolean UPDATED_IS_VERIFIED = true;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final UUID DEFAULT_ENC_ID = UUID.randomUUID();
    private static final UUID UPDATED_ENC_ID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/app-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/app-users/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppUserRepository appUserRepository;

    @Mock
    private AppUserRepository appUserRepositoryMock;

    @Autowired
    private AppUserMapper appUserMapper;

    @Mock
    private AppUserService appUserServiceMock;

    @Autowired
    private AppUserSearchRepository appUserSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppUserMockMvc;

    private AppUser appUser;

    private AppUser insertedAppUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUser createEntity() {
        return new AppUser()
            .birthDate(DEFAULT_BIRTH_DATE)
            .gender(DEFAULT_GENDER)
            .registerDate(DEFAULT_REGISTER_DATE)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .mobileNumber(DEFAULT_MOBILE_NUMBER)
            .fullName(DEFAULT_FULL_NAME)
            .isVerified(DEFAULT_IS_VERIFIED)
            .userId(DEFAULT_USER_ID)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .encId(DEFAULT_ENC_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUser createUpdatedEntity() {
        return new AppUser()
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .registerDate(UPDATED_REGISTER_DATE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .fullName(UPDATED_FULL_NAME)
            .isVerified(UPDATED_IS_VERIFIED)
            .userId(UPDATED_USER_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .encId(UPDATED_ENC_ID);
    }

    @BeforeEach
    public void initTest() {
        appUser = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAppUser != null) {
            appUserRepository.delete(insertedAppUser);
            appUserSearchRepository.delete(insertedAppUser);
            insertedAppUser = null;
        }
    }

    @Test
    @Transactional
    void createAppUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserSearchRepository.findAll());
        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);
        var returnedAppUserDTO = om.readValue(
            restAppUserMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUserDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AppUserDTO.class
        );

        // Validate the AppUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAppUser = appUserMapper.toEntity(returnedAppUserDTO);
        assertAppUserUpdatableFieldsEquals(returnedAppUser, getPersistedAppUser(returnedAppUser));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAppUser = returnedAppUser;
    }

    @Test
    @Transactional
    void createAppUserWithExistingId() throws Exception {
        // Create the AppUser with an existing ID
        appUser.setId(1L);
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAppUsers() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].registerDate").value(hasItem(DEFAULT_REGISTER_DATE.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].isVerified").value(hasItem(DEFAULT_IS_VERIFIED)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].encId").value(hasItem(DEFAULT_ENC_ID.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAppUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(appUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAppUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(appUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAppUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(appUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAppUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(appUserRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAppUser() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get the appUser
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL_ID, appUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appUser.getId().intValue()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.registerDate").value(DEFAULT_REGISTER_DATE.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.isVerified").value(DEFAULT_IS_VERIFIED))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.encId").value(DEFAULT_ENC_ID.toString()));
    }

    @Test
    @Transactional
    void getAppUsersByIdFiltering() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        Long id = appUser.getId();

        defaultAppUserFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAppUserFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAppUserFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppUsersByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where birthDate equals to
        defaultAppUserFiltering("birthDate.equals=" + DEFAULT_BIRTH_DATE, "birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllAppUsersByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where birthDate in
        defaultAppUserFiltering("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE, "birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllAppUsersByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where birthDate is not null
        defaultAppUserFiltering("birthDate.specified=true", "birthDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where gender equals to
        defaultAppUserFiltering("gender.equals=" + DEFAULT_GENDER, "gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllAppUsersByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where gender in
        defaultAppUserFiltering("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER, "gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllAppUsersByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where gender is not null
        defaultAppUserFiltering("gender.specified=true", "gender.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByRegisterDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where registerDate equals to
        defaultAppUserFiltering("registerDate.equals=" + DEFAULT_REGISTER_DATE, "registerDate.equals=" + UPDATED_REGISTER_DATE);
    }

    @Test
    @Transactional
    void getAllAppUsersByRegisterDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where registerDate in
        defaultAppUserFiltering(
            "registerDate.in=" + DEFAULT_REGISTER_DATE + "," + UPDATED_REGISTER_DATE,
            "registerDate.in=" + UPDATED_REGISTER_DATE
        );
    }

    @Test
    @Transactional
    void getAllAppUsersByRegisterDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where registerDate is not null
        defaultAppUserFiltering("registerDate.specified=true", "registerDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where phoneNumber equals to
        defaultAppUserFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppUsersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where phoneNumber in
        defaultAppUserFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllAppUsersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where phoneNumber is not null
        defaultAppUserFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where phoneNumber contains
        defaultAppUserFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppUsersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where phoneNumber does not contain
        defaultAppUserFiltering("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER, "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppUsersByMobileNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where mobileNumber equals to
        defaultAppUserFiltering("mobileNumber.equals=" + DEFAULT_MOBILE_NUMBER, "mobileNumber.equals=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppUsersByMobileNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where mobileNumber in
        defaultAppUserFiltering(
            "mobileNumber.in=" + DEFAULT_MOBILE_NUMBER + "," + UPDATED_MOBILE_NUMBER,
            "mobileNumber.in=" + UPDATED_MOBILE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllAppUsersByMobileNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where mobileNumber is not null
        defaultAppUserFiltering("mobileNumber.specified=true", "mobileNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByMobileNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where mobileNumber contains
        defaultAppUserFiltering("mobileNumber.contains=" + DEFAULT_MOBILE_NUMBER, "mobileNumber.contains=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppUsersByMobileNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where mobileNumber does not contain
        defaultAppUserFiltering(
            "mobileNumber.doesNotContain=" + UPDATED_MOBILE_NUMBER,
            "mobileNumber.doesNotContain=" + DEFAULT_MOBILE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllAppUsersByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where fullName equals to
        defaultAppUserFiltering("fullName.equals=" + DEFAULT_FULL_NAME, "fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where fullName in
        defaultAppUserFiltering("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME, "fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where fullName is not null
        defaultAppUserFiltering("fullName.specified=true", "fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByFullNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where fullName contains
        defaultAppUserFiltering("fullName.contains=" + DEFAULT_FULL_NAME, "fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where fullName does not contain
        defaultAppUserFiltering("fullName.doesNotContain=" + UPDATED_FULL_NAME, "fullName.doesNotContain=" + DEFAULT_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByIsVerifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where isVerified equals to
        defaultAppUserFiltering("isVerified.equals=" + DEFAULT_IS_VERIFIED, "isVerified.equals=" + UPDATED_IS_VERIFIED);
    }

    @Test
    @Transactional
    void getAllAppUsersByIsVerifiedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where isVerified in
        defaultAppUserFiltering("isVerified.in=" + DEFAULT_IS_VERIFIED + "," + UPDATED_IS_VERIFIED, "isVerified.in=" + UPDATED_IS_VERIFIED);
    }

    @Test
    @Transactional
    void getAllAppUsersByIsVerifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where isVerified is not null
        defaultAppUserFiltering("isVerified.specified=true", "isVerified.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where userId equals to
        defaultAppUserFiltering("userId.equals=" + DEFAULT_USER_ID, "userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllAppUsersByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where userId in
        defaultAppUserFiltering("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID, "userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllAppUsersByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where userId is not null
        defaultAppUserFiltering("userId.specified=true", "userId.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where userId is greater than or equal to
        defaultAppUserFiltering("userId.greaterThanOrEqual=" + DEFAULT_USER_ID, "userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllAppUsersByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where userId is less than or equal to
        defaultAppUserFiltering("userId.lessThanOrEqual=" + DEFAULT_USER_ID, "userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllAppUsersByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where userId is less than
        defaultAppUserFiltering("userId.lessThan=" + UPDATED_USER_ID, "userId.lessThan=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllAppUsersByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where userId is greater than
        defaultAppUserFiltering("userId.greaterThan=" + SMALLER_USER_ID, "userId.greaterThan=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllAppUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where firstName equals to
        defaultAppUserFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where firstName in
        defaultAppUserFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where firstName is not null
        defaultAppUserFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where firstName contains
        defaultAppUserFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where firstName does not contain
        defaultAppUserFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastName equals to
        defaultAppUserFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastName in
        defaultAppUserFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastName is not null
        defaultAppUserFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastName contains
        defaultAppUserFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastName does not contain
        defaultAppUserFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByEncIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where encId equals to
        defaultAppUserFiltering("encId.equals=" + DEFAULT_ENC_ID, "encId.equals=" + UPDATED_ENC_ID);
    }

    @Test
    @Transactional
    void getAllAppUsersByEncIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where encId in
        defaultAppUserFiltering("encId.in=" + DEFAULT_ENC_ID + "," + UPDATED_ENC_ID, "encId.in=" + UPDATED_ENC_ID);
    }

    @Test
    @Transactional
    void getAllAppUsersByEncIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where encId is not null
        defaultAppUserFiltering("encId.specified=true", "encId.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByPreferdLanguageIsEqualToSomething() throws Exception {
        Languages preferdLanguage;
        if (TestUtil.findAll(em, Languages.class).isEmpty()) {
            appUserRepository.saveAndFlush(appUser);
            preferdLanguage = LanguagesResourceIT.createEntity();
        } else {
            preferdLanguage = TestUtil.findAll(em, Languages.class).get(0);
        }
        em.persist(preferdLanguage);
        em.flush();
        appUser.setPreferdLanguage(preferdLanguage);
        appUserRepository.saveAndFlush(appUser);
        Long preferdLanguageId = preferdLanguage.getId();
        // Get all the appUserList where preferdLanguage equals to preferdLanguageId
        defaultAppUserShouldBeFound("preferdLanguageId.equals=" + preferdLanguageId);

        // Get all the appUserList where preferdLanguage equals to (preferdLanguageId + 1)
        defaultAppUserShouldNotBeFound("preferdLanguageId.equals=" + (preferdLanguageId + 1));
    }

    @Test
    @Transactional
    void getAllAppUsersByLocationIsEqualToSomething() throws Exception {
        Country location;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            appUserRepository.saveAndFlush(appUser);
            location = CountryResourceIT.createEntity();
        } else {
            location = TestUtil.findAll(em, Country.class).get(0);
        }
        em.persist(location);
        em.flush();
        appUser.setLocation(location);
        appUserRepository.saveAndFlush(appUser);
        Long locationId = location.getId();
        // Get all the appUserList where location equals to locationId
        defaultAppUserShouldBeFound("locationId.equals=" + locationId);

        // Get all the appUserList where location equals to (locationId + 1)
        defaultAppUserShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    private void defaultAppUserFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAppUserShouldBeFound(shouldBeFound);
        defaultAppUserShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppUserShouldBeFound(String filter) throws Exception {
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].registerDate").value(hasItem(DEFAULT_REGISTER_DATE.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].isVerified").value(hasItem(DEFAULT_IS_VERIFIED)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].encId").value(hasItem(DEFAULT_ENC_ID.toString())));

        // Check, that the count call also returns 1
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppUserShouldNotBeFound(String filter) throws Exception {
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppUser() throws Exception {
        // Get the appUser
        restAppUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppUser() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUserSearchRepository.save(appUser);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserSearchRepository.findAll());

        // Update the appUser
        AppUser updatedAppUser = appUserRepository.findById(appUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAppUser are not directly saved in db
        em.detach(updatedAppUser);
        updatedAppUser
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .registerDate(UPDATED_REGISTER_DATE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .fullName(UPDATED_FULL_NAME)
            .isVerified(UPDATED_IS_VERIFIED)
            .userId(UPDATED_USER_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .encId(UPDATED_ENC_ID);
        AppUserDTO appUserDTO = appUserMapper.toDto(updatedAppUser);

        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appUserDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppUserToMatchAllProperties(updatedAppUser);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AppUser> appUserSearchList = Streamable.of(appUserSearchRepository.findAll()).toList();
                AppUser testAppUserSearch = appUserSearchList.get(searchDatabaseSizeAfter - 1);

                assertAppUserAllPropertiesEquals(testAppUserSearch, updatedAppUser);
            });
    }

    @Test
    @Transactional
    void putNonExistingAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserSearchRepository.findAll());
        appUser.setId(longCount.incrementAndGet());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appUserDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserSearchRepository.findAll());
        appUser.setId(longCount.incrementAndGet());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserSearchRepository.findAll());
        appUser.setId(longCount.incrementAndGet());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAppUserWithPatch() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appUser using partial update
        AppUser partialUpdatedAppUser = new AppUser();
        partialUpdatedAppUser.setId(appUser.getId());

        partialUpdatedAppUser
            .gender(UPDATED_GENDER)
            .registerDate(UPDATED_REGISTER_DATE)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .isVerified(UPDATED_IS_VERIFIED)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .encId(UPDATED_ENC_ID);

        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppUser))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppUserUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAppUser, appUser), getPersistedAppUser(appUser));
    }

    @Test
    @Transactional
    void fullUpdateAppUserWithPatch() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appUser using partial update
        AppUser partialUpdatedAppUser = new AppUser();
        partialUpdatedAppUser.setId(appUser.getId());

        partialUpdatedAppUser
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .registerDate(UPDATED_REGISTER_DATE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .fullName(UPDATED_FULL_NAME)
            .isVerified(UPDATED_IS_VERIFIED)
            .userId(UPDATED_USER_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .encId(UPDATED_ENC_ID);

        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppUser))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppUserUpdatableFieldsEquals(partialUpdatedAppUser, getPersistedAppUser(partialUpdatedAppUser));
    }

    @Test
    @Transactional
    void patchNonExistingAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserSearchRepository.findAll());
        appUser.setId(longCount.incrementAndGet());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserSearchRepository.findAll());
        appUser.setId(longCount.incrementAndGet());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserSearchRepository.findAll());
        appUser.setId(longCount.incrementAndGet());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(appUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAppUser() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);
        appUserRepository.save(appUser);
        appUserSearchRepository.save(appUser);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the appUser
        restAppUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, appUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAppUser() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);
        appUserSearchRepository.save(appUser);

        // Search the appUser
        restAppUserMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + appUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].registerDate").value(hasItem(DEFAULT_REGISTER_DATE.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].isVerified").value(hasItem(DEFAULT_IS_VERIFIED)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].encId").value(hasItem(DEFAULT_ENC_ID.toString())));
    }

    protected long getRepositoryCount() {
        return appUserRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected AppUser getPersistedAppUser(AppUser appUser) {
        return appUserRepository.findById(appUser.getId()).orElseThrow();
    }

    protected void assertPersistedAppUserToMatchAllProperties(AppUser expectedAppUser) {
        assertAppUserAllPropertiesEquals(expectedAppUser, getPersistedAppUser(expectedAppUser));
    }

    protected void assertPersistedAppUserToMatchUpdatableProperties(AppUser expectedAppUser) {
        assertAppUserAllUpdatablePropertiesEquals(expectedAppUser, getPersistedAppUser(expectedAppUser));
    }
}

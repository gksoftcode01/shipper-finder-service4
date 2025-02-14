package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.SubscribeTypeAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.SubscribeType;
import ai.yarmook.shipperfinder.domain.enumeration.SubscribeTypeEnum;
import ai.yarmook.shipperfinder.repository.SubscribeTypeRepository;
import ai.yarmook.shipperfinder.repository.search.SubscribeTypeSearchRepository;
import ai.yarmook.shipperfinder.service.dto.SubscribeTypeDTO;
import ai.yarmook.shipperfinder.service.mapper.SubscribeTypeMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SubscribeTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubscribeTypeResourceIT {

    private static final SubscribeTypeEnum DEFAULT_TYPE = SubscribeTypeEnum.NORMAL;
    private static final SubscribeTypeEnum UPDATED_TYPE = SubscribeTypeEnum.PREMIUM;

    private static final String DEFAULT_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_EN = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_FR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_DE = "AAAAAAAAAA";
    private static final String UPDATED_NAME_DE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_URDU = "AAAAAAAAAA";
    private static final String UPDATED_NAME_URDU = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS_EN = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS_EN = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS_AR = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS_AR = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS_FR = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS_FR = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS_DE = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS_DE = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS_URDU = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS_URDU = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/subscribe-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/subscribe-types/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubscribeTypeRepository subscribeTypeRepository;

    @Autowired
    private SubscribeTypeMapper subscribeTypeMapper;

    @Autowired
    private SubscribeTypeSearchRepository subscribeTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscribeTypeMockMvc;

    private SubscribeType subscribeType;

    private SubscribeType insertedSubscribeType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscribeType createEntity() {
        return new SubscribeType()
            .type(DEFAULT_TYPE)
            .nameEn(DEFAULT_NAME_EN)
            .nameAr(DEFAULT_NAME_AR)
            .nameFr(DEFAULT_NAME_FR)
            .nameDe(DEFAULT_NAME_DE)
            .nameUrdu(DEFAULT_NAME_URDU)
            .details(DEFAULT_DETAILS)
            .detailsEn(DEFAULT_DETAILS_EN)
            .detailsAr(DEFAULT_DETAILS_AR)
            .detailsFr(DEFAULT_DETAILS_FR)
            .detailsDe(DEFAULT_DETAILS_DE)
            .detailsUrdu(DEFAULT_DETAILS_URDU);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscribeType createUpdatedEntity() {
        return new SubscribeType()
            .type(UPDATED_TYPE)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .nameFr(UPDATED_NAME_FR)
            .nameDe(UPDATED_NAME_DE)
            .nameUrdu(UPDATED_NAME_URDU)
            .details(UPDATED_DETAILS)
            .detailsEn(UPDATED_DETAILS_EN)
            .detailsAr(UPDATED_DETAILS_AR)
            .detailsFr(UPDATED_DETAILS_FR)
            .detailsDe(UPDATED_DETAILS_DE)
            .detailsUrdu(UPDATED_DETAILS_URDU);
    }

    @BeforeEach
    public void initTest() {
        subscribeType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSubscribeType != null) {
            subscribeTypeRepository.delete(insertedSubscribeType);
            subscribeTypeSearchRepository.delete(insertedSubscribeType);
            insertedSubscribeType = null;
        }
    }

    @Test
    @Transactional
    void createSubscribeType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());
        // Create the SubscribeType
        SubscribeTypeDTO subscribeTypeDTO = subscribeTypeMapper.toDto(subscribeType);
        var returnedSubscribeTypeDTO = om.readValue(
            restSubscribeTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscribeTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SubscribeTypeDTO.class
        );

        // Validate the SubscribeType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSubscribeType = subscribeTypeMapper.toEntity(returnedSubscribeTypeDTO);
        assertSubscribeTypeUpdatableFieldsEquals(returnedSubscribeType, getPersistedSubscribeType(returnedSubscribeType));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedSubscribeType = returnedSubscribeType;
    }

    @Test
    @Transactional
    void createSubscribeTypeWithExistingId() throws Exception {
        // Create the SubscribeType with an existing ID
        subscribeType.setId(1L);
        SubscribeTypeDTO subscribeTypeDTO = subscribeTypeMapper.toDto(subscribeType);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscribeTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscribeTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscribeType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSubscribeTypes() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList
        restSubscribeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscribeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameFr").value(hasItem(DEFAULT_NAME_FR)))
            .andExpect(jsonPath("$.[*].nameDe").value(hasItem(DEFAULT_NAME_DE)))
            .andExpect(jsonPath("$.[*].nameUrdu").value(hasItem(DEFAULT_NAME_URDU)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].detailsEn").value(hasItem(DEFAULT_DETAILS_EN)))
            .andExpect(jsonPath("$.[*].detailsAr").value(hasItem(DEFAULT_DETAILS_AR)))
            .andExpect(jsonPath("$.[*].detailsFr").value(hasItem(DEFAULT_DETAILS_FR)))
            .andExpect(jsonPath("$.[*].detailsDe").value(hasItem(DEFAULT_DETAILS_DE)))
            .andExpect(jsonPath("$.[*].detailsUrdu").value(hasItem(DEFAULT_DETAILS_URDU)));
    }

    @Test
    @Transactional
    void getSubscribeType() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get the subscribeType
        restSubscribeTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, subscribeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscribeType.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.nameFr").value(DEFAULT_NAME_FR))
            .andExpect(jsonPath("$.nameDe").value(DEFAULT_NAME_DE))
            .andExpect(jsonPath("$.nameUrdu").value(DEFAULT_NAME_URDU))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS))
            .andExpect(jsonPath("$.detailsEn").value(DEFAULT_DETAILS_EN))
            .andExpect(jsonPath("$.detailsAr").value(DEFAULT_DETAILS_AR))
            .andExpect(jsonPath("$.detailsFr").value(DEFAULT_DETAILS_FR))
            .andExpect(jsonPath("$.detailsDe").value(DEFAULT_DETAILS_DE))
            .andExpect(jsonPath("$.detailsUrdu").value(DEFAULT_DETAILS_URDU));
    }

    @Test
    @Transactional
    void getSubscribeTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        Long id = subscribeType.getId();

        defaultSubscribeTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSubscribeTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSubscribeTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where type equals to
        defaultSubscribeTypeFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where type in
        defaultSubscribeTypeFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where type is not null
        defaultSubscribeTypeFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameEnIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameEn equals to
        defaultSubscribeTypeFiltering("nameEn.equals=" + DEFAULT_NAME_EN, "nameEn.equals=" + UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameEnIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameEn in
        defaultSubscribeTypeFiltering("nameEn.in=" + DEFAULT_NAME_EN + "," + UPDATED_NAME_EN, "nameEn.in=" + UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameEnIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameEn is not null
        defaultSubscribeTypeFiltering("nameEn.specified=true", "nameEn.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameEnContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameEn contains
        defaultSubscribeTypeFiltering("nameEn.contains=" + DEFAULT_NAME_EN, "nameEn.contains=" + UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameEnNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameEn does not contain
        defaultSubscribeTypeFiltering("nameEn.doesNotContain=" + UPDATED_NAME_EN, "nameEn.doesNotContain=" + DEFAULT_NAME_EN);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameArIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameAr equals to
        defaultSubscribeTypeFiltering("nameAr.equals=" + DEFAULT_NAME_AR, "nameAr.equals=" + UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameArIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameAr in
        defaultSubscribeTypeFiltering("nameAr.in=" + DEFAULT_NAME_AR + "," + UPDATED_NAME_AR, "nameAr.in=" + UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameArIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameAr is not null
        defaultSubscribeTypeFiltering("nameAr.specified=true", "nameAr.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameArContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameAr contains
        defaultSubscribeTypeFiltering("nameAr.contains=" + DEFAULT_NAME_AR, "nameAr.contains=" + UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameArNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameAr does not contain
        defaultSubscribeTypeFiltering("nameAr.doesNotContain=" + UPDATED_NAME_AR, "nameAr.doesNotContain=" + DEFAULT_NAME_AR);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameFrIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameFr equals to
        defaultSubscribeTypeFiltering("nameFr.equals=" + DEFAULT_NAME_FR, "nameFr.equals=" + UPDATED_NAME_FR);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameFrIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameFr in
        defaultSubscribeTypeFiltering("nameFr.in=" + DEFAULT_NAME_FR + "," + UPDATED_NAME_FR, "nameFr.in=" + UPDATED_NAME_FR);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameFr is not null
        defaultSubscribeTypeFiltering("nameFr.specified=true", "nameFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameFrContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameFr contains
        defaultSubscribeTypeFiltering("nameFr.contains=" + DEFAULT_NAME_FR, "nameFr.contains=" + UPDATED_NAME_FR);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameFrNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameFr does not contain
        defaultSubscribeTypeFiltering("nameFr.doesNotContain=" + UPDATED_NAME_FR, "nameFr.doesNotContain=" + DEFAULT_NAME_FR);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameDeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameDe equals to
        defaultSubscribeTypeFiltering("nameDe.equals=" + DEFAULT_NAME_DE, "nameDe.equals=" + UPDATED_NAME_DE);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameDeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameDe in
        defaultSubscribeTypeFiltering("nameDe.in=" + DEFAULT_NAME_DE + "," + UPDATED_NAME_DE, "nameDe.in=" + UPDATED_NAME_DE);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameDeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameDe is not null
        defaultSubscribeTypeFiltering("nameDe.specified=true", "nameDe.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameDeContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameDe contains
        defaultSubscribeTypeFiltering("nameDe.contains=" + DEFAULT_NAME_DE, "nameDe.contains=" + UPDATED_NAME_DE);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameDeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameDe does not contain
        defaultSubscribeTypeFiltering("nameDe.doesNotContain=" + UPDATED_NAME_DE, "nameDe.doesNotContain=" + DEFAULT_NAME_DE);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameUrduIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameUrdu equals to
        defaultSubscribeTypeFiltering("nameUrdu.equals=" + DEFAULT_NAME_URDU, "nameUrdu.equals=" + UPDATED_NAME_URDU);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameUrduIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameUrdu in
        defaultSubscribeTypeFiltering("nameUrdu.in=" + DEFAULT_NAME_URDU + "," + UPDATED_NAME_URDU, "nameUrdu.in=" + UPDATED_NAME_URDU);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameUrduIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameUrdu is not null
        defaultSubscribeTypeFiltering("nameUrdu.specified=true", "nameUrdu.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameUrduContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameUrdu contains
        defaultSubscribeTypeFiltering("nameUrdu.contains=" + DEFAULT_NAME_URDU, "nameUrdu.contains=" + UPDATED_NAME_URDU);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByNameUrduNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where nameUrdu does not contain
        defaultSubscribeTypeFiltering("nameUrdu.doesNotContain=" + UPDATED_NAME_URDU, "nameUrdu.doesNotContain=" + DEFAULT_NAME_URDU);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsEnIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsEn equals to
        defaultSubscribeTypeFiltering("detailsEn.equals=" + DEFAULT_DETAILS_EN, "detailsEn.equals=" + UPDATED_DETAILS_EN);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsEnIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsEn in
        defaultSubscribeTypeFiltering(
            "detailsEn.in=" + DEFAULT_DETAILS_EN + "," + UPDATED_DETAILS_EN,
            "detailsEn.in=" + UPDATED_DETAILS_EN
        );
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsEnIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsEn is not null
        defaultSubscribeTypeFiltering("detailsEn.specified=true", "detailsEn.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsEnContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsEn contains
        defaultSubscribeTypeFiltering("detailsEn.contains=" + DEFAULT_DETAILS_EN, "detailsEn.contains=" + UPDATED_DETAILS_EN);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsEnNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsEn does not contain
        defaultSubscribeTypeFiltering("detailsEn.doesNotContain=" + UPDATED_DETAILS_EN, "detailsEn.doesNotContain=" + DEFAULT_DETAILS_EN);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsArIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsAr equals to
        defaultSubscribeTypeFiltering("detailsAr.equals=" + DEFAULT_DETAILS_AR, "detailsAr.equals=" + UPDATED_DETAILS_AR);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsArIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsAr in
        defaultSubscribeTypeFiltering(
            "detailsAr.in=" + DEFAULT_DETAILS_AR + "," + UPDATED_DETAILS_AR,
            "detailsAr.in=" + UPDATED_DETAILS_AR
        );
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsArIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsAr is not null
        defaultSubscribeTypeFiltering("detailsAr.specified=true", "detailsAr.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsArContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsAr contains
        defaultSubscribeTypeFiltering("detailsAr.contains=" + DEFAULT_DETAILS_AR, "detailsAr.contains=" + UPDATED_DETAILS_AR);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsArNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsAr does not contain
        defaultSubscribeTypeFiltering("detailsAr.doesNotContain=" + UPDATED_DETAILS_AR, "detailsAr.doesNotContain=" + DEFAULT_DETAILS_AR);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsFrIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsFr equals to
        defaultSubscribeTypeFiltering("detailsFr.equals=" + DEFAULT_DETAILS_FR, "detailsFr.equals=" + UPDATED_DETAILS_FR);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsFrIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsFr in
        defaultSubscribeTypeFiltering(
            "detailsFr.in=" + DEFAULT_DETAILS_FR + "," + UPDATED_DETAILS_FR,
            "detailsFr.in=" + UPDATED_DETAILS_FR
        );
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsFr is not null
        defaultSubscribeTypeFiltering("detailsFr.specified=true", "detailsFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsFrContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsFr contains
        defaultSubscribeTypeFiltering("detailsFr.contains=" + DEFAULT_DETAILS_FR, "detailsFr.contains=" + UPDATED_DETAILS_FR);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsFrNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsFr does not contain
        defaultSubscribeTypeFiltering("detailsFr.doesNotContain=" + UPDATED_DETAILS_FR, "detailsFr.doesNotContain=" + DEFAULT_DETAILS_FR);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsDeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsDe equals to
        defaultSubscribeTypeFiltering("detailsDe.equals=" + DEFAULT_DETAILS_DE, "detailsDe.equals=" + UPDATED_DETAILS_DE);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsDeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsDe in
        defaultSubscribeTypeFiltering(
            "detailsDe.in=" + DEFAULT_DETAILS_DE + "," + UPDATED_DETAILS_DE,
            "detailsDe.in=" + UPDATED_DETAILS_DE
        );
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsDeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsDe is not null
        defaultSubscribeTypeFiltering("detailsDe.specified=true", "detailsDe.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsDeContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsDe contains
        defaultSubscribeTypeFiltering("detailsDe.contains=" + DEFAULT_DETAILS_DE, "detailsDe.contains=" + UPDATED_DETAILS_DE);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsDeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsDe does not contain
        defaultSubscribeTypeFiltering("detailsDe.doesNotContain=" + UPDATED_DETAILS_DE, "detailsDe.doesNotContain=" + DEFAULT_DETAILS_DE);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsUrduIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsUrdu equals to
        defaultSubscribeTypeFiltering("detailsUrdu.equals=" + DEFAULT_DETAILS_URDU, "detailsUrdu.equals=" + UPDATED_DETAILS_URDU);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsUrduIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsUrdu in
        defaultSubscribeTypeFiltering(
            "detailsUrdu.in=" + DEFAULT_DETAILS_URDU + "," + UPDATED_DETAILS_URDU,
            "detailsUrdu.in=" + UPDATED_DETAILS_URDU
        );
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsUrduIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsUrdu is not null
        defaultSubscribeTypeFiltering("detailsUrdu.specified=true", "detailsUrdu.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsUrduContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsUrdu contains
        defaultSubscribeTypeFiltering("detailsUrdu.contains=" + DEFAULT_DETAILS_URDU, "detailsUrdu.contains=" + UPDATED_DETAILS_URDU);
    }

    @Test
    @Transactional
    void getAllSubscribeTypesByDetailsUrduNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        // Get all the subscribeTypeList where detailsUrdu does not contain
        defaultSubscribeTypeFiltering(
            "detailsUrdu.doesNotContain=" + UPDATED_DETAILS_URDU,
            "detailsUrdu.doesNotContain=" + DEFAULT_DETAILS_URDU
        );
    }

    private void defaultSubscribeTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSubscribeTypeShouldBeFound(shouldBeFound);
        defaultSubscribeTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubscribeTypeShouldBeFound(String filter) throws Exception {
        restSubscribeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscribeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameFr").value(hasItem(DEFAULT_NAME_FR)))
            .andExpect(jsonPath("$.[*].nameDe").value(hasItem(DEFAULT_NAME_DE)))
            .andExpect(jsonPath("$.[*].nameUrdu").value(hasItem(DEFAULT_NAME_URDU)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].detailsEn").value(hasItem(DEFAULT_DETAILS_EN)))
            .andExpect(jsonPath("$.[*].detailsAr").value(hasItem(DEFAULT_DETAILS_AR)))
            .andExpect(jsonPath("$.[*].detailsFr").value(hasItem(DEFAULT_DETAILS_FR)))
            .andExpect(jsonPath("$.[*].detailsDe").value(hasItem(DEFAULT_DETAILS_DE)))
            .andExpect(jsonPath("$.[*].detailsUrdu").value(hasItem(DEFAULT_DETAILS_URDU)));

        // Check, that the count call also returns 1
        restSubscribeTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubscribeTypeShouldNotBeFound(String filter) throws Exception {
        restSubscribeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubscribeTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSubscribeType() throws Exception {
        // Get the subscribeType
        restSubscribeTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubscribeType() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscribeTypeSearchRepository.save(subscribeType);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());

        // Update the subscribeType
        SubscribeType updatedSubscribeType = subscribeTypeRepository.findById(subscribeType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubscribeType are not directly saved in db
        em.detach(updatedSubscribeType);
        updatedSubscribeType
            .type(UPDATED_TYPE)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .nameFr(UPDATED_NAME_FR)
            .nameDe(UPDATED_NAME_DE)
            .nameUrdu(UPDATED_NAME_URDU)
            .details(UPDATED_DETAILS)
            .detailsEn(UPDATED_DETAILS_EN)
            .detailsAr(UPDATED_DETAILS_AR)
            .detailsFr(UPDATED_DETAILS_FR)
            .detailsDe(UPDATED_DETAILS_DE)
            .detailsUrdu(UPDATED_DETAILS_URDU);
        SubscribeTypeDTO subscribeTypeDTO = subscribeTypeMapper.toDto(updatedSubscribeType);

        restSubscribeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscribeTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscribeTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubscribeType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubscribeTypeToMatchAllProperties(updatedSubscribeType);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SubscribeType> subscribeTypeSearchList = Streamable.of(subscribeTypeSearchRepository.findAll()).toList();
                SubscribeType testSubscribeTypeSearch = subscribeTypeSearchList.get(searchDatabaseSizeAfter - 1);

                assertSubscribeTypeAllPropertiesEquals(testSubscribeTypeSearch, updatedSubscribeType);
            });
    }

    @Test
    @Transactional
    void putNonExistingSubscribeType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());
        subscribeType.setId(longCount.incrementAndGet());

        // Create the SubscribeType
        SubscribeTypeDTO subscribeTypeDTO = subscribeTypeMapper.toDto(subscribeType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscribeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscribeTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscribeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscribeType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubscribeType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());
        subscribeType.setId(longCount.incrementAndGet());

        // Create the SubscribeType
        SubscribeTypeDTO subscribeTypeDTO = subscribeTypeMapper.toDto(subscribeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscribeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscribeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscribeType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubscribeType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());
        subscribeType.setId(longCount.incrementAndGet());

        // Create the SubscribeType
        SubscribeTypeDTO subscribeTypeDTO = subscribeTypeMapper.toDto(subscribeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscribeTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscribeTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscribeType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSubscribeTypeWithPatch() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscribeType using partial update
        SubscribeType partialUpdatedSubscribeType = new SubscribeType();
        partialUpdatedSubscribeType.setId(subscribeType.getId());

        partialUpdatedSubscribeType.detailsEn(UPDATED_DETAILS_EN).detailsUrdu(UPDATED_DETAILS_URDU);

        restSubscribeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscribeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscribeType))
            )
            .andExpect(status().isOk());

        // Validate the SubscribeType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscribeTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSubscribeType, subscribeType),
            getPersistedSubscribeType(subscribeType)
        );
    }

    @Test
    @Transactional
    void fullUpdateSubscribeTypeWithPatch() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscribeType using partial update
        SubscribeType partialUpdatedSubscribeType = new SubscribeType();
        partialUpdatedSubscribeType.setId(subscribeType.getId());

        partialUpdatedSubscribeType
            .type(UPDATED_TYPE)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .nameFr(UPDATED_NAME_FR)
            .nameDe(UPDATED_NAME_DE)
            .nameUrdu(UPDATED_NAME_URDU)
            .details(UPDATED_DETAILS)
            .detailsEn(UPDATED_DETAILS_EN)
            .detailsAr(UPDATED_DETAILS_AR)
            .detailsFr(UPDATED_DETAILS_FR)
            .detailsDe(UPDATED_DETAILS_DE)
            .detailsUrdu(UPDATED_DETAILS_URDU);

        restSubscribeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscribeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscribeType))
            )
            .andExpect(status().isOk());

        // Validate the SubscribeType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscribeTypeUpdatableFieldsEquals(partialUpdatedSubscribeType, getPersistedSubscribeType(partialUpdatedSubscribeType));
    }

    @Test
    @Transactional
    void patchNonExistingSubscribeType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());
        subscribeType.setId(longCount.incrementAndGet());

        // Create the SubscribeType
        SubscribeTypeDTO subscribeTypeDTO = subscribeTypeMapper.toDto(subscribeType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscribeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subscribeTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscribeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscribeType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubscribeType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());
        subscribeType.setId(longCount.incrementAndGet());

        // Create the SubscribeType
        SubscribeTypeDTO subscribeTypeDTO = subscribeTypeMapper.toDto(subscribeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscribeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscribeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscribeType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubscribeType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());
        subscribeType.setId(longCount.incrementAndGet());

        // Create the SubscribeType
        SubscribeTypeDTO subscribeTypeDTO = subscribeTypeMapper.toDto(subscribeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscribeTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(subscribeTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscribeType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSubscribeType() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);
        subscribeTypeRepository.save(subscribeType);
        subscribeTypeSearchRepository.save(subscribeType);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the subscribeType
        restSubscribeTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, subscribeType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscribeTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSubscribeType() throws Exception {
        // Initialize the database
        insertedSubscribeType = subscribeTypeRepository.saveAndFlush(subscribeType);
        subscribeTypeSearchRepository.save(subscribeType);

        // Search the subscribeType
        restSubscribeTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + subscribeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscribeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameFr").value(hasItem(DEFAULT_NAME_FR)))
            .andExpect(jsonPath("$.[*].nameDe").value(hasItem(DEFAULT_NAME_DE)))
            .andExpect(jsonPath("$.[*].nameUrdu").value(hasItem(DEFAULT_NAME_URDU)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())))
            .andExpect(jsonPath("$.[*].detailsEn").value(hasItem(DEFAULT_DETAILS_EN)))
            .andExpect(jsonPath("$.[*].detailsAr").value(hasItem(DEFAULT_DETAILS_AR)))
            .andExpect(jsonPath("$.[*].detailsFr").value(hasItem(DEFAULT_DETAILS_FR)))
            .andExpect(jsonPath("$.[*].detailsDe").value(hasItem(DEFAULT_DETAILS_DE)))
            .andExpect(jsonPath("$.[*].detailsUrdu").value(hasItem(DEFAULT_DETAILS_URDU)));
    }

    protected long getRepositoryCount() {
        return subscribeTypeRepository.count();
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

    protected SubscribeType getPersistedSubscribeType(SubscribeType subscribeType) {
        return subscribeTypeRepository.findById(subscribeType.getId()).orElseThrow();
    }

    protected void assertPersistedSubscribeTypeToMatchAllProperties(SubscribeType expectedSubscribeType) {
        assertSubscribeTypeAllPropertiesEquals(expectedSubscribeType, getPersistedSubscribeType(expectedSubscribeType));
    }

    protected void assertPersistedSubscribeTypeToMatchUpdatableProperties(SubscribeType expectedSubscribeType) {
        assertSubscribeTypeAllUpdatablePropertiesEquals(expectedSubscribeType, getPersistedSubscribeType(expectedSubscribeType));
    }
}

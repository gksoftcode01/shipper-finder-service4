package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.CountryAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.Country;
import ai.yarmook.shipperfinder.repository.CountryRepository;
import ai.yarmook.shipperfinder.repository.search.CountrySearchRepository;
import ai.yarmook.shipperfinder.service.dto.CountryDTO;
import ai.yarmook.shipperfinder.service.mapper.CountryMapper;
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
 * Integration tests for the {@link CountryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CountryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ISO_2 = "AAAAAAAAAA";
    private static final String UPDATED_ISO_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ISO_3 = "AAAAAAAAAA";
    private static final String UPDATED_ISO_3 = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERIC_CODE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERIC_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_SYMBOL = "BBBBBBBBBB";

    private static final String DEFAULT_EMOJI = "AAAAAAAAAA";
    private static final String UPDATED_EMOJI = "BBBBBBBBBB";

    private static final String DEFAULT_EMOJI_U = "AAAAAAAAAA";
    private static final String UPDATED_EMOJI_U = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/countries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/countries/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    private CountrySearchRepository countrySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountryMockMvc;

    private Country country;

    private Country insertedCountry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createEntity() {
        return new Country()
            .name(DEFAULT_NAME)
            .localName(DEFAULT_LOCAL_NAME)
            .iso2(DEFAULT_ISO_2)
            .iso3(DEFAULT_ISO_3)
            .numericCode(DEFAULT_NUMERIC_CODE)
            .phoneCode(DEFAULT_PHONE_CODE)
            .currency(DEFAULT_CURRENCY)
            .currencyName(DEFAULT_CURRENCY_NAME)
            .currencySymbol(DEFAULT_CURRENCY_SYMBOL)
            .emoji(DEFAULT_EMOJI)
            .emojiU(DEFAULT_EMOJI_U);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createUpdatedEntity() {
        return new Country()
            .name(UPDATED_NAME)
            .localName(UPDATED_LOCAL_NAME)
            .iso2(UPDATED_ISO_2)
            .iso3(UPDATED_ISO_3)
            .numericCode(UPDATED_NUMERIC_CODE)
            .phoneCode(UPDATED_PHONE_CODE)
            .currency(UPDATED_CURRENCY)
            .currencyName(UPDATED_CURRENCY_NAME)
            .currencySymbol(UPDATED_CURRENCY_SYMBOL)
            .emoji(UPDATED_EMOJI)
            .emojiU(UPDATED_EMOJI_U);
    }

    @BeforeEach
    public void initTest() {
        country = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCountry != null) {
            countryRepository.delete(insertedCountry);
            countrySearchRepository.delete(insertedCountry);
            insertedCountry = null;
        }
    }

    @Test
    @Transactional
    void createCountry() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll());
        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);
        var returnedCountryDTO = om.readValue(
            restCountryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CountryDTO.class
        );

        // Validate the Country in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCountry = countryMapper.toEntity(returnedCountryDTO);
        assertCountryUpdatableFieldsEquals(returnedCountry, getPersistedCountry(returnedCountry));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedCountry = returnedCountry;
    }

    @Test
    @Transactional
    void createCountryWithExistingId() throws Exception {
        // Create the Country with an existing ID
        country.setId(1L);
        CountryDTO countryDTO = countryMapper.toDto(country);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCountries() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].localName").value(hasItem(DEFAULT_LOCAL_NAME)))
            .andExpect(jsonPath("$.[*].iso2").value(hasItem(DEFAULT_ISO_2)))
            .andExpect(jsonPath("$.[*].iso3").value(hasItem(DEFAULT_ISO_3)))
            .andExpect(jsonPath("$.[*].numericCode").value(hasItem(DEFAULT_NUMERIC_CODE)))
            .andExpect(jsonPath("$.[*].phoneCode").value(hasItem(DEFAULT_PHONE_CODE)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].currencyName").value(hasItem(DEFAULT_CURRENCY_NAME)))
            .andExpect(jsonPath("$.[*].currencySymbol").value(hasItem(DEFAULT_CURRENCY_SYMBOL)))
            .andExpect(jsonPath("$.[*].emoji").value(hasItem(DEFAULT_EMOJI)))
            .andExpect(jsonPath("$.[*].emojiU").value(hasItem(DEFAULT_EMOJI_U)));
    }

    @Test
    @Transactional
    void getCountry() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get the country
        restCountryMockMvc
            .perform(get(ENTITY_API_URL_ID, country.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(country.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.localName").value(DEFAULT_LOCAL_NAME))
            .andExpect(jsonPath("$.iso2").value(DEFAULT_ISO_2))
            .andExpect(jsonPath("$.iso3").value(DEFAULT_ISO_3))
            .andExpect(jsonPath("$.numericCode").value(DEFAULT_NUMERIC_CODE))
            .andExpect(jsonPath("$.phoneCode").value(DEFAULT_PHONE_CODE))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.currencyName").value(DEFAULT_CURRENCY_NAME))
            .andExpect(jsonPath("$.currencySymbol").value(DEFAULT_CURRENCY_SYMBOL))
            .andExpect(jsonPath("$.emoji").value(DEFAULT_EMOJI))
            .andExpect(jsonPath("$.emojiU").value(DEFAULT_EMOJI_U));
    }

    @Test
    @Transactional
    void getCountriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        Long id = country.getId();

        defaultCountryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCountryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCountryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCountriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where name equals to
        defaultCountryFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where name in
        defaultCountryFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where name is not null
        defaultCountryFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where name contains
        defaultCountryFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where name does not contain
        defaultCountryFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByLocalNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where localName equals to
        defaultCountryFiltering("localName.equals=" + DEFAULT_LOCAL_NAME, "localName.equals=" + UPDATED_LOCAL_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByLocalNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where localName in
        defaultCountryFiltering("localName.in=" + DEFAULT_LOCAL_NAME + "," + UPDATED_LOCAL_NAME, "localName.in=" + UPDATED_LOCAL_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByLocalNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where localName is not null
        defaultCountryFiltering("localName.specified=true", "localName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByLocalNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where localName contains
        defaultCountryFiltering("localName.contains=" + DEFAULT_LOCAL_NAME, "localName.contains=" + UPDATED_LOCAL_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByLocalNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where localName does not contain
        defaultCountryFiltering("localName.doesNotContain=" + UPDATED_LOCAL_NAME, "localName.doesNotContain=" + DEFAULT_LOCAL_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByIso2IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where iso2 equals to
        defaultCountryFiltering("iso2.equals=" + DEFAULT_ISO_2, "iso2.equals=" + UPDATED_ISO_2);
    }

    @Test
    @Transactional
    void getAllCountriesByIso2IsInShouldWork() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where iso2 in
        defaultCountryFiltering("iso2.in=" + DEFAULT_ISO_2 + "," + UPDATED_ISO_2, "iso2.in=" + UPDATED_ISO_2);
    }

    @Test
    @Transactional
    void getAllCountriesByIso2IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where iso2 is not null
        defaultCountryFiltering("iso2.specified=true", "iso2.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByIso2ContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where iso2 contains
        defaultCountryFiltering("iso2.contains=" + DEFAULT_ISO_2, "iso2.contains=" + UPDATED_ISO_2);
    }

    @Test
    @Transactional
    void getAllCountriesByIso2NotContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where iso2 does not contain
        defaultCountryFiltering("iso2.doesNotContain=" + UPDATED_ISO_2, "iso2.doesNotContain=" + DEFAULT_ISO_2);
    }

    @Test
    @Transactional
    void getAllCountriesByIso3IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where iso3 equals to
        defaultCountryFiltering("iso3.equals=" + DEFAULT_ISO_3, "iso3.equals=" + UPDATED_ISO_3);
    }

    @Test
    @Transactional
    void getAllCountriesByIso3IsInShouldWork() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where iso3 in
        defaultCountryFiltering("iso3.in=" + DEFAULT_ISO_3 + "," + UPDATED_ISO_3, "iso3.in=" + UPDATED_ISO_3);
    }

    @Test
    @Transactional
    void getAllCountriesByIso3IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where iso3 is not null
        defaultCountryFiltering("iso3.specified=true", "iso3.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByIso3ContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where iso3 contains
        defaultCountryFiltering("iso3.contains=" + DEFAULT_ISO_3, "iso3.contains=" + UPDATED_ISO_3);
    }

    @Test
    @Transactional
    void getAllCountriesByIso3NotContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where iso3 does not contain
        defaultCountryFiltering("iso3.doesNotContain=" + UPDATED_ISO_3, "iso3.doesNotContain=" + DEFAULT_ISO_3);
    }

    @Test
    @Transactional
    void getAllCountriesByNumericCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where numericCode equals to
        defaultCountryFiltering("numericCode.equals=" + DEFAULT_NUMERIC_CODE, "numericCode.equals=" + UPDATED_NUMERIC_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByNumericCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where numericCode in
        defaultCountryFiltering(
            "numericCode.in=" + DEFAULT_NUMERIC_CODE + "," + UPDATED_NUMERIC_CODE,
            "numericCode.in=" + UPDATED_NUMERIC_CODE
        );
    }

    @Test
    @Transactional
    void getAllCountriesByNumericCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where numericCode is not null
        defaultCountryFiltering("numericCode.specified=true", "numericCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByNumericCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where numericCode contains
        defaultCountryFiltering("numericCode.contains=" + DEFAULT_NUMERIC_CODE, "numericCode.contains=" + UPDATED_NUMERIC_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByNumericCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where numericCode does not contain
        defaultCountryFiltering("numericCode.doesNotContain=" + UPDATED_NUMERIC_CODE, "numericCode.doesNotContain=" + DEFAULT_NUMERIC_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByPhoneCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where phoneCode equals to
        defaultCountryFiltering("phoneCode.equals=" + DEFAULT_PHONE_CODE, "phoneCode.equals=" + UPDATED_PHONE_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByPhoneCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where phoneCode in
        defaultCountryFiltering("phoneCode.in=" + DEFAULT_PHONE_CODE + "," + UPDATED_PHONE_CODE, "phoneCode.in=" + UPDATED_PHONE_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByPhoneCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where phoneCode is not null
        defaultCountryFiltering("phoneCode.specified=true", "phoneCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByPhoneCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where phoneCode contains
        defaultCountryFiltering("phoneCode.contains=" + DEFAULT_PHONE_CODE, "phoneCode.contains=" + UPDATED_PHONE_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByPhoneCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where phoneCode does not contain
        defaultCountryFiltering("phoneCode.doesNotContain=" + UPDATED_PHONE_CODE, "phoneCode.doesNotContain=" + DEFAULT_PHONE_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where currency equals to
        defaultCountryFiltering("currency.equals=" + DEFAULT_CURRENCY, "currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCountriesByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where currency in
        defaultCountryFiltering("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY, "currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCountriesByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where currency is not null
        defaultCountryFiltering("currency.specified=true", "currency.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where currency contains
        defaultCountryFiltering("currency.contains=" + DEFAULT_CURRENCY, "currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCountriesByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where currency does not contain
        defaultCountryFiltering("currency.doesNotContain=" + UPDATED_CURRENCY, "currency.doesNotContain=" + DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCountriesByCurrencyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where currencyName equals to
        defaultCountryFiltering("currencyName.equals=" + DEFAULT_CURRENCY_NAME, "currencyName.equals=" + UPDATED_CURRENCY_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCurrencyNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where currencyName in
        defaultCountryFiltering(
            "currencyName.in=" + DEFAULT_CURRENCY_NAME + "," + UPDATED_CURRENCY_NAME,
            "currencyName.in=" + UPDATED_CURRENCY_NAME
        );
    }

    @Test
    @Transactional
    void getAllCountriesByCurrencyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where currencyName is not null
        defaultCountryFiltering("currencyName.specified=true", "currencyName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByCurrencyNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where currencyName contains
        defaultCountryFiltering("currencyName.contains=" + DEFAULT_CURRENCY_NAME, "currencyName.contains=" + UPDATED_CURRENCY_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCurrencyNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where currencyName does not contain
        defaultCountryFiltering(
            "currencyName.doesNotContain=" + UPDATED_CURRENCY_NAME,
            "currencyName.doesNotContain=" + DEFAULT_CURRENCY_NAME
        );
    }

    @Test
    @Transactional
    void getAllCountriesByCurrencySymbolIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where currencySymbol equals to
        defaultCountryFiltering("currencySymbol.equals=" + DEFAULT_CURRENCY_SYMBOL, "currencySymbol.equals=" + UPDATED_CURRENCY_SYMBOL);
    }

    @Test
    @Transactional
    void getAllCountriesByCurrencySymbolIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where currencySymbol in
        defaultCountryFiltering(
            "currencySymbol.in=" + DEFAULT_CURRENCY_SYMBOL + "," + UPDATED_CURRENCY_SYMBOL,
            "currencySymbol.in=" + UPDATED_CURRENCY_SYMBOL
        );
    }

    @Test
    @Transactional
    void getAllCountriesByCurrencySymbolIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where currencySymbol is not null
        defaultCountryFiltering("currencySymbol.specified=true", "currencySymbol.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByCurrencySymbolContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where currencySymbol contains
        defaultCountryFiltering("currencySymbol.contains=" + DEFAULT_CURRENCY_SYMBOL, "currencySymbol.contains=" + UPDATED_CURRENCY_SYMBOL);
    }

    @Test
    @Transactional
    void getAllCountriesByCurrencySymbolNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where currencySymbol does not contain
        defaultCountryFiltering(
            "currencySymbol.doesNotContain=" + UPDATED_CURRENCY_SYMBOL,
            "currencySymbol.doesNotContain=" + DEFAULT_CURRENCY_SYMBOL
        );
    }

    @Test
    @Transactional
    void getAllCountriesByEmojiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where emoji equals to
        defaultCountryFiltering("emoji.equals=" + DEFAULT_EMOJI, "emoji.equals=" + UPDATED_EMOJI);
    }

    @Test
    @Transactional
    void getAllCountriesByEmojiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where emoji in
        defaultCountryFiltering("emoji.in=" + DEFAULT_EMOJI + "," + UPDATED_EMOJI, "emoji.in=" + UPDATED_EMOJI);
    }

    @Test
    @Transactional
    void getAllCountriesByEmojiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where emoji is not null
        defaultCountryFiltering("emoji.specified=true", "emoji.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByEmojiContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where emoji contains
        defaultCountryFiltering("emoji.contains=" + DEFAULT_EMOJI, "emoji.contains=" + UPDATED_EMOJI);
    }

    @Test
    @Transactional
    void getAllCountriesByEmojiNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where emoji does not contain
        defaultCountryFiltering("emoji.doesNotContain=" + UPDATED_EMOJI, "emoji.doesNotContain=" + DEFAULT_EMOJI);
    }

    @Test
    @Transactional
    void getAllCountriesByEmojiUIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where emojiU equals to
        defaultCountryFiltering("emojiU.equals=" + DEFAULT_EMOJI_U, "emojiU.equals=" + UPDATED_EMOJI_U);
    }

    @Test
    @Transactional
    void getAllCountriesByEmojiUIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where emojiU in
        defaultCountryFiltering("emojiU.in=" + DEFAULT_EMOJI_U + "," + UPDATED_EMOJI_U, "emojiU.in=" + UPDATED_EMOJI_U);
    }

    @Test
    @Transactional
    void getAllCountriesByEmojiUIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where emojiU is not null
        defaultCountryFiltering("emojiU.specified=true", "emojiU.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByEmojiUContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where emojiU contains
        defaultCountryFiltering("emojiU.contains=" + DEFAULT_EMOJI_U, "emojiU.contains=" + UPDATED_EMOJI_U);
    }

    @Test
    @Transactional
    void getAllCountriesByEmojiUNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList where emojiU does not contain
        defaultCountryFiltering("emojiU.doesNotContain=" + UPDATED_EMOJI_U, "emojiU.doesNotContain=" + DEFAULT_EMOJI_U);
    }

    private void defaultCountryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCountryShouldBeFound(shouldBeFound);
        defaultCountryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCountryShouldBeFound(String filter) throws Exception {
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].localName").value(hasItem(DEFAULT_LOCAL_NAME)))
            .andExpect(jsonPath("$.[*].iso2").value(hasItem(DEFAULT_ISO_2)))
            .andExpect(jsonPath("$.[*].iso3").value(hasItem(DEFAULT_ISO_3)))
            .andExpect(jsonPath("$.[*].numericCode").value(hasItem(DEFAULT_NUMERIC_CODE)))
            .andExpect(jsonPath("$.[*].phoneCode").value(hasItem(DEFAULT_PHONE_CODE)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].currencyName").value(hasItem(DEFAULT_CURRENCY_NAME)))
            .andExpect(jsonPath("$.[*].currencySymbol").value(hasItem(DEFAULT_CURRENCY_SYMBOL)))
            .andExpect(jsonPath("$.[*].emoji").value(hasItem(DEFAULT_EMOJI)))
            .andExpect(jsonPath("$.[*].emojiU").value(hasItem(DEFAULT_EMOJI_U)));

        // Check, that the count call also returns 1
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCountryShouldNotBeFound(String filter) throws Exception {
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCountry() throws Exception {
        // Get the country
        restCountryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCountry() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        countrySearchRepository.save(country);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll());

        // Update the country
        Country updatedCountry = countryRepository.findById(country.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCountry are not directly saved in db
        em.detach(updatedCountry);
        updatedCountry
            .name(UPDATED_NAME)
            .localName(UPDATED_LOCAL_NAME)
            .iso2(UPDATED_ISO_2)
            .iso3(UPDATED_ISO_3)
            .numericCode(UPDATED_NUMERIC_CODE)
            .phoneCode(UPDATED_PHONE_CODE)
            .currency(UPDATED_CURRENCY)
            .currencyName(UPDATED_CURRENCY_NAME)
            .currencySymbol(UPDATED_CURRENCY_SYMBOL)
            .emoji(UPDATED_EMOJI)
            .emojiU(UPDATED_EMOJI_U);
        CountryDTO countryDTO = countryMapper.toDto(updatedCountry);

        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCountryToMatchAllProperties(updatedCountry);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Country> countrySearchList = Streamable.of(countrySearchRepository.findAll()).toList();
                Country testCountrySearch = countrySearchList.get(searchDatabaseSizeAfter - 1);

                assertCountryAllPropertiesEquals(testCountrySearch, updatedCountry);
            });
    }

    @Test
    @Transactional
    void putNonExistingCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll());
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll());
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll());
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setId(country.getId());

        partialUpdatedCountry
            .iso2(UPDATED_ISO_2)
            .iso3(UPDATED_ISO_3)
            .phoneCode(UPDATED_PHONE_CODE)
            .currencyName(UPDATED_CURRENCY_NAME)
            .emoji(UPDATED_EMOJI)
            .emojiU(UPDATED_EMOJI_U);

        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCountry))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCountryUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCountry, country), getPersistedCountry(country));
    }

    @Test
    @Transactional
    void fullUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setId(country.getId());

        partialUpdatedCountry
            .name(UPDATED_NAME)
            .localName(UPDATED_LOCAL_NAME)
            .iso2(UPDATED_ISO_2)
            .iso3(UPDATED_ISO_3)
            .numericCode(UPDATED_NUMERIC_CODE)
            .phoneCode(UPDATED_PHONE_CODE)
            .currency(UPDATED_CURRENCY)
            .currencyName(UPDATED_CURRENCY_NAME)
            .currencySymbol(UPDATED_CURRENCY_SYMBOL)
            .emoji(UPDATED_EMOJI)
            .emojiU(UPDATED_EMOJI_U);

        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCountry))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCountryUpdatableFieldsEquals(partialUpdatedCountry, getPersistedCountry(partialUpdatedCountry));
    }

    @Test
    @Transactional
    void patchNonExistingCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll());
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll());
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll());
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(countryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCountry() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);
        countryRepository.save(country);
        countrySearchRepository.save(country);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the country
        restCountryMockMvc
            .perform(delete(ENTITY_API_URL_ID, country.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCountry() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);
        countrySearchRepository.save(country);

        // Search the country
        restCountryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + country.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].localName").value(hasItem(DEFAULT_LOCAL_NAME)))
            .andExpect(jsonPath("$.[*].iso2").value(hasItem(DEFAULT_ISO_2)))
            .andExpect(jsonPath("$.[*].iso3").value(hasItem(DEFAULT_ISO_3)))
            .andExpect(jsonPath("$.[*].numericCode").value(hasItem(DEFAULT_NUMERIC_CODE)))
            .andExpect(jsonPath("$.[*].phoneCode").value(hasItem(DEFAULT_PHONE_CODE)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].currencyName").value(hasItem(DEFAULT_CURRENCY_NAME)))
            .andExpect(jsonPath("$.[*].currencySymbol").value(hasItem(DEFAULT_CURRENCY_SYMBOL)))
            .andExpect(jsonPath("$.[*].emoji").value(hasItem(DEFAULT_EMOJI)))
            .andExpect(jsonPath("$.[*].emojiU").value(hasItem(DEFAULT_EMOJI_U)));
    }

    protected long getRepositoryCount() {
        return countryRepository.count();
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

    protected Country getPersistedCountry(Country country) {
        return countryRepository.findById(country.getId()).orElseThrow();
    }

    protected void assertPersistedCountryToMatchAllProperties(Country expectedCountry) {
        assertCountryAllPropertiesEquals(expectedCountry, getPersistedCountry(expectedCountry));
    }

    protected void assertPersistedCountryToMatchUpdatableProperties(Country expectedCountry) {
        assertCountryAllUpdatablePropertiesEquals(expectedCountry, getPersistedCountry(expectedCountry));
    }
}

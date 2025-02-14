package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.AppUserDeviceAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.AppUserDevice;
import ai.yarmook.shipperfinder.repository.AppUserDeviceRepository;
import ai.yarmook.shipperfinder.repository.search.AppUserDeviceSearchRepository;
import ai.yarmook.shipperfinder.service.dto.AppUserDeviceDTO;
import ai.yarmook.shipperfinder.service.mapper.AppUserDeviceMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link AppUserDeviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppUserDeviceResourceIT {

    private static final String DEFAULT_DEVICE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTIFICATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFICATION_TOKEN = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_LOGIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_LOGIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final UUID DEFAULT_USER_ENC_ID = UUID.randomUUID();
    private static final UUID UPDATED_USER_ENC_ID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/app-user-devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/app-user-devices/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppUserDeviceRepository appUserDeviceRepository;

    @Autowired
    private AppUserDeviceMapper appUserDeviceMapper;

    @Autowired
    private AppUserDeviceSearchRepository appUserDeviceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppUserDeviceMockMvc;

    private AppUserDevice appUserDevice;

    private AppUserDevice insertedAppUserDevice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUserDevice createEntity() {
        return new AppUserDevice()
            .deviceCode(DEFAULT_DEVICE_CODE)
            .notificationToken(DEFAULT_NOTIFICATION_TOKEN)
            .lastLogin(DEFAULT_LAST_LOGIN)
            .active(DEFAULT_ACTIVE)
            .userEncId(DEFAULT_USER_ENC_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUserDevice createUpdatedEntity() {
        return new AppUserDevice()
            .deviceCode(UPDATED_DEVICE_CODE)
            .notificationToken(UPDATED_NOTIFICATION_TOKEN)
            .lastLogin(UPDATED_LAST_LOGIN)
            .active(UPDATED_ACTIVE)
            .userEncId(UPDATED_USER_ENC_ID);
    }

    @BeforeEach
    public void initTest() {
        appUserDevice = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAppUserDevice != null) {
            appUserDeviceRepository.delete(insertedAppUserDevice);
            appUserDeviceSearchRepository.delete(insertedAppUserDevice);
            insertedAppUserDevice = null;
        }
    }

    @Test
    @Transactional
    void createAppUserDevice() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());
        // Create the AppUserDevice
        AppUserDeviceDTO appUserDeviceDTO = appUserDeviceMapper.toDto(appUserDevice);
        var returnedAppUserDeviceDTO = om.readValue(
            restAppUserDeviceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUserDeviceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AppUserDeviceDTO.class
        );

        // Validate the AppUserDevice in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAppUserDevice = appUserDeviceMapper.toEntity(returnedAppUserDeviceDTO);
        assertAppUserDeviceUpdatableFieldsEquals(returnedAppUserDevice, getPersistedAppUserDevice(returnedAppUserDevice));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAppUserDevice = returnedAppUserDevice;
    }

    @Test
    @Transactional
    void createAppUserDeviceWithExistingId() throws Exception {
        // Create the AppUserDevice with an existing ID
        appUserDevice.setId(1L);
        AppUserDeviceDTO appUserDeviceDTO = appUserDeviceMapper.toDto(appUserDevice);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppUserDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUserDeviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AppUserDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAppUserDevices() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList
        restAppUserDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUserDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceCode").value(hasItem(DEFAULT_DEVICE_CODE)))
            .andExpect(jsonPath("$.[*].notificationToken").value(hasItem(DEFAULT_NOTIFICATION_TOKEN)))
            .andExpect(jsonPath("$.[*].lastLogin").value(hasItem(DEFAULT_LAST_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].userEncId").value(hasItem(DEFAULT_USER_ENC_ID.toString())));
    }

    @Test
    @Transactional
    void getAppUserDevice() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get the appUserDevice
        restAppUserDeviceMockMvc
            .perform(get(ENTITY_API_URL_ID, appUserDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appUserDevice.getId().intValue()))
            .andExpect(jsonPath("$.deviceCode").value(DEFAULT_DEVICE_CODE))
            .andExpect(jsonPath("$.notificationToken").value(DEFAULT_NOTIFICATION_TOKEN))
            .andExpect(jsonPath("$.lastLogin").value(DEFAULT_LAST_LOGIN.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE))
            .andExpect(jsonPath("$.userEncId").value(DEFAULT_USER_ENC_ID.toString()));
    }

    @Test
    @Transactional
    void getAppUserDevicesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        Long id = appUserDevice.getId();

        defaultAppUserDeviceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAppUserDeviceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAppUserDeviceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByDeviceCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where deviceCode equals to
        defaultAppUserDeviceFiltering("deviceCode.equals=" + DEFAULT_DEVICE_CODE, "deviceCode.equals=" + UPDATED_DEVICE_CODE);
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByDeviceCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where deviceCode in
        defaultAppUserDeviceFiltering(
            "deviceCode.in=" + DEFAULT_DEVICE_CODE + "," + UPDATED_DEVICE_CODE,
            "deviceCode.in=" + UPDATED_DEVICE_CODE
        );
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByDeviceCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where deviceCode is not null
        defaultAppUserDeviceFiltering("deviceCode.specified=true", "deviceCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByDeviceCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where deviceCode contains
        defaultAppUserDeviceFiltering("deviceCode.contains=" + DEFAULT_DEVICE_CODE, "deviceCode.contains=" + UPDATED_DEVICE_CODE);
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByDeviceCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where deviceCode does not contain
        defaultAppUserDeviceFiltering(
            "deviceCode.doesNotContain=" + UPDATED_DEVICE_CODE,
            "deviceCode.doesNotContain=" + DEFAULT_DEVICE_CODE
        );
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByNotificationTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where notificationToken equals to
        defaultAppUserDeviceFiltering(
            "notificationToken.equals=" + DEFAULT_NOTIFICATION_TOKEN,
            "notificationToken.equals=" + UPDATED_NOTIFICATION_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByNotificationTokenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where notificationToken in
        defaultAppUserDeviceFiltering(
            "notificationToken.in=" + DEFAULT_NOTIFICATION_TOKEN + "," + UPDATED_NOTIFICATION_TOKEN,
            "notificationToken.in=" + UPDATED_NOTIFICATION_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByNotificationTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where notificationToken is not null
        defaultAppUserDeviceFiltering("notificationToken.specified=true", "notificationToken.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByNotificationTokenContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where notificationToken contains
        defaultAppUserDeviceFiltering(
            "notificationToken.contains=" + DEFAULT_NOTIFICATION_TOKEN,
            "notificationToken.contains=" + UPDATED_NOTIFICATION_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByNotificationTokenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where notificationToken does not contain
        defaultAppUserDeviceFiltering(
            "notificationToken.doesNotContain=" + UPDATED_NOTIFICATION_TOKEN,
            "notificationToken.doesNotContain=" + DEFAULT_NOTIFICATION_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByLastLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where lastLogin equals to
        defaultAppUserDeviceFiltering("lastLogin.equals=" + DEFAULT_LAST_LOGIN, "lastLogin.equals=" + UPDATED_LAST_LOGIN);
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByLastLoginIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where lastLogin in
        defaultAppUserDeviceFiltering(
            "lastLogin.in=" + DEFAULT_LAST_LOGIN + "," + UPDATED_LAST_LOGIN,
            "lastLogin.in=" + UPDATED_LAST_LOGIN
        );
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByLastLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where lastLogin is not null
        defaultAppUserDeviceFiltering("lastLogin.specified=true", "lastLogin.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where active equals to
        defaultAppUserDeviceFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where active in
        defaultAppUserDeviceFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where active is not null
        defaultAppUserDeviceFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByUserEncIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where userEncId equals to
        defaultAppUserDeviceFiltering("userEncId.equals=" + DEFAULT_USER_ENC_ID, "userEncId.equals=" + UPDATED_USER_ENC_ID);
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByUserEncIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where userEncId in
        defaultAppUserDeviceFiltering(
            "userEncId.in=" + DEFAULT_USER_ENC_ID + "," + UPDATED_USER_ENC_ID,
            "userEncId.in=" + UPDATED_USER_ENC_ID
        );
    }

    @Test
    @Transactional
    void getAllAppUserDevicesByUserEncIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        // Get all the appUserDeviceList where userEncId is not null
        defaultAppUserDeviceFiltering("userEncId.specified=true", "userEncId.specified=false");
    }

    private void defaultAppUserDeviceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAppUserDeviceShouldBeFound(shouldBeFound);
        defaultAppUserDeviceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppUserDeviceShouldBeFound(String filter) throws Exception {
        restAppUserDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUserDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceCode").value(hasItem(DEFAULT_DEVICE_CODE)))
            .andExpect(jsonPath("$.[*].notificationToken").value(hasItem(DEFAULT_NOTIFICATION_TOKEN)))
            .andExpect(jsonPath("$.[*].lastLogin").value(hasItem(DEFAULT_LAST_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].userEncId").value(hasItem(DEFAULT_USER_ENC_ID.toString())));

        // Check, that the count call also returns 1
        restAppUserDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppUserDeviceShouldNotBeFound(String filter) throws Exception {
        restAppUserDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppUserDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppUserDevice() throws Exception {
        // Get the appUserDevice
        restAppUserDeviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppUserDevice() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUserDeviceSearchRepository.save(appUserDevice);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());

        // Update the appUserDevice
        AppUserDevice updatedAppUserDevice = appUserDeviceRepository.findById(appUserDevice.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAppUserDevice are not directly saved in db
        em.detach(updatedAppUserDevice);
        updatedAppUserDevice
            .deviceCode(UPDATED_DEVICE_CODE)
            .notificationToken(UPDATED_NOTIFICATION_TOKEN)
            .lastLogin(UPDATED_LAST_LOGIN)
            .active(UPDATED_ACTIVE)
            .userEncId(UPDATED_USER_ENC_ID);
        AppUserDeviceDTO appUserDeviceDTO = appUserDeviceMapper.toDto(updatedAppUserDevice);

        restAppUserDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appUserDeviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appUserDeviceDTO))
            )
            .andExpect(status().isOk());

        // Validate the AppUserDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppUserDeviceToMatchAllProperties(updatedAppUserDevice);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AppUserDevice> appUserDeviceSearchList = Streamable.of(appUserDeviceSearchRepository.findAll()).toList();
                AppUserDevice testAppUserDeviceSearch = appUserDeviceSearchList.get(searchDatabaseSizeAfter - 1);

                assertAppUserDeviceAllPropertiesEquals(testAppUserDeviceSearch, updatedAppUserDevice);
            });
    }

    @Test
    @Transactional
    void putNonExistingAppUserDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());
        appUserDevice.setId(longCount.incrementAndGet());

        // Create the AppUserDevice
        AppUserDeviceDTO appUserDeviceDTO = appUserDeviceMapper.toDto(appUserDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppUserDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appUserDeviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appUserDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUserDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppUserDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());
        appUserDevice.setId(longCount.incrementAndGet());

        // Create the AppUserDevice
        AppUserDeviceDTO appUserDeviceDTO = appUserDeviceMapper.toDto(appUserDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appUserDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUserDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppUserDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());
        appUserDevice.setId(longCount.incrementAndGet());

        // Create the AppUserDevice
        AppUserDeviceDTO appUserDeviceDTO = appUserDeviceMapper.toDto(appUserDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserDeviceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUserDeviceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppUserDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAppUserDeviceWithPatch() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appUserDevice using partial update
        AppUserDevice partialUpdatedAppUserDevice = new AppUserDevice();
        partialUpdatedAppUserDevice.setId(appUserDevice.getId());

        partialUpdatedAppUserDevice.notificationToken(UPDATED_NOTIFICATION_TOKEN).lastLogin(UPDATED_LAST_LOGIN).active(UPDATED_ACTIVE);

        restAppUserDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppUserDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppUserDevice))
            )
            .andExpect(status().isOk());

        // Validate the AppUserDevice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppUserDeviceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAppUserDevice, appUserDevice),
            getPersistedAppUserDevice(appUserDevice)
        );
    }

    @Test
    @Transactional
    void fullUpdateAppUserDeviceWithPatch() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appUserDevice using partial update
        AppUserDevice partialUpdatedAppUserDevice = new AppUserDevice();
        partialUpdatedAppUserDevice.setId(appUserDevice.getId());

        partialUpdatedAppUserDevice
            .deviceCode(UPDATED_DEVICE_CODE)
            .notificationToken(UPDATED_NOTIFICATION_TOKEN)
            .lastLogin(UPDATED_LAST_LOGIN)
            .active(UPDATED_ACTIVE)
            .userEncId(UPDATED_USER_ENC_ID);

        restAppUserDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppUserDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppUserDevice))
            )
            .andExpect(status().isOk());

        // Validate the AppUserDevice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppUserDeviceUpdatableFieldsEquals(partialUpdatedAppUserDevice, getPersistedAppUserDevice(partialUpdatedAppUserDevice));
    }

    @Test
    @Transactional
    void patchNonExistingAppUserDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());
        appUserDevice.setId(longCount.incrementAndGet());

        // Create the AppUserDevice
        AppUserDeviceDTO appUserDeviceDTO = appUserDeviceMapper.toDto(appUserDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppUserDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appUserDeviceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appUserDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUserDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppUserDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());
        appUserDevice.setId(longCount.incrementAndGet());

        // Create the AppUserDevice
        AppUserDeviceDTO appUserDeviceDTO = appUserDeviceMapper.toDto(appUserDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appUserDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUserDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppUserDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());
        appUserDevice.setId(longCount.incrementAndGet());

        // Create the AppUserDevice
        AppUserDeviceDTO appUserDeviceDTO = appUserDeviceMapper.toDto(appUserDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserDeviceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(appUserDeviceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppUserDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAppUserDevice() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);
        appUserDeviceRepository.save(appUserDevice);
        appUserDeviceSearchRepository.save(appUserDevice);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the appUserDevice
        restAppUserDeviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, appUserDevice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(appUserDeviceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAppUserDevice() throws Exception {
        // Initialize the database
        insertedAppUserDevice = appUserDeviceRepository.saveAndFlush(appUserDevice);
        appUserDeviceSearchRepository.save(appUserDevice);

        // Search the appUserDevice
        restAppUserDeviceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + appUserDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUserDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceCode").value(hasItem(DEFAULT_DEVICE_CODE)))
            .andExpect(jsonPath("$.[*].notificationToken").value(hasItem(DEFAULT_NOTIFICATION_TOKEN)))
            .andExpect(jsonPath("$.[*].lastLogin").value(hasItem(DEFAULT_LAST_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].userEncId").value(hasItem(DEFAULT_USER_ENC_ID.toString())));
    }

    protected long getRepositoryCount() {
        return appUserDeviceRepository.count();
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

    protected AppUserDevice getPersistedAppUserDevice(AppUserDevice appUserDevice) {
        return appUserDeviceRepository.findById(appUserDevice.getId()).orElseThrow();
    }

    protected void assertPersistedAppUserDeviceToMatchAllProperties(AppUserDevice expectedAppUserDevice) {
        assertAppUserDeviceAllPropertiesEquals(expectedAppUserDevice, getPersistedAppUserDevice(expectedAppUserDevice));
    }

    protected void assertPersistedAppUserDeviceToMatchUpdatableProperties(AppUserDevice expectedAppUserDevice) {
        assertAppUserDeviceAllUpdatablePropertiesEquals(expectedAppUserDevice, getPersistedAppUserDevice(expectedAppUserDevice));
    }
}

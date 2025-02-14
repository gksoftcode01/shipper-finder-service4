package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.LanguagesAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.Languages;
import ai.yarmook.shipperfinder.repository.LanguagesRepository;
import ai.yarmook.shipperfinder.repository.search.LanguagesSearchRepository;
import ai.yarmook.shipperfinder.service.dto.LanguagesDTO;
import ai.yarmook.shipperfinder.service.mapper.LanguagesMapper;
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
 * Integration tests for the {@link LanguagesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LanguagesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/languages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/languages/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LanguagesRepository languagesRepository;

    @Autowired
    private LanguagesMapper languagesMapper;

    @Autowired
    private LanguagesSearchRepository languagesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLanguagesMockMvc;

    private Languages languages;

    private Languages insertedLanguages;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Languages createEntity() {
        return new Languages().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Languages createUpdatedEntity() {
        return new Languages().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        languages = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedLanguages != null) {
            languagesRepository.delete(insertedLanguages);
            languagesSearchRepository.delete(insertedLanguages);
            insertedLanguages = null;
        }
    }

    @Test
    @Transactional
    void createLanguages() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(languagesSearchRepository.findAll());
        // Create the Languages
        LanguagesDTO languagesDTO = languagesMapper.toDto(languages);
        var returnedLanguagesDTO = om.readValue(
            restLanguagesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languagesDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LanguagesDTO.class
        );

        // Validate the Languages in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLanguages = languagesMapper.toEntity(returnedLanguagesDTO);
        assertLanguagesUpdatableFieldsEquals(returnedLanguages, getPersistedLanguages(returnedLanguages));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(languagesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedLanguages = returnedLanguages;
    }

    @Test
    @Transactional
    void createLanguagesWithExistingId() throws Exception {
        // Create the Languages with an existing ID
        languages.setId(1L);
        LanguagesDTO languagesDTO = languagesMapper.toDto(languages);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(languagesSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restLanguagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languagesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Languages in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(languagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllLanguages() throws Exception {
        // Initialize the database
        insertedLanguages = languagesRepository.saveAndFlush(languages);

        // Get all the languagesList
        restLanguagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(languages.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getLanguages() throws Exception {
        // Initialize the database
        insertedLanguages = languagesRepository.saveAndFlush(languages);

        // Get the languages
        restLanguagesMockMvc
            .perform(get(ENTITY_API_URL_ID, languages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(languages.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingLanguages() throws Exception {
        // Get the languages
        restLanguagesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLanguages() throws Exception {
        // Initialize the database
        insertedLanguages = languagesRepository.saveAndFlush(languages);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        languagesSearchRepository.save(languages);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(languagesSearchRepository.findAll());

        // Update the languages
        Languages updatedLanguages = languagesRepository.findById(languages.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLanguages are not directly saved in db
        em.detach(updatedLanguages);
        updatedLanguages.name(UPDATED_NAME);
        LanguagesDTO languagesDTO = languagesMapper.toDto(updatedLanguages);

        restLanguagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, languagesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(languagesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Languages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLanguagesToMatchAllProperties(updatedLanguages);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(languagesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Languages> languagesSearchList = Streamable.of(languagesSearchRepository.findAll()).toList();
                Languages testLanguagesSearch = languagesSearchList.get(searchDatabaseSizeAfter - 1);

                assertLanguagesAllPropertiesEquals(testLanguagesSearch, updatedLanguages);
            });
    }

    @Test
    @Transactional
    void putNonExistingLanguages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(languagesSearchRepository.findAll());
        languages.setId(longCount.incrementAndGet());

        // Create the Languages
        LanguagesDTO languagesDTO = languagesMapper.toDto(languages);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLanguagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, languagesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(languagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Languages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(languagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchLanguages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(languagesSearchRepository.findAll());
        languages.setId(longCount.incrementAndGet());

        // Create the Languages
        LanguagesDTO languagesDTO = languagesMapper.toDto(languages);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(languagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Languages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(languagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLanguages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(languagesSearchRepository.findAll());
        languages.setId(longCount.incrementAndGet());

        // Create the Languages
        LanguagesDTO languagesDTO = languagesMapper.toDto(languages);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguagesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languagesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Languages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(languagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateLanguagesWithPatch() throws Exception {
        // Initialize the database
        insertedLanguages = languagesRepository.saveAndFlush(languages);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the languages using partial update
        Languages partialUpdatedLanguages = new Languages();
        partialUpdatedLanguages.setId(languages.getId());

        partialUpdatedLanguages.name(UPDATED_NAME);

        restLanguagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLanguages.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLanguages))
            )
            .andExpect(status().isOk());

        // Validate the Languages in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLanguagesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLanguages, languages),
            getPersistedLanguages(languages)
        );
    }

    @Test
    @Transactional
    void fullUpdateLanguagesWithPatch() throws Exception {
        // Initialize the database
        insertedLanguages = languagesRepository.saveAndFlush(languages);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the languages using partial update
        Languages partialUpdatedLanguages = new Languages();
        partialUpdatedLanguages.setId(languages.getId());

        partialUpdatedLanguages.name(UPDATED_NAME);

        restLanguagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLanguages.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLanguages))
            )
            .andExpect(status().isOk());

        // Validate the Languages in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLanguagesUpdatableFieldsEquals(partialUpdatedLanguages, getPersistedLanguages(partialUpdatedLanguages));
    }

    @Test
    @Transactional
    void patchNonExistingLanguages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(languagesSearchRepository.findAll());
        languages.setId(longCount.incrementAndGet());

        // Create the Languages
        LanguagesDTO languagesDTO = languagesMapper.toDto(languages);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLanguagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, languagesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(languagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Languages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(languagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLanguages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(languagesSearchRepository.findAll());
        languages.setId(longCount.incrementAndGet());

        // Create the Languages
        LanguagesDTO languagesDTO = languagesMapper.toDto(languages);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(languagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Languages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(languagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLanguages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(languagesSearchRepository.findAll());
        languages.setId(longCount.incrementAndGet());

        // Create the Languages
        LanguagesDTO languagesDTO = languagesMapper.toDto(languages);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguagesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(languagesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Languages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(languagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteLanguages() throws Exception {
        // Initialize the database
        insertedLanguages = languagesRepository.saveAndFlush(languages);
        languagesRepository.save(languages);
        languagesSearchRepository.save(languages);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(languagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the languages
        restLanguagesMockMvc
            .perform(delete(ENTITY_API_URL_ID, languages.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(languagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchLanguages() throws Exception {
        // Initialize the database
        insertedLanguages = languagesRepository.saveAndFlush(languages);
        languagesSearchRepository.save(languages);

        // Search the languages
        restLanguagesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + languages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(languages.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    protected long getRepositoryCount() {
        return languagesRepository.count();
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

    protected Languages getPersistedLanguages(Languages languages) {
        return languagesRepository.findById(languages.getId()).orElseThrow();
    }

    protected void assertPersistedLanguagesToMatchAllProperties(Languages expectedLanguages) {
        assertLanguagesAllPropertiesEquals(expectedLanguages, getPersistedLanguages(expectedLanguages));
    }

    protected void assertPersistedLanguagesToMatchUpdatableProperties(Languages expectedLanguages) {
        assertLanguagesAllUpdatablePropertiesEquals(expectedLanguages, getPersistedLanguages(expectedLanguages));
    }
}

package ai.yarmook.shipperfinder.repository.search;

import ai.yarmook.shipperfinder.domain.AppUserDevice;
import ai.yarmook.shipperfinder.repository.AppUserDeviceRepository;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link AppUserDevice} entity.
 */
public interface AppUserDeviceSearchRepository
    extends ElasticsearchRepository<AppUserDevice, Long>, AppUserDeviceSearchRepositoryInternal {}

interface AppUserDeviceSearchRepositoryInternal {
    Page<AppUserDevice> search(String query, Pageable pageable);

    Page<AppUserDevice> search(Query query);

    @Async
    void index(AppUserDevice entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AppUserDeviceSearchRepositoryInternalImpl implements AppUserDeviceSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AppUserDeviceRepository repository;

    AppUserDeviceSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AppUserDeviceRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<AppUserDevice> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<AppUserDevice> search(Query query) {
        SearchHits<AppUserDevice> searchHits = elasticsearchTemplate.search(query, AppUserDevice.class);
        List<AppUserDevice> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(AppUserDevice entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), AppUserDevice.class);
    }
}

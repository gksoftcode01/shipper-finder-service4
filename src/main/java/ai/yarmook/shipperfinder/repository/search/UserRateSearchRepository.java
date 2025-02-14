package ai.yarmook.shipperfinder.repository.search;

import ai.yarmook.shipperfinder.domain.UserRate;
import ai.yarmook.shipperfinder.repository.UserRateRepository;
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
 * Spring Data Elasticsearch repository for the {@link UserRate} entity.
 */
public interface UserRateSearchRepository extends ElasticsearchRepository<UserRate, Long>, UserRateSearchRepositoryInternal {}

interface UserRateSearchRepositoryInternal {
    Page<UserRate> search(String query, Pageable pageable);

    Page<UserRate> search(Query query);

    @Async
    void index(UserRate entity);

    @Async
    void deleteFromIndexById(Long id);
}

class UserRateSearchRepositoryInternalImpl implements UserRateSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final UserRateRepository repository;

    UserRateSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, UserRateRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<UserRate> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<UserRate> search(Query query) {
        SearchHits<UserRate> searchHits = elasticsearchTemplate.search(query, UserRate.class);
        List<UserRate> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(UserRate entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), UserRate.class);
    }
}

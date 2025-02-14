package ai.yarmook.shipperfinder.repository.search;

import ai.yarmook.shipperfinder.domain.UserSubscribe;
import ai.yarmook.shipperfinder.repository.UserSubscribeRepository;
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
 * Spring Data Elasticsearch repository for the {@link UserSubscribe} entity.
 */
public interface UserSubscribeSearchRepository
    extends ElasticsearchRepository<UserSubscribe, Long>, UserSubscribeSearchRepositoryInternal {}

interface UserSubscribeSearchRepositoryInternal {
    Page<UserSubscribe> search(String query, Pageable pageable);

    Page<UserSubscribe> search(Query query);

    @Async
    void index(UserSubscribe entity);

    @Async
    void deleteFromIndexById(Long id);
}

class UserSubscribeSearchRepositoryInternalImpl implements UserSubscribeSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final UserSubscribeRepository repository;

    UserSubscribeSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, UserSubscribeRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<UserSubscribe> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<UserSubscribe> search(Query query) {
        SearchHits<UserSubscribe> searchHits = elasticsearchTemplate.search(query, UserSubscribe.class);
        List<UserSubscribe> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(UserSubscribe entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), UserSubscribe.class);
    }
}

package ai.yarmook.shipperfinder.repository.search;

import ai.yarmook.shipperfinder.domain.AppUser;
import ai.yarmook.shipperfinder.repository.AppUserRepository;
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
 * Spring Data Elasticsearch repository for the {@link AppUser} entity.
 */
public interface AppUserSearchRepository extends ElasticsearchRepository<AppUser, Long>, AppUserSearchRepositoryInternal {}

interface AppUserSearchRepositoryInternal {
    Page<AppUser> search(String query, Pageable pageable);

    Page<AppUser> search(Query query);

    @Async
    void index(AppUser entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AppUserSearchRepositoryInternalImpl implements AppUserSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AppUserRepository repository;

    AppUserSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AppUserRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<AppUser> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<AppUser> search(Query query) {
        SearchHits<AppUser> searchHits = elasticsearchTemplate.search(query, AppUser.class);
        List<AppUser> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(AppUser entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), AppUser.class);
    }
}

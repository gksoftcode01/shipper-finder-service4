package ai.yarmook.shipperfinder.repository.search;

import ai.yarmook.shipperfinder.domain.Unit;
import ai.yarmook.shipperfinder.repository.UnitRepository;
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
 * Spring Data Elasticsearch repository for the {@link Unit} entity.
 */
public interface UnitSearchRepository extends ElasticsearchRepository<Unit, Long>, UnitSearchRepositoryInternal {}

interface UnitSearchRepositoryInternal {
    Page<Unit> search(String query, Pageable pageable);

    Page<Unit> search(Query query);

    @Async
    void index(Unit entity);

    @Async
    void deleteFromIndexById(Long id);
}

class UnitSearchRepositoryInternalImpl implements UnitSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final UnitRepository repository;

    UnitSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, UnitRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Unit> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Unit> search(Query query) {
        SearchHits<Unit> searchHits = elasticsearchTemplate.search(query, Unit.class);
        List<Unit> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Unit entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Unit.class);
    }
}

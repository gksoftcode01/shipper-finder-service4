package ai.yarmook.shipperfinder.repository.search;

import ai.yarmook.shipperfinder.domain.StateProvince;
import ai.yarmook.shipperfinder.repository.StateProvinceRepository;
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
 * Spring Data Elasticsearch repository for the {@link StateProvince} entity.
 */
public interface StateProvinceSearchRepository
    extends ElasticsearchRepository<StateProvince, Long>, StateProvinceSearchRepositoryInternal {}

interface StateProvinceSearchRepositoryInternal {
    Page<StateProvince> search(String query, Pageable pageable);

    Page<StateProvince> search(Query query);

    @Async
    void index(StateProvince entity);

    @Async
    void deleteFromIndexById(Long id);
}

class StateProvinceSearchRepositoryInternalImpl implements StateProvinceSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final StateProvinceRepository repository;

    StateProvinceSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, StateProvinceRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<StateProvince> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<StateProvince> search(Query query) {
        SearchHits<StateProvince> searchHits = elasticsearchTemplate.search(query, StateProvince.class);
        List<StateProvince> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(StateProvince entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), StateProvince.class);
    }
}

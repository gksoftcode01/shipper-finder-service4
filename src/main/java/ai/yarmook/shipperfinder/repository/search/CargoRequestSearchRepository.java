package ai.yarmook.shipperfinder.repository.search;

import ai.yarmook.shipperfinder.domain.CargoRequest;
import ai.yarmook.shipperfinder.repository.CargoRequestRepository;
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
 * Spring Data Elasticsearch repository for the {@link CargoRequest} entity.
 */
public interface CargoRequestSearchRepository extends ElasticsearchRepository<CargoRequest, Long>, CargoRequestSearchRepositoryInternal {}

interface CargoRequestSearchRepositoryInternal {
    Page<CargoRequest> search(String query, Pageable pageable);

    Page<CargoRequest> search(Query query);

    @Async
    void index(CargoRequest entity);

    @Async
    void deleteFromIndexById(Long id);
}

class CargoRequestSearchRepositoryInternalImpl implements CargoRequestSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final CargoRequestRepository repository;

    CargoRequestSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, CargoRequestRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<CargoRequest> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<CargoRequest> search(Query query) {
        SearchHits<CargoRequest> searchHits = elasticsearchTemplate.search(query, CargoRequest.class);
        List<CargoRequest> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(CargoRequest entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), CargoRequest.class);
    }
}

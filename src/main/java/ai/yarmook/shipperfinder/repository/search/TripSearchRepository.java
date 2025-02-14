package ai.yarmook.shipperfinder.repository.search;

import ai.yarmook.shipperfinder.domain.Trip;
import ai.yarmook.shipperfinder.repository.TripRepository;
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
 * Spring Data Elasticsearch repository for the {@link Trip} entity.
 */
public interface TripSearchRepository extends ElasticsearchRepository<Trip, Long>, TripSearchRepositoryInternal {}

interface TripSearchRepositoryInternal {
    Page<Trip> search(String query, Pageable pageable);

    Page<Trip> search(Query query);

    @Async
    void index(Trip entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TripSearchRepositoryInternalImpl implements TripSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TripRepository repository;

    TripSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TripRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Trip> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Trip> search(Query query) {
        SearchHits<Trip> searchHits = elasticsearchTemplate.search(query, Trip.class);
        List<Trip> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Trip entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Trip.class);
    }
}

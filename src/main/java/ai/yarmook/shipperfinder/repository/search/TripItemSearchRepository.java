package ai.yarmook.shipperfinder.repository.search;

import ai.yarmook.shipperfinder.domain.TripItem;
import ai.yarmook.shipperfinder.repository.TripItemRepository;
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
 * Spring Data Elasticsearch repository for the {@link TripItem} entity.
 */
public interface TripItemSearchRepository extends ElasticsearchRepository<TripItem, Long>, TripItemSearchRepositoryInternal {}

interface TripItemSearchRepositoryInternal {
    Page<TripItem> search(String query, Pageable pageable);

    Page<TripItem> search(Query query);

    @Async
    void index(TripItem entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TripItemSearchRepositoryInternalImpl implements TripItemSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TripItemRepository repository;

    TripItemSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TripItemRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<TripItem> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<TripItem> search(Query query) {
        SearchHits<TripItem> searchHits = elasticsearchTemplate.search(query, TripItem.class);
        List<TripItem> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(TripItem entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), TripItem.class);
    }
}

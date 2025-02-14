package ai.yarmook.shipperfinder.repository.search;

import ai.yarmook.shipperfinder.domain.TripMsg;
import ai.yarmook.shipperfinder.repository.TripMsgRepository;
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
 * Spring Data Elasticsearch repository for the {@link TripMsg} entity.
 */
public interface TripMsgSearchRepository extends ElasticsearchRepository<TripMsg, Long>, TripMsgSearchRepositoryInternal {}

interface TripMsgSearchRepositoryInternal {
    Page<TripMsg> search(String query, Pageable pageable);

    Page<TripMsg> search(Query query);

    @Async
    void index(TripMsg entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TripMsgSearchRepositoryInternalImpl implements TripMsgSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TripMsgRepository repository;

    TripMsgSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TripMsgRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<TripMsg> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<TripMsg> search(Query query) {
        SearchHits<TripMsg> searchHits = elasticsearchTemplate.search(query, TripMsg.class);
        List<TripMsg> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(TripMsg entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), TripMsg.class);
    }
}

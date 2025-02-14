package ai.yarmook.shipperfinder.repository.search;

import ai.yarmook.shipperfinder.domain.SubscribeTypeDetail;
import ai.yarmook.shipperfinder.repository.SubscribeTypeDetailRepository;
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
 * Spring Data Elasticsearch repository for the {@link SubscribeTypeDetail} entity.
 */
public interface SubscribeTypeDetailSearchRepository
    extends ElasticsearchRepository<SubscribeTypeDetail, Long>, SubscribeTypeDetailSearchRepositoryInternal {}

interface SubscribeTypeDetailSearchRepositoryInternal {
    Page<SubscribeTypeDetail> search(String query, Pageable pageable);

    Page<SubscribeTypeDetail> search(Query query);

    @Async
    void index(SubscribeTypeDetail entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SubscribeTypeDetailSearchRepositoryInternalImpl implements SubscribeTypeDetailSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SubscribeTypeDetailRepository repository;

    SubscribeTypeDetailSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, SubscribeTypeDetailRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<SubscribeTypeDetail> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<SubscribeTypeDetail> search(Query query) {
        SearchHits<SubscribeTypeDetail> searchHits = elasticsearchTemplate.search(query, SubscribeTypeDetail.class);
        List<SubscribeTypeDetail> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(SubscribeTypeDetail entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), SubscribeTypeDetail.class);
    }
}

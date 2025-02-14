package ai.yarmook.shipperfinder.repository.search;

import ai.yarmook.shipperfinder.domain.ItemType;
import ai.yarmook.shipperfinder.repository.ItemTypeRepository;
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
 * Spring Data Elasticsearch repository for the {@link ItemType} entity.
 */
public interface ItemTypeSearchRepository extends ElasticsearchRepository<ItemType, Long>, ItemTypeSearchRepositoryInternal {}

interface ItemTypeSearchRepositoryInternal {
    Page<ItemType> search(String query, Pageable pageable);

    Page<ItemType> search(Query query);

    @Async
    void index(ItemType entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ItemTypeSearchRepositoryInternalImpl implements ItemTypeSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ItemTypeRepository repository;

    ItemTypeSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ItemTypeRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<ItemType> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<ItemType> search(Query query) {
        SearchHits<ItemType> searchHits = elasticsearchTemplate.search(query, ItemType.class);
        List<ItemType> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(ItemType entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ItemType.class);
    }
}

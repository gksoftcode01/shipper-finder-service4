package ai.yarmook.shipperfinder.repository.search;

import ai.yarmook.shipperfinder.domain.CargoMsg;
import ai.yarmook.shipperfinder.repository.CargoMsgRepository;
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
 * Spring Data Elasticsearch repository for the {@link CargoMsg} entity.
 */
public interface CargoMsgSearchRepository extends ElasticsearchRepository<CargoMsg, Long>, CargoMsgSearchRepositoryInternal {}

interface CargoMsgSearchRepositoryInternal {
    Page<CargoMsg> search(String query, Pageable pageable);

    Page<CargoMsg> search(Query query);

    @Async
    void index(CargoMsg entity);

    @Async
    void deleteFromIndexById(Long id);
}

class CargoMsgSearchRepositoryInternalImpl implements CargoMsgSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final CargoMsgRepository repository;

    CargoMsgSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, CargoMsgRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<CargoMsg> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<CargoMsg> search(Query query) {
        SearchHits<CargoMsg> searchHits = elasticsearchTemplate.search(query, CargoMsg.class);
        List<CargoMsg> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(CargoMsg entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), CargoMsg.class);
    }
}

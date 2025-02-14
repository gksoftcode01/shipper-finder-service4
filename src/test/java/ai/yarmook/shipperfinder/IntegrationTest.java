package ai.yarmook.shipperfinder;

import ai.yarmook.shipperfinder.config.AsyncSyncConfiguration;
import ai.yarmook.shipperfinder.config.EmbeddedElasticsearch;
import ai.yarmook.shipperfinder.config.EmbeddedKafka;
import ai.yarmook.shipperfinder.config.EmbeddedSQL;
import ai.yarmook.shipperfinder.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { Shipperfinderservice4App.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedElasticsearch
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}

package ai.yarmook.shipperfinder.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, ai.yarmook.shipperfinder.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, ai.yarmook.shipperfinder.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, ai.yarmook.shipperfinder.domain.User.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.Authority.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.User.class.getName() + ".authorities");
            createCache(cm, ai.yarmook.shipperfinder.domain.StateProvince.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.Country.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.Country.class.getName() + ".stateProvinces");
            createCache(cm, ai.yarmook.shipperfinder.domain.ReportAbuse.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.UserRate.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.ItemType.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.Item.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.AppUser.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.AppUserDevice.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.SubscribeType.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.SubscribeTypeDetail.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.UserSubscribe.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.Languages.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.Trip.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.Trip.class.getName() + ".items");
            createCache(cm, ai.yarmook.shipperfinder.domain.Tag.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.Tag.class.getName() + ".tripItems");
            createCache(cm, ai.yarmook.shipperfinder.domain.Tag.class.getName() + ".cargoRequestItems");
            createCache(cm, ai.yarmook.shipperfinder.domain.Unit.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.TripItem.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.TripItem.class.getName() + ".tags");
            createCache(cm, ai.yarmook.shipperfinder.domain.CargoRequest.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.CargoRequest.class.getName() + ".items");
            createCache(cm, ai.yarmook.shipperfinder.domain.CargoRequestItem.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.CargoRequestItem.class.getName() + ".tags");
            createCache(cm, ai.yarmook.shipperfinder.domain.ShowNumberHistory.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.TripMsg.class.getName());
            createCache(cm, ai.yarmook.shipperfinder.domain.CargoMsg.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}

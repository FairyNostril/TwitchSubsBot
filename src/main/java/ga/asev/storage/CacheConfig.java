package ga.asev.storage;

import ga.asev.storage.model.UserData;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    public static final String USER_DATA_CACHE = "USER_DATA_CACHE";



    @Bean
    public PersistentCacheManager persistentCacheManager() {
        PersistentCacheManager persistentCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .with(CacheManagerBuilder.persistence("d:\\Work\\proj\\twiche\\twitch-bot-data"))
                .withCache(USER_DATA_CACHE, CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, UserData.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder().disk(1, MemoryUnit.GB, true))
                ).build(true);
        return persistentCacheManager;
    }

}

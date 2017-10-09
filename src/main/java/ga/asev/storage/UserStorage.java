package ga.asev.storage;

import ga.asev.client.model.TokenException;
import ga.asev.storage.model.UserData;
import org.ehcache.Cache;
import org.ehcache.PersistentCacheManager;
import org.springframework.stereotype.Component;

import static ga.asev.storage.CacheConfig.USER_DATA_CACHE;
import static java.util.Optional.ofNullable;

@Component
public class UserStorage {

    private final PersistentCacheManager cacheManager;

    public UserStorage(PersistentCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public UserData getUserData(String id) {
        UserData userData = getCache().get(id);
        return ofNullable(userData)
                .orElseThrow(TokenException::new);
    }

    public void saveUserData(String id, UserData userData) {
        getCache().put(id, userData);
    }

    public UserData removeUserData(String id) {
        UserData userData = getUserData(id);
        getCache().remove(id);
        return userData;
    }

    private Cache<String, UserData> getCache() {
        return cacheManager.getCache(USER_DATA_CACHE, String.class, UserData.class);
    }

}

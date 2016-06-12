package payara.reactive.rest;

import javax.cache.annotation.*;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ComputationCache {
    @CachePut(cacheName = "computation")
    public void put(@CacheKey Integer id, @CacheValue Object value) {
    }
}

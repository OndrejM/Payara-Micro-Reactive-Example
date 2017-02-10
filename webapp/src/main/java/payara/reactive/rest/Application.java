package payara.reactive.rest;

import fish.payara.micro.cdi.ClusteredCDIEventBus;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletContext;

@ApplicationScoped
public class Application {
    
    public static final String CACHE_NAME = "computation";
    
    @Inject
    private ClusteredCDIEventBus bus;
    
    @Inject
    CacheManager cm;
    
    public void onStart(@Observes @Initialized(ApplicationScoped.class) ServletContext init) {
        bus.initialize();
    }
    
    @PostConstruct
    public void init() {
        Cache cache = cm.getCache(CACHE_NAME);
        if (cache == null)
        {
            cm.createCache(CACHE_NAME, new MutableConfiguration<Integer, Object>());
        }        
    }
    
    @PreDestroy
    public void shutdown() {
        cm.getCache(CACHE_NAME).close();
    }
    
}

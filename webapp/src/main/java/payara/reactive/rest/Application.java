package payara.reactive.rest;

import fish.payara.micro.cdi.ClusteredCDIEventBus;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class Application {
    
    public static final String CACHE_NAME = "computation";
    
    @Inject
    private ClusteredCDIEventBus bus;
    
    public void onStart(@Observes @Initialized(ApplicationScoped.class) Object init) {
        bus.initialize();
    }
    
    @PostConstruct
    public void init() {
        Caching.getCachingProvider()
                .getCacheManager().createCache(CACHE_NAME, new MutableConfiguration<Integer, Object>());
        
    }
    
    @PreDestroy
    public void shutdown() {
        Caching.getCachingProvider()
                .getCacheManager()
                .getCache(CACHE_NAME)
                .close();
    }
    
}

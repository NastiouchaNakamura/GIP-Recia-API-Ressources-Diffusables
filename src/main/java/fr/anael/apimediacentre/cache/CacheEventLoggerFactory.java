package fr.anael.apimediacentre.cache;

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;

import java.util.Properties;

public class CacheEventLoggerFactory extends CacheEventListenerFactory {
    @Override
    public CacheEventListener createCacheEventListener(Properties properties) {
        return new CacheEventLogger();
    }
}

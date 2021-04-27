package fr.anael.apimediacentre.cache;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

@Slf4j
public class CacheEventLogger implements CacheEventListener {
    @Override
    public void notifyElementRemoved(Ehcache ehcache, Element element) throws CacheException {
        log.debug("Cache: Element removed in " + ehcache.getName());
    }

    @Override
    public void notifyElementPut(Ehcache ehcache, Element element) throws CacheException {
        log.debug("Cache: Element put in " + ehcache.getName());
    }

    @Override
    public void notifyElementUpdated(Ehcache ehcache, Element element) throws CacheException {
        log.debug("Cache: Element updated in " + ehcache.getName());
    }

    @Override
    public void notifyElementExpired(Ehcache ehcache, Element element) {
        log.debug("Cache: Element expired in " + ehcache.getName());
    }

    @Override
    public void notifyElementEvicted(Ehcache ehcache, Element element) {
        log.debug("Cache: Element evicted in " + ehcache.getName());
    }

    @Override
    public void notifyRemoveAll(Ehcache ehcache) {
        log.debug("Cache: Removed all elements from " + ehcache.getName());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        log.debug("Cache: Cloning not supported");
        return super.clone();
    }

    @Override
    public void dispose() {
        log.debug("Cache: Disposal not supported");
    }
}

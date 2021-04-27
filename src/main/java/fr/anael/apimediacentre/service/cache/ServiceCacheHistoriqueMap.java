package fr.anael.apimediacentre.service.cache;

import fr.anael.apimediacentre.model.RessourceDiffusable;
import fr.anael.apimediacentre.model.RessourceDiffusableFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ServiceCacheHistoriqueMap implements ServiceCacheHistorique {
    // Attributs
    private final LinkedHashMap<RessourceDiffusableFilter, List<RessourceDiffusable>> historiqueRequetes = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(final Map.Entry eldest) {
            return size() > 10;
        }
    };

    // Méthodes
    @Override
    public List<RessourceDiffusable> get(RessourceDiffusableFilter filter) {
        return this.historiqueRequetes.get(filter);
    }

    @Override
    public void put(RessourceDiffusableFilter filter, List<RessourceDiffusable> ressourcesDiffusables) {
        log.debug("Cache: Ressource diffusable put in history using HashMap; new size of history is " + this.historiqueRequetes.size());
        this.historiqueRequetes.put(filter, ressourcesDiffusables);
    }

    @Override
    public int size() {
        return this.historiqueRequetes.size();
    }

    @Override
    public void clear() {
        int count = this.historiqueRequetes.size();
        this.historiqueRequetes.clear();
        log.debug("Cache: History cleared using HashMap; " + count + " elements removed");
    }
}

package fr.anael.apimediacentre.service.cache;

import fr.anael.apimediacentre.model.RessourceDiffusable;
import fr.anael.apimediacentre.model.RessourceDiffusableFilter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ServiceCacheHistorique {
    // Méthodes
    List<RessourceDiffusable> get(RessourceDiffusableFilter filter);

    void put(RessourceDiffusableFilter filter, List<RessourceDiffusable> ressourcesDiffusables);

    int size();

    void clear();
}

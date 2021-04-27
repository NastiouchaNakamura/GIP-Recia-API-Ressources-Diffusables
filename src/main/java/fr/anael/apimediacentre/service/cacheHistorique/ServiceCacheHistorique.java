package fr.anael.apimediacentre.service.cacheHistorique;

import fr.anael.apimediacentre.model.RessourceDiffusable;
import fr.anael.apimediacentre.model.RessourceDiffusableFilter;

import java.util.List;

public interface ServiceCacheHistorique {
    // Méthodes
    List<RessourceDiffusable> get(RessourceDiffusableFilter filter);

    void put(RessourceDiffusableFilter filter, List<RessourceDiffusable> ressourcesDiffusables);

    int size();

    void clear();
}

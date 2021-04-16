package fr.anael.apimediacentre.service;

import fr.anael.apimediacentre.model.RessourceDiffusable;
import fr.anael.apimediacentre.model.RessourceDiffusableFilter;

import java.util.Collection;

public interface MediacentreService {
    // Getteurs
    int getNombreDePages(int elementsParPage, RessourceDiffusableFilter filter);

    Collection<RessourceDiffusable> getRessourcesDiffusables(int page, int elementsParPage, RessourceDiffusableFilter filter);
}

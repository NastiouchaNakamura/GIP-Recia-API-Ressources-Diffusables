package fr.anael.apimediacentre.service;

import fr.anael.apimediacentre.model.RessourceDiffusable;

import java.util.Collection;

public interface MediacentreService {
    // Getteurs
    int getNombreDePages(int elementsParPage);

    Collection<RessourceDiffusable> getRessourcesDiffusables(int page, int elementsParPage);
}

package fr.anael.apimediacentre.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

public class RessourceDiffusable implements Serializable {
    // Attributs
    private final AttributRessource ressource;
    private final AttributRessource editeur;
    private final Collection<AttributRessource> distributeursCom;
    private final AttributRessource distributeurTech;
    private final boolean affichable;
    private final boolean diffusable;

    // Constructeurs
    public RessourceDiffusable(
            AttributRessource ressource,
            AttributRessource editeur,
            Collection<AttributRessource> distributeursCom,
            AttributRessource distributeurTech,
            boolean affichable,
            boolean diffusable
    ) {
        this.ressource = ressource;
        this.editeur = editeur;
        this.distributeursCom = distributeursCom;
        this.distributeurTech = distributeurTech;
        this.affichable = affichable;
        this.diffusable = diffusable;
    }

    // Getteurs
    public AttributRessource getRessource() {
        return this.ressource;
    }

    public AttributRessource getEditeur() {
        return this.editeur;
    }

    public Collection<AttributRessource> getDistributeursCom() {
        return this.distributeursCom;
    }

    public AttributRessource getDistributeurTech() {
        return this.distributeurTech;
    }

    public boolean isAffichable() {
        return this.affichable;
    }

    public boolean isDiffusable() {
        return this.diffusable;
    }

    // Méthodes
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RessourceDiffusable that = (RessourceDiffusable) o;
        return affichable == that.affichable && diffusable == that.diffusable && ressource.equals(that.ressource) && editeur.equals(that.editeur) && distributeursCom.equals(that.distributeursCom) && distributeurTech.equals(that.distributeurTech);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ressource, editeur, distributeursCom, distributeurTech, affichable, diffusable);
    }
}

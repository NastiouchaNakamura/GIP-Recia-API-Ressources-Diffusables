package fr.anael.apimediacentre.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;

public class RessourceDiffusable {
    // Attributs
    private final String idRessource;
    private final String nomRessource;
    private final String idEditeur;
    private final Collection<String> distributeursCom;
    private final String distributeurTech;
    private final boolean affichable;
    private final boolean diffusable;

    // Constructeurs
    public RessourceDiffusable(
            String idRessource,
            String nomRessource,
            String idEditeur,
            Collection<String> distributeursCom,
            String distributeurTech,
            boolean affichable,
            boolean diffusable
    ) {
        this.idRessource = idRessource;
        this.nomRessource = nomRessource;
        this.idEditeur = idEditeur;
        this.distributeursCom = distributeursCom;
        this.distributeurTech = distributeurTech;
        this.affichable = affichable;
        this.diffusable = diffusable;
    }

    // Getteurs
    @JsonIgnore
    public String getIdRessource() {
        return this.idRessource;
    }

    public String getNomRessource() {
        return this.nomRessource;
    }

    public String getIdEditeur() {
        return this.idEditeur;
    }

    public Collection<String> getDistributeursCom() {
        return this.distributeursCom;
    }

    public String getDistributeurTech() {
        return this.distributeurTech;
    }

    public boolean isAffichable() {
        return this.affichable;
    }

    public boolean isDiffusable() {
        return this.diffusable;
    }
}

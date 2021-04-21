package fr.anael.apimediacentre.model;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

public class RessourceDiffusableFilter {
    // Attributs
    private final String idRessource;
    private final String nomRessource;
    private final String idEditeur;
    private final String distributeurCom;
    private final String distributeurTech;
    private final Boolean affichable;
    private final Boolean diffusable;

    // Constructeurs
    public RessourceDiffusableFilter(
            String idRessource,
            String nomRessource,
            String idEditeur,
            String distributeurCom,
            String distributeurTech,
            Boolean affichable,
            Boolean diffusable
    ) {
        this.idRessource = idRessource;
        this.nomRessource = nomRessource;
        this.idEditeur = idEditeur;
        this.distributeurCom = distributeurCom;
        this.distributeurTech = distributeurTech;
        this.affichable = affichable;
        this.diffusable = diffusable;
    }

    // Getteurs
    public String getIdRessource() {
        return this.idRessource;
    }

    public String getNomRessource() {
        return this.nomRessource;
    }

    public String getIdEditeur() {
        return this.idEditeur;
    }

    public String getDistributeurCom() {
        return this.distributeurCom;
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

    // Méthodes
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RessourceDiffusableFilter that = (RessourceDiffusableFilter) o;
        return affichable == that.affichable && diffusable == that.diffusable && Objects.equals(idRessource, that.idRessource) && Objects.equals(nomRessource, that.nomRessource) && Objects.equals(idEditeur, that.idEditeur) && Objects.equals(distributeurCom, that.distributeurCom) && Objects.equals(distributeurTech, that.distributeurTech);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRessource, nomRessource, idEditeur, distributeurCom, distributeurTech, affichable, diffusable);
    }

    public boolean filter(RessourceDiffusable ressourceDiffusable) {
        return (this.idRessource == null || ressourceDiffusable.getIdRessource().contains(this.idRessource)) &&
                (this.nomRessource == null || ressourceDiffusable.getNomRessource().contains(this.nomRessource)) &&
                (this.idEditeur == null || ressourceDiffusable.getIdEditeur().contains(this.idEditeur)) &&
                (this.distributeurCom == null || ressourceDiffusable.getDistributeursCom().contains(this.distributeurCom)) && // TODO: Ici on check la chaîne entière il faudrait check une partie uniquement comme pour les autres.
                (this.distributeurTech == null || ressourceDiffusable.getDistributeurTech().contains(this.distributeurTech)) &&
                (this.affichable == null || this.affichable == ressourceDiffusable.isAffichable()) &&
                (this.diffusable == null || this.diffusable == ressourceDiffusable.isDiffusable());
    }

    public boolean isEmpty() {
        return this.idRessource == null &&
                this.nomRessource == null &&
                this.idEditeur == null &&
                this.distributeurCom == null &&
                this.distributeurTech == null &&
                this.affichable == null &&
                this.diffusable == null;
    }
}

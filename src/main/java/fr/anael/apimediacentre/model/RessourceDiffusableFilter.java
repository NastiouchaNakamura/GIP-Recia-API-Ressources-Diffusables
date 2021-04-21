package fr.anael.apimediacentre.model;

import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
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

    private boolean containsLowerCase(String s1, String s2) {
        return s1.toLowerCase(Locale.ROOT).contains(s2.toLowerCase(Locale.ROOT));
    }

    public boolean filter(RessourceDiffusable rd) {
        if (this.idRessource != null && !this.containsLowerCase(rd.getIdRessource(), this.idRessource)) {
            return false;
        }

        if (this.nomRessource != null && !this.containsLowerCase(rd.getNomRessource(), this.nomRessource)) {
            return false;
        }

        if (this.idEditeur != null && !this.containsLowerCase(rd.getIdEditeur(), this.idEditeur)) {
            return false;
        }

        if (this.distributeurCom != null) {
            boolean valide = false;
            for (String distributeurCom : rd.getDistributeursCom()) {
                if (this.containsLowerCase(distributeurCom, this.distributeurCom)) {
                    valide = true;
                    break;
                }
            }
            if (!valide) {
                return false;
            }
        }

        if (this.distributeurTech != null && !this.containsLowerCase(rd.getDistributeurTech(), this.distributeurTech)) {
            return false;
        }

        if (this.affichable != null && this.affichable != rd.isAffichable()) {
            return false;
        }

        if (this.diffusable != null && this.diffusable != rd.isDiffusable()) {
            return false;
        }

        return true;
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

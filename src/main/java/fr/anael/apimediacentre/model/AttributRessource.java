package fr.anael.apimediacentre.model;

import java.io.Serializable;
import java.util.Objects;

public class AttributRessource implements Serializable {
    // Attributs
    private final String id;
    private final String nom;

    // Constructeurs
    public AttributRessource(String id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    // Getteurs
    public String getId() {
        return this.id;
    }

    public String getNom() {
        return this.nom;
    }

    // Méthodes
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributRessource that = (AttributRessource) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

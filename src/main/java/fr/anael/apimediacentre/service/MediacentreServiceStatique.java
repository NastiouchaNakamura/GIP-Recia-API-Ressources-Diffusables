package fr.anael.apimediacentre.service;

import fr.anael.apimediacentre.model.RessourceDiffusable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MediacentreServiceStatique implements MediacentreService {
    // Attributs
    private final List<RessourceDiffusable> ressourcesDiffusables = new ArrayList<>();

    // Constructeurs
    public MediacentreServiceStatique() {
        this.ajouterRessource(
                new RessourceDiffusable(
                        "ark/86527/049777635741477512036823067623231707978",
                        "Outils et langages numériques - Première Spécialité - Voie Technologique - STD2A (Tactileo Maskott)",
                        "479875718_0000000000000000",
                        Arrays.asList("479875718_0000000000000000"),
                        "479875718_0000000000000000",
                        true,
                        true

                )
        );

        for (int i = 0; i < 100; i++) {
            this.ajouterRessource(
                    new RessourceDiffusable(
                            "idRessource." + i,
                            "nomRessource." + i,
                            "idEditeur." + i,
                            Arrays.asList("distributeurCom." + i + ".1", "distributeurCom." + i + ".2"),
                            "distributeurTech." + i,
                            true,
                            true
                    )
            );
        }
    }

    // Getteurs
    @Override
    public int getNombreDePages(int elementsParPage) {
        return (int) Math.ceil(this.ressourcesDiffusables.size() / (double) elementsParPage);
    }

    @Override
    public Collection<RessourceDiffusable> getRessourcesDiffusables(int page, int elementsParPage) {
        List<RessourceDiffusable> ressourcesDiffusables = new ArrayList<>();
        for (int i = page * elementsParPage; i < Math.min((page + 1) * elementsParPage, this.ressourcesDiffusables.size()); i++) {
            ressourcesDiffusables.add(this.ressourcesDiffusables.get(i));
        }
        return ressourcesDiffusables;
    }

    // Méthodes
    private void ajouterRessource(RessourceDiffusable ressourceDiffusable) {
        this.ressourcesDiffusables.add(ressourceDiffusable);
    }
}

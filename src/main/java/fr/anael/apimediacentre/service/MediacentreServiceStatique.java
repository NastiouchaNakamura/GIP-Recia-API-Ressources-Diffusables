package fr.anael.apimediacentre.service;

import fr.anael.apimediacentre.model.AttributRessource;
import fr.anael.apimediacentre.model.RessourceDiffusable;
import fr.anael.apimediacentre.model.RessourceDiffusableFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MediacentreServiceStatique implements MediacentreService {
    // Attributs
    private final List<RessourceDiffusable> ressourcesDiffusablesComplet = new ArrayList<>();

    // Getteurs
    @Override
    public int getNombreDePages(int elementsParPage, RessourceDiffusableFilter filter) {
        if (this.ressourcesDiffusablesComplet.isEmpty()) {
            this.initialize();
        }

        if (filter.isEmpty()) {
            return (int) Math.ceil(this.ressourcesDiffusablesComplet.size() / (double) elementsParPage);
        } else {
            List<RessourceDiffusable> ressourcesDiffusablesFiltrees = new ArrayList<>();
            for (RessourceDiffusable ressourceDiffusable : this.ressourcesDiffusablesComplet) {
                if (filter.filter(ressourceDiffusable)) {
                    ressourcesDiffusablesFiltrees.add(ressourceDiffusable);
                }
            }

            return (int) Math.ceil(ressourcesDiffusablesFiltrees.size() / (double) elementsParPage);
        }
    }

    @Override
    public Collection<RessourceDiffusable> getRessourcesDiffusables(int page, int elementsParPage, RessourceDiffusableFilter filter) {
        if (this.ressourcesDiffusablesComplet.isEmpty()) {
            this.initialize();
        }

        List<RessourceDiffusable> ressourcesDiffusablesFiltrees = new ArrayList<>();
        for (RessourceDiffusable ressourceDiffusable : this.ressourcesDiffusablesComplet) {
            if (filter.filter(ressourceDiffusable)) {
                ressourcesDiffusablesFiltrees.add(ressourceDiffusable);
            }
        }

        return ressourcesDiffusablesFiltrees;
    }

    // Méthodes
    private void initialize() {
        this.ajouterRessource(
                new RessourceDiffusable(
                        new AttributRessource(
                                "ark/86527/049777635741477512036823067623231707978",
                                "Outils et langages numériques - Première Spécialité - Voie Technologique - STD2A (Tactileo Maskott)"
                        ),
                        new AttributRessource(
                                "479875718_0000000000000000",
                                ""
                        ),
                        Arrays.asList(
                                new AttributRessource(
                                        "479875718_0000000000000000",
                                        ""
                                )
                        ),
                        new AttributRessource(
                                "479875718_0000000000000000",
                                ""
                        ),
                        true,
                        true

                )
        );

        for (int i = 0; i < 100; i++) {
            this.ajouterRessource(
                    new RessourceDiffusable(
                            new AttributRessource(
                                    "idRessource." + i,
                                    "nomRessource." + i
                            ),
                            new AttributRessource(
                                    "idEditeur." + i,
                                    ""
                            ),
                            Arrays.asList(
                                    new AttributRessource(
                                            "distributeurCom." + i + ".1",
                                            ""
                                    ),
                                    new AttributRessource(
                                            "distributeurCom." + i + ".2",
                                            ""
                                    )
                            ),
                            new AttributRessource(
                                    "distributeurTech." + i,
                                    ""
                            ),
                            true,
                            true

                    )
            );
        }
    }

    private void ajouterRessource(RessourceDiffusable ressourceDiffusable) {
        this.ressourcesDiffusablesComplet.add(ressourceDiffusable);
    }
}

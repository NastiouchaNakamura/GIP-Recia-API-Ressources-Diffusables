package fr.anael.apimediacentre.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.anael.apimediacentre.model.AttributRessource;
import fr.anael.apimediacentre.model.RessourceDiffusable;
import fr.anael.apimediacentre.model.RessourceDiffusableFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.SECONDS;

@Slf4j
public class MediacentreServiceHttpGet implements MediacentreService {
    // Attributs
    @Value("${mediacentre.ressources-diffusables-uri:}")
    private String ressourcesDiffusablesUri;

    private final List<RessourceDiffusable> ressourcesDiffusablesComplet = new ArrayList<>();

    private File ressourcesDiffusablesFile = null;

    private LocalDateTime dateGeneration = null;

    private LocalDateTime dateTelechargement = null;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final LinkedHashMap<RessourceDiffusableFilter, List<RessourceDiffusable>> historiqueRequetes = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(final Map.Entry eldest) {
            return size() > 10;
        }
    };

    // Getteurs
    @Override
    public int getNombreDePages(int elementsParPage, RessourceDiffusableFilter filter) {
        this.verifValidite();

        if (filter.isEmpty()) {
            return (int) Math.ceil(this.ressourcesDiffusablesComplet.size() / (double) elementsParPage);
        } else {
            List<RessourceDiffusable> ressourcesDiffusablesHistorisees = this.historiqueRequetes.get(filter);
            if (ressourcesDiffusablesHistorisees != null) {
                return (int) Math.ceil(ressourcesDiffusablesHistorisees.size() / (double) elementsParPage);
            } else {
                List<RessourceDiffusable> ressourcesDiffusablesFiltrees = new ArrayList<>();
                for (RessourceDiffusable ressourceDiffusable : this.ressourcesDiffusablesComplet) {
                    if (filter.filter(ressourceDiffusable)) {
                        ressourcesDiffusablesFiltrees.add(ressourceDiffusable);
                    }
                }
                this.historiqueRequetes.put(filter, ressourcesDiffusablesFiltrees);
                return (int) Math.ceil(ressourcesDiffusablesFiltrees.size() / (double) elementsParPage);
            }
        }
    }

    @Override
    public Collection<RessourceDiffusable> getRessourcesDiffusables(int page, int elementsParPage, RessourceDiffusableFilter filter) {
        // On vérifie que les données sont toujours valides.
        this.verifValidite();

        if (filter.isEmpty()) {
            log.debug("Ressources diffusables request: No filter; no need to check history");
            return this.genererPage(this.ressourcesDiffusablesComplet, page, elementsParPage);
        } else {
            List<RessourceDiffusable> ressourcesDiffusablesHistorisees = this.historiqueRequetes.get(filter);
            if (ressourcesDiffusablesHistorisees != null) {
                log.debug("Ressources diffusables request: Getting request result from history");
                return this.genererPage(ressourcesDiffusablesHistorisees, page, elementsParPage);
            } else {
                List<RessourceDiffusable> ressourcesDiffusablesFiltrees = new ArrayList<>();
                for (RessourceDiffusable ressourceDiffusable : this.ressourcesDiffusablesComplet) {
                    if (filter.filter(ressourceDiffusable)) {
                        ressourcesDiffusablesFiltrees.add(ressourceDiffusable);
                    }
                }
                log.debug("Ressources diffusables request: Putting request result in history; new size of history will be " + Math.min(this.historiqueRequetes.size() + 1, 6));
                this.historiqueRequetes.put(filter, ressourcesDiffusablesFiltrees);

                return this.genererPage(ressourcesDiffusablesFiltrees, page, elementsParPage);
            }
        }
    }

    // Méthodes
    private List<RessourceDiffusable> genererPage(List<RessourceDiffusable> ressourcesDiffusablesTotal, int page, int elementsParPage) {
        List<RessourceDiffusable> ressourcesDiffusables = new ArrayList<>();
        for (int i = page * elementsParPage; i < Math.min((page + 1) * elementsParPage, ressourcesDiffusablesTotal.size()); i++) {
            ressourcesDiffusables.add(ressourcesDiffusablesTotal.get(i));
        }
        return ressourcesDiffusables;
    }

    private void ajouterRessource(RessourceDiffusable ressourceDiffusable) {
        this.ressourcesDiffusablesComplet.add(ressourceDiffusable);
    }

    private void verifValidite() {
        if (this.dateTelechargement == null || SECONDS.between(this.dateTelechargement, LocalDateTime.now()) > 86400) { // 24h.
            this.telechargerFichier();
            this.lireFichier();
        }
    }

    private void telechargerFichier() {
        // Début de téléchargement.
        log.info("Mediacentre file download: Starting download procedure");
        log.debug("Mediacentre file download: URL is {}", this.ressourcesDiffusablesUri);

        try {
            URL website = new URL(this.ressourcesDiffusablesUri);

            ReadableByteChannel rbc = Channels.newChannel(website.openStream());

            FileOutputStream fos = new FileOutputStream("src/main/resources/downloads/mediacentre.json");

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            this.ressourcesDiffusablesFile = new File("src/main/resources/downloads/mediacentre.json");
            this.dateTelechargement = LocalDateTime.now();
        } catch (MalformedURLException malformedURLException) {
            log.error("Mediacentre file download: malformed URL exception");
            malformedURLException.printStackTrace();
        } catch (IOException ioException) {
            log.error("Mediacentre file download: IO exception");
            ioException.printStackTrace();
        }

        // Suppression de l'historique.
        this.historiqueRequetes.clear();

        // Fin de téléchargement
        log.info("Mediacentre file download: Mediacentre file successfully downloaded!");
    }

    private void lireFichier() {
        log.debug("Reading of Mediacentre file: Starting reading procedure");
        log.debug("Reading of Mediacentre file: file is at {}", this.ressourcesDiffusablesFile.getAbsolutePath());

        try {
            // Lecture du JSON.
            JsonNode jsonNode = objectMapper.readTree(this.ressourcesDiffusablesFile);

            // Ouverture du singleton.
            jsonNode = jsonNode.get(0);

            // Récupération de la date de génération.
            LocalDateTime dateGeneration = LocalDateTime.parse(
                    jsonNode.get("dateGeneration").asText().replaceAll(" ", ""),
                    DateTimeFormatter.ISO_DATE_TIME
            );
            log.debug(
                    "Reading of Mediacentre file: Mediacentre file generation date: {}",
                    DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss").format(dateGeneration)
            );

            // Est-ce que cela est utile de le relire ?
            if (this.dateGeneration != null && dateGeneration.isEqual(this.dateGeneration)) {
                log.debug("Reading of Mediacentre file: The file that has been previously read and the file currently read are the same; stopping the reading procedure");
                return;
            } else {
                this.dateGeneration = dateGeneration;
            }

            // Ouverture de l'objet des ressources diffusables.
            jsonNode = jsonNode.get("ressourceDiffusable");

            // Création de la map qui sert au cas où des ID viennent sans les noms.
            Map<String, String> attributsPseudoCache = new HashMap<>();

            // Pour chaque objet (étonnamment, il y en a des fois plusieurs, souvent le deuxième est un objet vide).
            for (int i = 0; jsonNode.has(i); i++) {
                // Récupération du node JSON.
                JsonNode ressourceDiffusableJson = jsonNode.get(i);

                // Nom et ID de la ressource.
                String idRessource = ressourceDiffusableJson.get("idRessource").asText();
                JsonNode nomRessourceJson = ressourceDiffusableJson.get("nomRessource");
                if (nomRessourceJson != null) {
                    attributsPseudoCache.put(idRessource, nomRessourceJson.asText());
                }

                // Nom et ID de l'éditeur.
                String idEditeur = ressourceDiffusableJson.get("idEditeur").asText();
                JsonNode nomEditeurJson = ressourceDiffusableJson.get("nomEditeur");
                if (nomEditeurJson != null) {
                    attributsPseudoCache.put(idEditeur, nomEditeurJson.asText());
                }

                // Récupération du node JSON des distributeurs com.
                JsonNode distributeursComJson = ressourceDiffusableJson.get("distributeursCom");

                // Création de la liste des distributeurs com.
                List<String> listeIdDistributeurCom = new ArrayList<>();

                // Itération sur tous les distributeurs com.
                for (int j = 0; distributeursComJson.has(j); j++) {
                    // Nom et ID du distributeur com.
                    String idDistributeurCom = distributeursComJson.get(j).get("distributeurCom").asText();
                    JsonNode nomDistributeurComJson = distributeursComJson.get(j).get("nomDistributeurCom");
                    if (nomDistributeurComJson != null) {
                        attributsPseudoCache.put(idDistributeurCom, nomDistributeurComJson.asText());
                    }

                    // On ajoute le distributeur com à la liste des distributeurs com à ajouter.
                    listeIdDistributeurCom.add(idDistributeurCom);
                }

                // Nom et ID de l'éditeur.
                String idDistributeurTech = ressourceDiffusableJson.get("distributeurTech").asText();
                JsonNode nomDistributeurTech = ressourceDiffusableJson.get("nomDistributeurTech");
                if (nomDistributeurTech != null) {
                    attributsPseudoCache.put(idDistributeurTech, nomDistributeurTech.asText());
                }

                // Est-ce que la ressource est affichable ?
                boolean affichable = ressourceDiffusableJson.get("affichable").asBoolean();

                // Est-ce que la ressource est diffusable ?
                boolean diffusable = ressourceDiffusableJson.get("diffusable").asBoolean();

                // On crée les objets AttributRessources à ce moment là seulement car si le nom d'une des ressources est
                // indiquée dans la ressource suivante il faut d'abord lire cette suivante avant de générer la première.
                // Exemple :
                //     editeur: {"130006042_0000000106379136", "");
                //     distributeurTech: {"130006042_0000000106379136", "Agrosup Dijon"}
                // Il y a ce genre d'ambiguïté dans le fichier téléchargé.
                this.ajouterRessource(
                        new RessourceDiffusable(
                                new AttributRessource(
                                        idRessource,
                                        attributsPseudoCache.getOrDefault(idRessource, "")
                                ),
                                new AttributRessource(
                                        idEditeur,
                                        attributsPseudoCache.getOrDefault(idEditeur, "")
                                ),
                                listeIdDistributeurCom.stream().map(
                                        idDistributeurCom -> new AttributRessource(
                                                idDistributeurCom,
                                                attributsPseudoCache.getOrDefault(idDistributeurCom, "")
                                        )
                                ).collect(Collectors.toList()),
                                new AttributRessource(
                                        idDistributeurTech,
                                        attributsPseudoCache.getOrDefault(idDistributeurTech, "")
                                ),
                                affichable,
                                diffusable
                        )
                );
            }
        } catch (JsonProcessingException e) {
            log.error("Reading of Mediacentre file: JSON processing exception");
            e.printStackTrace();
        } catch (IOException ioException) {
            log.error("Reading of Mediacentre file: IO exception");
            ioException.printStackTrace();
        }

        // Suppression de l'historique.
        this.historiqueRequetes.clear();

        // Fin de lecture.
        log.debug("Reading of Mediacentre file: Mediacentre file successfully read!");
    }
}

package fr.anael.apimediacentre.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.anael.apimediacentre.model.RessourceDiffusable;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;

@Slf4j
public class MediacentreServiceHttpGet implements MediacentreService {
    // Attributs
    @Value("${mediacentre.ressources-diffusables-uri:}")
    private String ressourcesDiffusablesUri;

    private final List<RessourceDiffusable> ressourcesDiffusables = new ArrayList<>();

    private File ressourcesDiffusablesFile = null;

    private LocalDateTime dateGeneration = null;

    private LocalDateTime dateTelechargement = null;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Getteurs
    @Override
    public int getNombreDePages(int elementsParPage) {
        this.verifValidite();

        return (int) Math.ceil(this.ressourcesDiffusables.size() / (double) elementsParPage);
    }

    @Override
    public Collection<RessourceDiffusable> getRessourcesDiffusables(int page, int elementsParPage) {
        this.verifValidite();

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

    private void verifValidite() {
        if (this.dateTelechargement == null || SECONDS.between(this.dateTelechargement, LocalDateTime.now()) > 86400) { // 24h.
            this.telechargerFichier();
            this.lireFichier();
        }
    }

    private void telechargerFichier() {
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

        log.info("Mediacentre file download: Mediacentre file successfully downloaded!");
    }

    private void lireFichier() {
        log.debug("Reading of Mediacentre file: Starting reading procedure");
        log.debug("Reading of Mediacentre file: file is at {}", this.ressourcesDiffusablesFile.getAbsolutePath());

        try {
            JsonNode jsonNode = objectMapper.readTree(this.ressourcesDiffusablesFile);

            jsonNode = jsonNode.get(0);

            LocalDateTime dateGeneration = LocalDateTime.parse(
                    jsonNode.get("dateGeneration").asText().replaceAll(" ", ""),
                    DateTimeFormatter.ISO_DATE_TIME
            );
            log.debug(
                    "Reading of Mediacentre file: Mediacentre file generation date: {}",
                    DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss").format(dateGeneration)
            );

            if (this.dateGeneration != null && dateGeneration.isEqual(this.dateGeneration)) {
                log.debug("Reading of Mediacentre file: The file that has been previously read and the file currently read are the same; stopping the reading procedure");
                return;
            } else {
                this.dateGeneration = dateGeneration;
            }

            jsonNode = jsonNode.get("ressourceDiffusable");

            for (int i = 0; jsonNode.has(i); i++) {
                JsonNode ressourceDiffusableJson = jsonNode.get(i);
                String idRessource = ressourceDiffusableJson.get("idRessource").asText();
                String nomRessource = ressourceDiffusableJson.get("nomRessource").asText();
                String idEditeur = ressourceDiffusableJson.get("idEditeur").asText();
                JsonNode distributeursComJson = ressourceDiffusableJson.get("distributeursCom");
                List<String> distributeursCom = new ArrayList<>();
                for (int j = 0; distributeursComJson.has(j); j++) {
                    distributeursCom.add(distributeursComJson.get(j).get("distributeurCom").asText());
                }
                String distributeurTech = ressourceDiffusableJson.get("distributeurTech").asText();
                boolean affichable = ressourceDiffusableJson.get("affichable").asBoolean();
                boolean diffusable = ressourceDiffusableJson.get("diffusable").asBoolean();

                this.ajouterRessource(
                        new RessourceDiffusable(
                                idRessource,
                                nomRessource,
                                idEditeur,
                                distributeursCom,
                                distributeurTech,
                                affichable,
                                diffusable
                        )
                );
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        log.debug("Reading of Mediacentre file: Mediacentre file successfully read!");
    }
}

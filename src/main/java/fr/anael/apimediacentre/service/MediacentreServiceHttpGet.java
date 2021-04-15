package fr.anael.apimediacentre.service;

import fr.anael.apimediacentre.model.RessourceDiffusable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.ManagedBean;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class MediacentreServiceHttpGet implements MediacentreService {
    // Attributs
    private final String xmlUri = "http://vanneau.giprecia.net:8080/mediacentre-ws/api/ressourcesDiffusables";

    private final List<RessourceDiffusable> ressourcesDiffusables = new ArrayList<>();

    // Constructeurs
    public MediacentreServiceHttpGet() {
        this.telechargerXml();
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

    private void telechargerXml() {
        log.info("XML file download: Starting download procedure");
        log.debug("XML file download: URL is {}", this.xmlUri);

        try {
            URL siteWeb = new URL(this.xmlUri);

            ReadableByteChannel rbc = Channels.newChannel(siteWeb.openStream());

            FileOutputStream fos = new FileOutputStream("./src/main/resources/downloads/mediacentre.xml");

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

        } catch (MalformedURLException malformedURLException) {
            log.error("XML file download: malformed URL exception");
            malformedURLException.printStackTrace();
        } catch (IOException ioException) {
            log.error("XML file download: IO exception");
            ioException.printStackTrace();
        }

        log.info("XML file download: XML file successfully downloaded!");
    }
}

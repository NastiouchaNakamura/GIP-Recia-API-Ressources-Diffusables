package fr.anael.apimediacentre.controller;

import fr.anael.apimediacentre.model.RessourceDiffusable;
import fr.anael.apimediacentre.service.MediacentreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "api/")
public class ApiMediacentreController {
    // Attributs
    @Autowired
    private MediacentreService mediacentreService;

    // Méthodes
    @GetMapping(value = "/ressources-diffusables")
    public Collection<RessourceDiffusable> ressourcesDiffusables(
            @RequestParam(
                    value = "page", defaultValue = "0"
            ) final int page,
            @RequestParam(
                    value = "elementsParPage", defaultValue = "32"
            ) final int elementsParPage,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.debug("Ressources diffusables request. page: {}, elementsParPage: {}.", page, elementsParPage);
        return this.mediacentreService.getRessourcesDiffusables(page, elementsParPage);
    }

    @GetMapping(value = "/ressources-diffusables/pages")
    public int nombrePages(
            @RequestParam(
                    value = "elementsParPage", defaultValue = "32"
            ) final int elementsParPage,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.debug("Nombre pages ressources diffusables request.");
        return this.mediacentreService.getNombreDePages(elementsParPage);
    }
}

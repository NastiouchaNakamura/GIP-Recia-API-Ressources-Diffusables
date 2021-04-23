package fr.anael.apimediacentre.controller;

import fr.anael.apimediacentre.model.RessourceDiffusable;
import fr.anael.apimediacentre.model.RessourceDiffusableFilter;
import fr.anael.apimediacentre.service.MediacentreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
            @RequestParam(value = "page") final int page,
            @RequestParam(value = "idRessource", required = false) final String idRessource,
            @RequestParam(value = "nomRessource", required = false)  final String nomRessource,
            @RequestParam(value = "idEditeur", required = false) final String idEditeur,
            @RequestParam(value = "distributeurCom", required = false) final String distributeurCom,
            @RequestParam(value = "distributeurTech", required = false) final String distributeurTech,
            @RequestParam(value = "affichable", required = false) final Boolean affichable,
            @RequestParam(value = "diffusable", required = false) final Boolean diffusable,
            @RequestParam(value = "elementsParPage", defaultValue = "32") final int elementsParPage,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.debug(
                "Ressources diffusables request. page: {}, elementsParPage: {}.",
                page,
                elementsParPage
        );
        return this.mediacentreService.getRessourcesDiffusables(
                page,
                elementsParPage,
                new RessourceDiffusableFilter(
                    idRessource,
                    nomRessource,
                    idEditeur,
                    distributeurCom,
                    distributeurTech,
                    affichable,
                    diffusable
                )
        );
    }

    @GetMapping(value = "/ressources-diffusables/page-count")
    public int pageCount(
            @RequestParam(value = "idRessource", required = false) final String idRessource,
            @RequestParam(value = "nomRessource", required = false)  final String nomRessource,
            @RequestParam(value = "idEditeur", required = false) final String idEditeur,
            @RequestParam(value = "distributeurCom", required = false) final String distributeurCom,
            @RequestParam(value = "distributeurTech", required = false) final String distributeurTech,
            @RequestParam(value = "affichable", required = false) final Boolean affichable,
            @RequestParam(value = "diffusable", required = false) final Boolean diffusable,
            @RequestParam(value = "elementsParPage", defaultValue = "32") final int elementsParPage,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.debug("Nombre pages ressources diffusables request.");
        return this.mediacentreService.getPageCount(
                elementsParPage,
                new RessourceDiffusableFilter(
                        idRessource,
                        nomRessource,
                        idEditeur,
                        distributeurCom,
                        distributeurTech,
                        affichable,
                        diffusable
                )
        );
    }

    @GetMapping(value = "/ressources-diffusables/size")
    public int size(
            @RequestParam(value = "idRessource", required = false) final String idRessource,
            @RequestParam(value = "nomRessource", required = false)  final String nomRessource,
            @RequestParam(value = "idEditeur", required = false) final String idEditeur,
            @RequestParam(value = "distributeurCom", required = false) final String distributeurCom,
            @RequestParam(value = "distributeurTech", required = false) final String distributeurTech,
            @RequestParam(value = "affichable", required = false) final Boolean affichable,
            @RequestParam(value = "diffusable", required = false) final Boolean diffusable,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.debug("Nombre pages ressources diffusables request.");
        return this.mediacentreService.getSize(
                new RessourceDiffusableFilter(
                        idRessource,
                        nomRessource,
                        idEditeur,
                        distributeurCom,
                        distributeurTech,
                        affichable,
                        diffusable
                )
        );
    }
}

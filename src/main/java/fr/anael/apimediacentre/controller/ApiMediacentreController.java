package fr.anael.apimediacentre.controller;

import fr.anael.apimediacentre.model.RessourceDiffusable;
import fr.anael.apimediacentre.model.RessourceDiffusableFilter;
import fr.anael.apimediacentre.service.gar.ServiceGar;
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
    private ServiceGar serviceGar;

    // Méthodes
    @GetMapping(value = "/ressources-diffusables")
    public Collection<RessourceDiffusable> ressourcesDiffusables(
            @RequestParam(value = "page") final int page,
            @RequestParam(value = "operator", required = false) final String operator,
            @RequestParam(value = "idRessource", required = false) final String idRessource,
            @RequestParam(value = "nomRessource", required = false)  final String nomRessource,
            @RequestParam(value = "idEditeur", required = false) final String idEditeur,
            @RequestParam(value = "nomEditeur", required = false)  final String nomEditeur,
            @RequestParam(value = "distributeurCom", required = false) final String distributeurCom,
            @RequestParam(value = "nomDistributeurCom", required = false)  final String nomDistributeurCom,
            @RequestParam(value = "distributeurTech", required = false) final String distributeurTech,
            @RequestParam(value = "nomDistributeurTech", required = false)  final String nomDistributeurTech,
            @RequestParam(value = "affichable", required = false) final Boolean affichable,
            @RequestParam(value = "diffusable", required = false) final Boolean diffusable,
            @RequestParam(value = "ressourcesPerPage", defaultValue = "32") final int elementsParPage,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return this.serviceGar.getRessourcesDiffusables(
                page,
                elementsParPage,
                new RessourceDiffusableFilter(
                        operator,
                        idRessource,
                        nomRessource,
                        idEditeur,
                        nomEditeur,
                        distributeurCom,
                        nomDistributeurCom,
                        distributeurTech,
                        nomDistributeurTech,
                        affichable,
                        diffusable
                )
        );
    }

    @GetMapping(value = "/ressources-diffusables/page-count")
    public int pageCount(
            @RequestParam(value = "operator", required = false) final String operator,
            @RequestParam(value = "idRessource", required = false) final String idRessource,
            @RequestParam(value = "nomRessource", required = false)  final String nomRessource,
            @RequestParam(value = "idEditeur", required = false) final String idEditeur,
            @RequestParam(value = "nomEditeur", required = false)  final String nomEditeur,
            @RequestParam(value = "distributeurCom", required = false) final String distributeurCom,
            @RequestParam(value = "nomDistributeurCom", required = false)  final String nomDistributeurCom,
            @RequestParam(value = "distributeurTech", required = false) final String distributeurTech,
            @RequestParam(value = "nomDistributeurTech", required = false)  final String nomDistributeurTech,
            @RequestParam(value = "affichable", required = false) final Boolean affichable,
            @RequestParam(value = "diffusable", required = false) final Boolean diffusable,
            @RequestParam(value = "ressourcesPerPage", defaultValue = "32") final int elementsParPage,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.debug("Nombre pages ressources diffusables request.");
        return this.serviceGar.getPageCount(
                elementsParPage,
                new RessourceDiffusableFilter(
                        operator,
                        idRessource,
                        nomRessource,
                        idEditeur,
                        nomEditeur,
                        distributeurCom,
                        nomDistributeurCom,
                        distributeurTech,
                        nomDistributeurTech,
                        affichable,
                        diffusable
                )
        );
    }

    @GetMapping(value = "/ressources-diffusables/size")
    public int size(
            @RequestParam(value = "operator", required = false) final String operator,
            @RequestParam(value = "idRessource", required = false) final String idRessource,
            @RequestParam(value = "nomRessource", required = false)  final String nomRessource,
            @RequestParam(value = "idEditeur", required = false) final String idEditeur,
            @RequestParam(value = "nomEditeur", required = false)  final String nomEditeur,
            @RequestParam(value = "distributeurCom", required = false) final String distributeurCom,
            @RequestParam(value = "nomDistributeurCom", required = false)  final String nomDistributeurCom,
            @RequestParam(value = "distributeurTech", required = false) final String distributeurTech,
            @RequestParam(value = "nomDistributeurTech", required = false)  final String nomDistributeurTech,
            @RequestParam(value = "affichable", required = false) final Boolean affichable,
            @RequestParam(value = "diffusable", required = false) final Boolean diffusable,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.debug("Nombre pages ressources diffusables request.");
        return this.serviceGar.getSize(
                new RessourceDiffusableFilter(
                        operator,
                        idRessource,
                        nomRessource,
                        idEditeur,
                        nomEditeur,
                        distributeurCom,
                        nomDistributeurCom,
                        distributeurTech,
                        nomDistributeurTech,
                        affichable,
                        diffusable
                )
        );
    }
}

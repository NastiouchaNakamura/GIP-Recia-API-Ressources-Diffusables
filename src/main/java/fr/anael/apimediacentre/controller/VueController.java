package fr.anael.apimediacentre.controller;

import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VueController {
    // Méthodes
    @GetMapping(value = "/")
    public String vueFrontEnd(HttpServletRequest request, HttpServletResponse response) {
        return "index.html";
    }
}

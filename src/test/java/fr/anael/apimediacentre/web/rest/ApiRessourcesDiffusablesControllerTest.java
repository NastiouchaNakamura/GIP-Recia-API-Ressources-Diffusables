package fr.anael.apimediacentre.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import fr.anael.apimediacentre.service.gar.ServiceGar;
import fr.anael.apimediacentre.test.TestUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
@Slf4j
@SpringBootTest
public class ApiRessourcesDiffusablesControllerTest {
    // Attributs
    @Autowired
    private ServiceGar serviceGar;

    private MockMvc mockListRessourcesMvc;

    // Méthodes
    @PostConstruct
    public void setup() {
        MockitoAnnotations.openMocks(this);

        ApiRessourcesDiffusablesController apiRessourcesDiffusablesController = new ApiRessourcesDiffusablesController();

        ReflectionTestUtils.setField(apiRessourcesDiffusablesController, "serviceGar", this.serviceGar);

        this.mockListRessourcesMvc = MockMvcBuilders.standaloneSetup(apiRessourcesDiffusablesController).build();
    }

    @Test
    public void testErreurRecup() throws Exception {
        Map<String,List<String>> userInfos = new HashMap<>();
        userInfos.put("uid",  Lists.newArrayList("erreur"));
        userInfos.put("ESCOUAI", Lists.newArrayList("NOTEXIST"));
        userInfos.put("ESCOUAICourant", Lists.newArrayList("NOTEXIST"));
        userInfos.put("ENTPersonProfils", Lists.newArrayList("National_ENS"));
        userInfos.put("ENTPersonGARIdentifiant", Lists.newArrayList("NOTEXIST"));
        log.debug("User tested {}", userInfos);

        this.mockListRessourcesMvc.perform(post("/api/ressources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userInfos))
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(jsonPath("$", Matchers.hasKey("Erreur")))
                .andExpect(jsonPath("$.Erreur", Matchers.hasKey("Code")))
                .andExpect(jsonPath("$.Erreur.Code", Matchers.equalToIgnoringCase(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.Erreur", Matchers.hasKey("Message")))
                .andExpect(jsonPath("$.Erreur.Message", Matchers.notNullValue()))
                .andExpect(jsonPath("$.Erreur", Matchers.hasKey("Resource")))
        ;
    }

    @Test
    public void testJsonApiRessourcesDiffusables() throws Exception {
        this.mockListRessourcesMvc.perform(get("/api/ressources-diffusables?page=0")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andDo(MockMvcResultHandlers.print())

                // Status.
                .andExpect(status().isOk())

                // Content-type.
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))

                // Encoding.
                .andExpect(content().encoding("UTF-8"))

                // JSON Analysis.
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.not(Matchers.emptyArray())))
                .andExpect(jsonPath("$.[0]", Matchers.hasKey("ressource")))
                .andExpect(jsonPath("$.[0].ressource").isMap())
                .andExpect(jsonPath("$.[0].ressource").isNotEmpty())
                .andExpect(jsonPath("$.[0].ressource", Matchers.hasKey("id")))
                .andExpect(jsonPath("$.[0].ressource.id").isString())
                .andExpect(jsonPath("$.[0].ressource.id").isNotEmpty())
                .andExpect(jsonPath("$.[0].ressource", Matchers.hasKey("nom")))
                .andExpect(jsonPath("$.[0].ressource.nom").isString())
                .andExpect(jsonPath("$.[0]", Matchers.hasKey("editeur")))
                .andExpect(jsonPath("$.[0].editeur").isMap())
                .andExpect(jsonPath("$.[0].editeur").isNotEmpty())
                .andExpect(jsonPath("$.[0].editeur", Matchers.hasKey("id")))
                .andExpect(jsonPath("$.[0].editeur.id").isString())
                .andExpect(jsonPath("$.[0].editeur.id").isNotEmpty())
                .andExpect(jsonPath("$.[0].editeur", Matchers.hasKey("nom")))
                .andExpect(jsonPath("$.[0].editeur.nom").isString())
                .andExpect(jsonPath("$.[0]", Matchers.hasKey("distributeursCom")))
                .andExpect(jsonPath("$.[0].distributeursCom").isArray())
                .andExpect(jsonPath("$.[0].distributeursCom").isNotEmpty())
                .andExpect(jsonPath("$.[0].distributeursCom.[0]").isMap())
                .andExpect(jsonPath("$.[0].distributeursCom.[0]", Matchers.hasKey("id")))
                .andExpect(jsonPath("$.[0].distributeursCom.[0].id").isString())
                .andExpect(jsonPath("$.[0].distributeursCom.[0].id").isNotEmpty())
                .andExpect(jsonPath("$.[0].distributeursCom.[0]", Matchers.hasKey("nom")))
                .andExpect(jsonPath("$.[0].distributeursCom.[0].nom").isString())
                .andExpect(jsonPath("$.[0]", Matchers.hasKey("distributeurTech")))
                .andExpect(jsonPath("$.[0].distributeurTech").isMap())
                .andExpect(jsonPath("$.[0].distributeurTech").isNotEmpty())
                .andExpect(jsonPath("$.[0].distributeurTech", Matchers.hasKey("id")))
                .andExpect(jsonPath("$.[0].distributeurTech.id").isString())
                .andExpect(jsonPath("$.[0].distributeurTech.id").isNotEmpty())
                .andExpect(jsonPath("$.[0].distributeurTech", Matchers.hasKey("nom")))
                .andExpect(jsonPath("$.[0].distributeurTech.nom").isString())
                .andExpect(jsonPath("$.[0]", Matchers.hasKey("affichable")))
                .andExpect(jsonPath("$.[0].affichable").isBoolean())
                .andExpect(jsonPath("$.[0]", Matchers.hasKey("diffusable")))
                .andExpect(jsonPath("$.[0].diffusable").isBoolean());
    }

    @Test
    public void testJsonApiRessourcesDiffusablesPageCount() throws Exception {
        this.mockListRessourcesMvc.perform(get("/api/ressources-diffusables/page-count")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andDo(MockMvcResultHandlers.print())

                // Status.
                .andExpect(status().isOk())

                // Content-type.
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))

                // Encoding.
                .andExpect(content().encoding("UTF-8"))

                // JSON Analysis.
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    public void testJsonApiRessourcesDiffusablesSize() throws Exception {
        this.mockListRessourcesMvc.perform(get("/api/ressources-diffusables/size")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andDo(MockMvcResultHandlers.print())

                // Status.
                .andExpect(status().isOk())

                // Content-type.
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))

                // Encoding.
                .andExpect(content().encoding("UTF-8"))

                // JSON Analysis.
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$").isNumber());
    }
}

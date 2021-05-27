package fr.anael.apimediacentre.web.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import fr.anael.apimediacentre.service.gar.ServiceGar;
import fr.anael.apimediacentre.test.TestUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIn;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Autowired
    private Environment environment;

    @Value("${service-gar-http-get.ressources-diffusables-uri:}")
    private String ressourcesDiffusablesUri;

    private MockMvc mockListRessourcesMvc;

    // Méthodes
    @PostConstruct
    public void setup() {
        MockitoAnnotations.openMocks(this);

        ApiRessourcesDiffusablesController apiRessourcesDiffusablesController = new ApiRessourcesDiffusablesController();

        ReflectionTestUtils.setField(apiRessourcesDiffusablesController, "serviceGar", this.serviceGar);

        this.mockListRessourcesMvc = MockMvcBuilders.standaloneSetup(apiRessourcesDiffusablesController).build();
    }

    /*
    private MockedRequestServiceImpl getMockedService() {
        for (IRemoteRequestService rrs : remoteServices) {
            if (rrs instanceof MockedRequestServiceImpl ) {
                log.debug("Using MockedRequestServiceImpl !");
                return (MockedRequestServiceImpl) rrs;
            }
        }
        return null;
    }

    @Test
    public void testAuthorizedUsersConf() {
        if (!isWithoutGAR) {
            assertThat(garClientConfiguration.getGARProperties().getAuthorizedUsers(), Matchers.not(Matchers.nullValue()));
            assertThat(garClientConfiguration.getGARProperties().getAuthorizedUsers().getOperator(), Matchers.not(Matchers.nullValue()));
            assertThat(garClientConfiguration.getGARProperties().getAuthorizedUsers().getFiltreDroitType(), Matchers.not(Matchers.emptyIterable()));
        }
    }
     */

    /*
    @Test
    public void testRecuperation() throws Exception {
        Map<String,List<String>> userInfos = new HashMap<>();
        if (isWithoutGAR) {
            userInfos.put("uid",  Lists.newArrayList("F01000ugr"));
            userInfos.put("ESCOUAI", Lists.newArrayList("0450822X","0377777U"));
            userInfos.put("ESCOUAICourant", Lists.newArrayList("0450822X"));
            userInfos.put("ENTPersonProfils", Lists.newArrayList("National_ENS"));

            this.mockListRessourcesMvc.perform(post("/api/ressources")
                    .contentType(TestUtil.APPLICATION_JSON_UTF8)
                    .content(TestUtil.convertObjectToJsonBytes(userInfos))
                    .characterEncoding("UTF-8"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    //.andExpect(content().encoding("UTF-8"))
                    .andExpect(jsonPath("$.*", Matchers.not(Matchers.emptyArray())))
                    .andExpect(jsonPath("$.[0]", Matchers.hasKey("idRessource")))
                    .andExpect(jsonPath("$.[0]", Matchers.hasKey("idEtablissement")))
                    .andExpect(jsonPath("$.[0].idEtablissement", Matchers.hasSize(1)))
                    .andExpect(jsonPath("$.[0].idEtablissement.[0]", Matchers.hasKey("UAI")))
                    .andExpect(jsonPath("$.[0].idEtablissement.[0].UAI").value(IsIn.in(Lists.newArrayList("0450822X", "0377777U"))));
        } else {
//            userinfos are setted from configuration file
//            userInfos.put("uid",  Lists.newArrayList("F1600m19"));
//            userInfos.put("ESCOUAI", Lists.newArrayList("0291595B"));
//            userInfos.put("ESCOUAICourant", Lists.newArrayList("0291595B"));
//            userInfos.put("ENTPersonProfils", Lists.newArrayList("National_ELV"));

            Map<String,List<String>> configUser =  garClientConfiguration.getGARProperties().getTestUser();

            assertThat(configUser, notNullValue());
            assertThat(configUser.keySet(), hasItem("uid"));
            assertThat(configUser.keySet(), hasItem("ESCOUAI"));
            assertThat(configUser.keySet(), hasItem("ESCOUAICourant"));
            assertThat(configUser.keySet(), hasItem("ENTPersonProfils"));
            assertThat(configUser.keySet(), hasItem("ENTPersonGARIdentifiant"));

            this.mockListRessourcesMvc.perform(post("/api/ressources")
                    .contentType(TestUtil.APPLICATION_JSON_UTF8)
                    .content(TestUtil.convertObjectToJsonBytes(configUser))
                    .accept(TestUtil.APPLICATION_JSON_UTF8))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                    .andExpect(content().encoding("UTF-8"))
                    .andExpect(jsonPath("$.*", Matchers.notNullValue()))
                    .andExpect(jsonPath("$.[0]", Matchers.hasKey("idRessource")))
                    .andExpect(jsonPath("$.[0]", Matchers.hasKey("idEtablissement")))
                    .andExpect(jsonPath("$.[0].idEtablissement", Matchers.hasSize(1)))
                    .andExpect(jsonPath("$.[0].idEtablissement.[0]", Matchers.hasKey("UAI")));
        }
    }
     */

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

    /*
    @Test
    public void testErreurUnAuthorized() throws Exception {
        Map<String,List<String>> userInfos = new HashMap<>();
        userInfos.put("uid",  Lists.newArrayList("F01000ugr"));
        userInfos.put("ESCOUAI", Lists.newArrayList("0450822X","0377777U"));
        userInfos.put("ESCOUAICourant", Lists.newArrayList("0450822X"));
        userInfos.put("ENTPersonProfils", Lists.newArrayList("National_EVS"));
        userInfos.put("ENTPersonGARIdentifiant", Lists.newArrayList("NOTEXIST"));
        log.debug("User tested {}", userInfos);

        mockListRessourcesMvc.perform(post("/api/ressources")
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
                .andExpect(jsonPath("$.Erreur.Message", Matchers.equalTo(GARRequestServiceImpl.UN_AUTHORIZED_MESSAGE)))
                .andExpect(jsonPath("$.Erreur", Matchers.hasKey("Resource")))
        ;
    }
     */

    @Test
    public void testJsonGetFromGarRessourcesDiffusables() throws Exception {
        this.mockListRessourcesMvc.perform(get(this.ressourcesDiffusablesUri)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.not(Matchers.emptyArray())))
                .andExpect(jsonPath("$.[0]", Matchers.hasKey("dateGeneration")))
                .andExpect(jsonPath("$.[0]", Matchers.hasKey("ressourceDiffusable")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable").isArray())
                .andExpect(jsonPath("$.[0].ressourceDiffusable").isNotEmpty())
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0]", Matchers.hasKey("idRessource")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0]", Matchers.hasKey("nomRessource")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0]", Matchers.hasKey("idEditeur")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0]", Matchers.hasKey("nomEditeur")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0]", Matchers.hasKey("distributeursCom")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0].distributeursCom").isArray())
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0].distributeursCom").isNotEmpty())
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0].distributeursCom.[0]", Matchers.hasKey("distributeurCom")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0].distributeursCom.[0]", Matchers.hasKey("nomDistributeurCom")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0]", Matchers.hasKey("distributeurTech")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0]", Matchers.hasKey("nomDistributeurTech")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0]", Matchers.hasKey("affichable")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0]", Matchers.hasKey("diffusable")));
    }

    @Test
    public void testJsonApiRessourcesDiffusables() throws Exception {
        this.mockListRessourcesMvc.perform(get("/api/ressources-diffusables?page=0")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.not(Matchers.emptyArray())))
                .andExpect(jsonPath("$.[0]", Matchers.hasKey("dateGeneration")))
                .andExpect(jsonPath("$.[0]", Matchers.hasKey("ressourceDiffusable")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable").isArray())
                .andExpect(jsonPath("$.[0].ressourceDiffusable").isNotEmpty())
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0]", Matchers.hasKey("idRessource")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0]", Matchers.hasKey("nomRessource")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0]", Matchers.hasKey("idEditeur")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0]", Matchers.hasKey("nomEditeur")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0]", Matchers.hasKey("distributeursCom")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0].distributeursCom").isArray())
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0].distributeursCom").isNotEmpty())
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0].distributeursCom.[0]", Matchers.hasKey("distributeurCom")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0].distributeursCom.[0]", Matchers.hasKey("nomDistributeurCom")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0]", Matchers.hasKey("distributeurTech")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0]", Matchers.hasKey("nomDistributeurTech")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0]", Matchers.hasKey("affichable")))
                .andExpect(jsonPath("$.[0].ressourceDiffusable.[0]", Matchers.hasKey("diffusable")));
    }
}

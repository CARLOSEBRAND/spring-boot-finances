package br.pro.brand.finance.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.pro.brand.finance.api.dto.UserDTO;
import br.pro.brand.finance.exceptions.AutenticationException;
import br.pro.brand.finance.exceptions.BussinessRuleException;
import br.pro.brand.finance.models.entity.User;
import br.pro.brand.finance.services.EntryService;
import br.pro.brand.finance.services.UserService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    static final String API = "/api/users";
    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService service;

    @MockBean
    EntryService entryService;

    @Test
    public void checkUserAuthenticate() throws Exception {

        //cenário

        String email = "carlosebrand@gmail.com";
        String password = "1234";

        UserDTO dto = new UserDTO();
        dto.setEmail(email);
        dto.setPassword(password);

        User user = new User();
        user.setId(1l);
        user.setEmail(email);
        user.setPassword(password);

        Mockito.when(service.autenticate(email, password)).thenReturn(user);

        String json = new ObjectMapper().writeValueAsString(dto);

        //execução e verificação
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                                    .post( API.concat("/auth"))
                                                    .accept(JSON)
                                                    .contentType(JSON)
                                                    .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("id").value(user.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("name").value(user.getName()))
            .andExpect(MockMvcResultMatchers.jsonPath("email").value(user.getEmail()));

    }

    @Test
    public void checkUserAuthenticateFail() throws Exception {

        //cenário

        String email = "carlosebrand@gmail.com";
        String password = "1234";

        UserDTO dto = new UserDTO();
        dto.setEmail(email);
        dto.setPassword(password);

        Mockito.when(service.autenticate(email, password)).thenThrow(AutenticationException.class);

        String json = new ObjectMapper().writeValueAsString(dto);

        //execução e verificação
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                                    .post( API.concat("/auth"))
                                                    .accept(JSON)
                                                    .contentType(JSON)
                                                    .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void checkSavingNewUser() throws Exception {

        //cenário

        String email = "carlosebrand@gmail.com";
        String password = "1234";

        UserDTO dto = new UserDTO();
        dto.setEmail(email);
        dto.setPassword(password);

        User user = new User();
        user.setId(1l);
        user.setEmail(email);
        user.setPassword(password);

        Mockito.when(service.salveUser(Mockito.any(User.class))).thenReturn(user);

        String json = new ObjectMapper().writeValueAsString(dto);

        //execução e verificação
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                                    .post( API.concat("/save"))
                                                    .accept(JSON)
                                                    .contentType(JSON)
                                                    .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("id").value(user.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("name").value(user.getName()))
            .andExpect(MockMvcResultMatchers.jsonPath("email").value(user.getEmail()));

    }

    @Test
    public void checkSavingNewUserFail() throws Exception {

        //cenário

        String email = "carlosebrand@gmail.com";
        String password = "1234";

        UserDTO dto = new UserDTO();
        dto.setEmail(email);
        dto.setPassword(password);

        Mockito.when(service.salveUser(Mockito.any(User.class))).thenThrow(BussinessRuleException.class);

        String json = new ObjectMapper().writeValueAsString(dto);

        //execução e verificação
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                                    .post( API.concat("/save"))
                                                    .accept(JSON)
                                                    .contentType(JSON)
                                                    .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
    
}

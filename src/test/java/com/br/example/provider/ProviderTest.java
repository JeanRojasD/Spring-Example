package com.br.example.provider;

import com.br.example.TesteBase;
import com.br.example.dto.form.ProviderForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProviderTest extends TesteBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void verify_WhenRequestPost() throws Exception{
        ProviderForm providerForm = ProviderForm.builder()
                .name("Example provider")
                .cnpj("87.544.769/0001-59")
                .build();

        mockMvc.perform(post("/providers").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(providerForm)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void verify_WhenCnpjIsInvalid() throws Exception{
        ProviderForm providerForm = ProviderForm.builder()
                .name("Example provider")
                .cnpj("564654654654")
                .build();

        mockMvc.perform(post("/providers").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(providerForm)))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

}

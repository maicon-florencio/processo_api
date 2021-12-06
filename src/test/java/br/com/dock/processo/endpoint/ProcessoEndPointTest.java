package br.com.dock.processo.endpoint;

import br.com.dock.processo.dto.ProcessoDto;
import br.com.dock.processo.model.Processo;
import br.com.dock.processo.service.ProcessoServiceI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
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

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class ProcessoEndPointTest {

    public static String API_ROUTE= "/v1/processos";

    @Autowired
    MockMvc mock;

    @MockBean
    ProcessoServiceI processService;

    @Test
    @DisplayName("Nao deve criar um processo sem dados suficiente")
    public void createProcessoInvalidoTest() throws Exception {

        String jsonTest = new ObjectMapper().writeValueAsString(new ProcessoDto());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API_ROUTE)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonTest);

        mock.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(3)));

    }


    @Test
    @DisplayName("deve obter informações de um process")
    public void getProcessDetailsTest() throws Exception {
        Long id = 1l;
        var process =
                Processo.builder().idProcess(id).
                        logic(createNewProcessDto().getLogic()).serial(createNewProcessDto().getSerial()).
                        model(createNewProcessDto().getModel()).sam(createNewProcessDto().getSam())
                        .ptid(createNewProcessDto().getPtid()).plat(createNewProcessDto().getPlat())
                        .version(createNewProcessDto().getVersion()).mxr(createNewProcessDto().getMxr())
                        .mxf(createNewProcessDto().getMxf()).pverfm(createNewProcessDto().getPverfm()).build();

        BDDMockito.given(processService.getByid(id)).willReturn(Optional.of(process));

        var request = MockMvcRequestBuilders.get(API_ROUTE.concat("/"+ id)).accept(MediaType.APPLICATION_JSON);

        mock.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("idProcess").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("logic").value(createNewProcessDto().getLogic()))
                .andExpect(MockMvcResultMatchers.jsonPath("serial").value(createNewProcessDto().getSerial()))
                .andExpect(MockMvcResultMatchers.jsonPath("model").value(createNewProcessDto().getModel()))
                .andExpect(MockMvcResultMatchers.jsonPath("sam"  ).value(createNewProcessDto().getSam()))
                .andExpect(MockMvcResultMatchers.jsonPath("ptid" ).value(createNewProcessDto().getPtid()))
                .andExpect(MockMvcResultMatchers.jsonPath("plat" ).value(createNewProcessDto().getPlat()))
                .andExpect(MockMvcResultMatchers.jsonPath("version"  ).value(createNewProcessDto().getVersion()))
                .andExpect(MockMvcResultMatchers.jsonPath("mxr"  ).value(createNewProcessDto().getMxr()))
                .andExpect(MockMvcResultMatchers.jsonPath("mxf"  ).value(createNewProcessDto().getMxf()))
                .andExpect(MockMvcResultMatchers.jsonPath("pverfm").value(createNewProcessDto().getPverfm()));


    }

    @Test
    @DisplayName("deve retorna resource not found quando processo procurado não existir")
    public void processDontFound() throws Exception {

        BDDMockito.given(processService.getByid(Mockito.anyLong())).willReturn(Optional.empty());

        var request = MockMvcRequestBuilders.get(API_ROUTE.concat("/"+ 1)).accept(MediaType.APPLICATION_JSON);

        mock.perform(request).
                andExpect(MockMvcResultMatchers.status().isNotFound());


    }


    @Test
    @DisplayName("deve atualizar um process já existente")
    public void updateProcessTest()throws Exception{
        Long id=1l;
        String json =  new ObjectMapper().writeValueAsString(createNewProcessDto());

        var updatingProcess = Processo.builder().idProcess(1l).logic(1235546699).serial(435620)
                .model("MSF281121").sam(23).ptid("mon281111715").plat(3).version("00001.a")
                .mxr(34).mxf(21).pverfm("MSFMONDDEV").build();
        BDDMockito.given(processService.getByid(id)).willReturn(Optional.of(updatingProcess));

        var  updatedProcess = Processo.builder().idProcess(1l).logic(123554667).serial(43562)
                .model("MSF281121").sam(23).ptid("mon281111715u").plat(3).version("00001.a")
                .mxr(34).mxf(21).pverfm("MSFMONDDEVU").build();

        BDDMockito.given(processService.update(updatingProcess)).willReturn(updatedProcess);

        var request = MockMvcRequestBuilders.
                put(API_ROUTE.concat("/"+ id))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mock.perform(request).
                andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("idProcess").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("logic").value(createNewProcessDto().getLogic()))
                .andExpect(MockMvcResultMatchers.jsonPath("serial").value(createNewProcessDto().getSerial()))
                .andExpect(MockMvcResultMatchers.jsonPath("model").value(createNewProcessDto().getModel()))
                .andExpect(MockMvcResultMatchers.jsonPath("sam"  ).value(createNewProcessDto().getSam()))
                .andExpect(MockMvcResultMatchers.jsonPath("ptid" ).value(createNewProcessDto().getPtid()))
                .andExpect(MockMvcResultMatchers.jsonPath("plat" ).value(createNewProcessDto().getPlat()))
                .andExpect(MockMvcResultMatchers.jsonPath("version"  ).value(createNewProcessDto().getVersion()))
                .andExpect(MockMvcResultMatchers.jsonPath("mxr"  ).value(createNewProcessDto().getMxr()))
                .andExpect(MockMvcResultMatchers.jsonPath("mxf"  ).value(createNewProcessDto().getMxf()))
                .andExpect(MockMvcResultMatchers.jsonPath("pverfm").value(createNewProcessDto().getPverfm()));

    }

    @Test
    @DisplayName("deve retorna 404 ao tentar atualizar um processo inexistente")
    public void updateInexistentProcessTest()throws Exception{

        String json = new ObjectMapper().writeValueAsString(createNewProcessDto());

        BDDMockito.given(processService.getByid(Mockito.anyLong())).
                willReturn(Optional.empty());

        var request = MockMvcRequestBuilders.
                put(API_ROUTE.concat("/"+ "1")).accept(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mock.perform(request).
                andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    private ProcessoDto createNewProcessDto() {
        return new ProcessoDto().builder().idProcess(1l).logic(123554667).serial(43562)
                .model("MSF281121").sam(23).ptid("mon281111715u").plat(3).version("00001.a")
                .mxr(34).mxf(21).pverfm("MSFMONDDEVU").build();
    }



}

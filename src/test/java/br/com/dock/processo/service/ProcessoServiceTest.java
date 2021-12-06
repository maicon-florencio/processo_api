package br.com.dock.processo.service;

import br.com.dock.processo.dao.ProcessoDao;
import br.com.dock.processo.dto.ProcessoDto;
import br.com.dock.processo.exception.BusinessException;
import br.com.dock.processo.model.Processo;
import br.com.dock.processo.service.imple.ProcessoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ProcessoServiceTest {

    ProcessoServiceI processoServiceI;

    @MockBean
    ProcessoDao processoDao;


    @BeforeEach
    public void setUp(){
        this.processoServiceI = new ProcessoServiceImpl(processoDao);
    }

    @Test
    @DisplayName("Deve salvar um Processo")
    public void saveProcessTest(){
        var processo = createdValidProcesso();
        Mockito.when(processoDao.existByLogic(Mockito.anyInt())).thenReturn(false);

        Mockito.when(processoDao.save(processo)).
                thenReturn( Processo.builder().idProcess(1l).logic(123554667).serial(43562)
                        .model("MSF281121").sam(23).ptid("mon281111715u").plat(3).version("00001.a")
                        .mxr(34).mxf(21).pverfm("MSFMONDDEVU").build());

        var processSalvo = processoServiceI.save(processo);
        assertThat(processSalvo.getIdProcess()).isNotNull();
        assertThat(processSalvo.getLogic()).isEqualTo(123554667);
        assertThat(processSalvo.getSerial()).isEqualTo(43562);
        assertThat(processSalvo.getModel()).isEqualTo("MSF281121");
        assertThat(processSalvo.getSam()).isEqualTo(23);
        assertThat(processSalvo.getPverfm()).isEqualTo("MSFMONDDEVU");
        assertThat(processSalvo.getPtid()).isEqualTo("mon281111715u");
        assertThat(processSalvo.getPlat()).isEqualTo(3);
        assertThat(processSalvo.getVersion()).isEqualTo("00001.a");
        assertThat(processSalvo.getMxf()).isEqualTo(21);
        assertThat(processSalvo.getMxr()).isEqualTo(34);

    }

    Processo createdValidProcesso() {
        return new Processo().builder().idProcess(1l).logic(123554667).serial(43562)
                .model("MSF281121").sam(23).ptid("mon281111715u").plat(3).version("00001.a")
                .mxr(34).mxf(21).pverfm("MSFMONDDEVU").build();
    }


    @Test
    @DisplayName("deve lançar erro de negocio tentar salvar um livro com isbn duplicado")
    public void shouldNotSaveProcessoWithDuplicateLogic(){
        var processo = createdValidProcesso();

        Mockito.when(processoDao.existByLogic(Mockito.anyInt())).thenReturn(true);

        Throwable excepiton =  Assertions.catchThrowable(()-> processoServiceI.save(processo));
        assertThat(excepiton).isInstanceOf(BusinessException.class).hasMessage("Isbn já cadastrado");

        Mockito.verify(processoDao, Mockito.never()).save(processo);

    }

    @Test
    @DisplayName("Deve obter um livro por id")
    public void getByIdTest(){
        Long id =1l;

        var process = createdValidProcesso();
        process.setIdProcess(id);
        Mockito.when(processoDao.findById(id)).thenReturn(Optional.of(process));

        Optional<Processo>  foundProcess = processoServiceI.getByid(id);

        assertThat(foundProcess.isPresent()).isTrue();
        assertThat(foundProcess.get().getIdProcess()).isEqualTo(id);
        assertThat(foundProcess.get().getLogic()).isEqualTo(process.getLogic());
        assertThat(foundProcess.get().getSerial()).isEqualTo(process.getSerial());
        assertThat(foundProcess.get().getModel()).isEqualTo(process.getModel());
        assertThat(foundProcess.get().getSam()).isEqualTo(process.getSam());
        assertThat(foundProcess.get().getPverfm()).isEqualTo(process.getPverfm());
        assertThat(foundProcess.get().getPtid()).isEqualTo(process.getPtid());
        assertThat(foundProcess.get().getPlat()).isEqualTo(process.getPlat());
        assertThat(foundProcess.get().getVersion()).isEqualTo(process.getVersion());
        assertThat(foundProcess.get().getMxf()).isEqualTo(process.getMxf());
        assertThat(foundProcess.get().getMxr()).isEqualTo(process.getMxr());
    }

    @Test
    @DisplayName("Deve retorna vazio quando id for inexistente")
    public void bookNotFoundIdTest(){
        Long id =1l;

        Mockito.when(processoDao.findById(id)).thenReturn(Optional.empty());

        Optional<Processo>  processo = processoServiceI.getByid(id);

        assertThat(processo.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve obter atualizar um processo existente")
    public void updateBookFoundTest(){
        Long id=1l;
        //livro para atualizar
        var processUpdating = Processo.builder().idProcess(id).build();

        var processUpdated = createdValidProcesso();
        processUpdated.setIdProcess(id);


        Mockito.when(processoDao.save(processUpdating)).thenReturn(processUpdated);
        var process = processoServiceI.update(processUpdating);


        assertThat(book.getId()).isEqualTo(bookUpdated.getId());
        assertThat(book.getAuthor()).isEqualTo(bookUpdated.getAuthor());
        assertThat(book.getTitle()).isEqualTo(bookUpdated.getTitle());
        assertThat(book.getIsbn()).isEqualTo(bookUpdated.getIsbn());
    }

    @Test
    @DisplayName("deve deletar um livro")
    public void deleteBookTest(){

        var book = Book.builder().id(1l).build();

        org.junit.jupiter.api.Assertions.assertDoesNotThrow( ()-> bookService.delete(book));

        Mockito.verify(bookDao, Mockito.times(1)).delete(book);

    }

    @Test
    @DisplayName("deve ocorrer erro ao tentar deletar um livro inexistente")
    public void deleteInvalidBookTest(){

        var book = new Book();
        org.junit.jupiter.api.Assertions.assertThrows( IllegalArgumentException.class,()-> bookService.delete(book));


        Mockito.verify(bookDao, Mockito.never()).delete(book);

    }

    @Test
    @DisplayName("deve ocorrer erro ao tentar atualizar um livro inexistente")
    public void updateInvalidBookTest(){

        var book = new Book();
        org.junit.jupiter.api.Assertions.assertThrows( IllegalArgumentException.class,()-> bookService.update(book));


        Mockito.verify(bookDao, Mockito.never()).save(book);

    }

}

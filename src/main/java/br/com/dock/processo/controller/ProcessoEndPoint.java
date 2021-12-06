package br.com.dock.processo.controller;

import br.com.dock.processo.dto.ProcessoDto;
import br.com.dock.processo.erros.ApiErrors;
import br.com.dock.processo.exception.BusinessException;
import br.com.dock.processo.model.Processo;
import br.com.dock.processo.service.ProcessoServiceI;
import org.modelmapper.ModelMapper;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/processos")
public class ProcessoEndPoint {

    private ProcessoServiceI processoServiceI;

    private ModelMapper modelMapper;

    public ProcessoEndPoint(ProcessoServiceI processoServiceI,ModelMapper modelMapper) {
        this.processoServiceI = processoServiceI;
        this.modelMapper = modelMapper;
    }

    @GetMapping("{id}")
    public ProcessoDto get(@PathVariable Long id){
        return
                processoServiceI.
                        getByid(id).
                        map(process -> modelMapper.map(process, ProcessoDto.class))
                        .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @PutMapping("{id}")
    public ProcessoDto Update(@PathVariable Long id, ProcessoDto dto){
        var process = processoServiceI.getByid(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        process.setLogic(dto.getLogic());
        process.setPtid(dto.getPtid());
        process.setPverfm(dto.getPverfm());
        process = processoServiceI.update(process);
        return modelMapper.map( process, ProcessoDto.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProcessoDto save( @RequestBody @Valid ProcessoDto processDto) {
        var processNovo = modelMapper.map(processDto, Processo.class);
        processNovo = processoServiceI.save(processNovo);
        return modelMapper.map(processNovo, ProcessoDto.class);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErrors(bindingResult);

    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinessException(BusinessException ex) {
        return new ApiErrors(ex);

    }
}

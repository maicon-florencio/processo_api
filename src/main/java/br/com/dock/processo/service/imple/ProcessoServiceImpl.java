package br.com.dock.processo.service.imple;

import br.com.dock.processo.dao.ProcessoDao;
import br.com.dock.processo.model.Processo;
import br.com.dock.processo.service.ProcessoServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ProcessoServiceImpl implements ProcessoServiceI {


    private ProcessoDao processoDao;

    public ProcessoServiceImpl(ProcessoDao processoDao) {
        this.processoDao = processoDao;
    }

    @Override
    public Processo save(Processo processo) {
        return null;
    }

    @Override
    public Optional<Processo> getByid(Long id) {
        return Optional.empty();
    }

    @Override
    public Processo update(Processo processo) {
        return null;
    }
}

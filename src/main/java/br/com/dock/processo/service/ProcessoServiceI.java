package br.com.dock.processo.service;

import br.com.dock.processo.model.Processo;

import java.util.Optional;

public interface ProcessoServiceI {

    public Processo save(Processo processo);

    public Optional<Processo> getByid(Long id);

    public Processo update(Processo processo);
}

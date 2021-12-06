package br.com.dock.processo.dao;

import br.com.dock.processo.model.Processo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessoDao extends JpaRepository<Processo, Long> {

    public boolean existByLogic(int logic);

}

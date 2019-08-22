package com.prototipo.tcc.repositories;

import com.prototipo.tcc.domain.Configuracao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracaoRepository extends JpaRepository<Configuracao, Integer> {

}

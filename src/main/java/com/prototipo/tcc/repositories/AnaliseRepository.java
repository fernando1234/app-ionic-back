package com.prototipo.tcc.repositories;

import com.prototipo.tcc.domain.Analise;
import com.prototipo.tcc.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AnaliseRepository extends JpaRepository<Analise, Integer> {

//    @Transactional(readOnly = true)
//    Page<Analise> findByUsuario(Usuario usuario, Pageable pageRequest);

}

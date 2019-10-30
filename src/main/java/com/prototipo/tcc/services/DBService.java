package com.prototipo.tcc.services;

import com.prototipo.tcc.domain.Analise;
import com.prototipo.tcc.domain.Configuracao;
import com.prototipo.tcc.domain.Usuario;
import com.prototipo.tcc.domain.enums.Perfil;
import com.prototipo.tcc.domain.enums.PeriodoRepeticao;
import com.prototipo.tcc.repositories.AnaliseRepository;
import com.prototipo.tcc.repositories.ConfiguracaoRepository;
import com.prototipo.tcc.repositories.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class DBService {

    private final BCryptPasswordEncoder pe;
    private final ConfiguracaoRepository configuracaoRepository;
    private final AnaliseRepository analiseRepository;
    private final UsuarioRepository usuarioRepository;

    public DBService(BCryptPasswordEncoder pe, ConfiguracaoRepository configuracaoRepository,
                     AnaliseRepository analiseRepository, UsuarioRepository usuarioRepository) {
        this.pe = pe;
        this.configuracaoRepository = configuracaoRepository;
        this.analiseRepository = analiseRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void instantiateTestDatabase() {

        Configuracao config1 = new Configuracao();
        config1.setId(null);
        config1.setCapacidadeLitros(1000);
        config1.setFatorDecantadorClarificante(BigDecimal.ONE);
        config1.setPeriodoRepeticao(PeriodoRepeticao.UM_AO_DIA);
        config1.setTemAquecedor(Boolean.TRUE);
        config1.setTemperaturaIdeal(25);

        configuracaoRepository.save(config1);

        Usuario user1 = new Usuario();
        user1.setId(null);
        user1.setNome("Maria Silva");
        user1.setEmail("maria@gmail.com");
        user1.setSenha(pe.encode("123"));
        user1.addPerfil(Perfil.ADMIN);

        Usuario usuario = usuarioRepository.save(user1);

        Analise a1 = new Analise();
        a1.setId(null);
        a1.setPh(BigDecimal.valueOf(7));
        a1.setCondutividade(BigDecimal.valueOf(100));
        a1.setAlcalinidade(BigDecimal.valueOf(100));
        a1.setTurbidez(BigDecimal.valueOf(2));
        a1.setTemperatura(BigDecimal.valueOf(25));
        a1.setDataLeitura(LocalDateTime.now());
        a1.setPhN(BigDecimal.valueOf(20));
        a1.setPhP(BigDecimal.valueOf(0));
        a1.setCloro(BigDecimal.valueOf(30));
        a1.setDecantador(BigDecimal.valueOf(10));
        a1.setDataTratamento(LocalDateTime.now().plusHours(2));
        a1.setPhNovo(BigDecimal.valueOf(7));
        a1.setCondutividadeNovo(BigDecimal.valueOf(100));
        a1.setTurbidezNovo(BigDecimal.valueOf(2));
        a1.setTemperaturaNovo(BigDecimal.valueOf(25));
        a1.setDataLeituraNovo(LocalDateTime.now().plusHours(3));
        a1.setUsuario(usuario);

        Analise a2 = new Analise();
        a2.setId(null);
        a2.setPh(BigDecimal.valueOf(7));
        a2.setCondutividade(BigDecimal.valueOf(100));
        a2.setAlcalinidade(BigDecimal.valueOf(100));
        a2.setTurbidez(BigDecimal.valueOf(2));
        a2.setTemperatura(BigDecimal.valueOf(25));
        a2.setDataLeitura(LocalDateTime.now().plusDays(1));
        a2.setPhN(BigDecimal.valueOf(20));
        a2.setPhP(BigDecimal.valueOf(0));
        a2.setCloro(BigDecimal.valueOf(30));
        a2.setDecantador(BigDecimal.valueOf(10));
        a2.setDataTratamento(LocalDateTime.now().plusDays(1).plusHours(1));
        a2.setPhNovo(BigDecimal.valueOf(7));
        a2.setCondutividadeNovo(BigDecimal.valueOf(100));
        a2.setTurbidezNovo(BigDecimal.valueOf(2));
        a2.setTemperaturaNovo(BigDecimal.valueOf(25));
        a2.setDataLeituraNovo(LocalDateTime.now().plusDays(1).plusHours(2));
        a2.setUsuario(usuario);

        Analise a3 = new Analise();
        a3.setId(null);
        a3.setPh(BigDecimal.valueOf(7));
        a3.setCondutividade(BigDecimal.valueOf(100));
        a3.setAlcalinidade(BigDecimal.valueOf(100));
        a3.setTurbidez(BigDecimal.valueOf(2));
        a3.setTemperatura(BigDecimal.valueOf(25));
        a3.setDataLeitura(LocalDateTime.now().plusDays(2));
        a3.setPhN(BigDecimal.valueOf(20));
        a3.setPhP(BigDecimal.valueOf(0));
        a3.setCloro(BigDecimal.valueOf(30));
        a3.setDecantador(BigDecimal.valueOf(10));
        a3.setDataTratamento(LocalDateTime.now().plusDays(2).plusHours(3));
        a3.setPhNovo(BigDecimal.valueOf(7));
        a3.setCondutividadeNovo(BigDecimal.valueOf(100));
        a3.setTurbidezNovo(BigDecimal.valueOf(2));
        a3.setTemperaturaNovo(BigDecimal.valueOf(25));
        a3.setDataLeituraNovo(LocalDateTime.now().plusDays(2).plusHours(4));
        a3.setUsuario(usuario);

        analiseRepository.saveAll(Arrays.asList(a1, a2, a3));

        usuario.getAnalises().addAll(Arrays.asList(a1, a2, a3));
        usuarioRepository.save(usuario);
    }
}

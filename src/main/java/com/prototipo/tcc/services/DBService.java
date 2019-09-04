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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
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

    public void instantiateTestDatabase() throws ParseException {

        Configuracao config1 = new Configuracao();
        config1.setId(null);
        config1.setCapacidadeLitros(1000);
        config1.setHorarioPrevisto(LocalTime.of(9, 10));
        config1.setPeriodoRepeticao(PeriodoRepeticao.UM_AO_DIA);

        configuracaoRepository.save(config1);

        Usuario user1 = new Usuario();
        user1.setId(null);
        user1.setNome("Maria Silva");
        user1.setEmail("maria@gmail.com");
        user1.setSenha(pe.encode("123"));
        user1.addPerfil(Perfil.ADMIN);

        Usuario usuario = usuarioRepository.save(user1);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Analise a1 = new Analise();
        a1.setId(null);
        a1.setPh(BigDecimal.valueOf(7));
        a1.setCondutividade(BigDecimal.valueOf(100));
        a1.setAlcalinidade(BigDecimal.valueOf(100));
        a1.setTurbidez(BigDecimal.valueOf(2));
        a1.setTemperatura(BigDecimal.valueOf(25));
        a1.setDataLeitura(sdf.parse("30/08/2019 10:32"));
        a1.setPhN(BigDecimal.valueOf(20));
        a1.setPhP(BigDecimal.valueOf(0));
        a1.setCloro(BigDecimal.valueOf(30));
        a1.setDecantador(BigDecimal.valueOf(10));
        a1.setDataTratamento(sdf.parse("30/08/2019 10:40"));
        a1.setPhNovo(BigDecimal.valueOf(7));
        a1.setCondutividadeNovo(BigDecimal.valueOf(100));
        a1.setTurbidezNovo(BigDecimal.valueOf(2));
        a1.setTemperaturaNovo(BigDecimal.valueOf(25));
        a1.setDataLeituraNovo(sdf.parse("30/08/2019 10:50"));
        a1.setUsuario(usuario);

        Analise a2 = new Analise();
        a2.setId(null);
        a2.setPh(BigDecimal.valueOf(7));
        a2.setCondutividade(BigDecimal.valueOf(100));
        a2.setAlcalinidade(BigDecimal.valueOf(100));
        a2.setTurbidez(BigDecimal.valueOf(2));
        a2.setTemperatura(BigDecimal.valueOf(25));
        a2.setDataLeitura(sdf.parse("31/08/2019 10:32"));
        a2.setPhN(BigDecimal.valueOf(20));
        a2.setPhP(BigDecimal.valueOf(0));
        a2.setCloro(BigDecimal.valueOf(30));
        a2.setDecantador(BigDecimal.valueOf(10));
        a2.setDataTratamento(sdf.parse("31/08/2019 10:40"));
        a2.setPhNovo(BigDecimal.valueOf(7));
        a2.setCondutividadeNovo(BigDecimal.valueOf(100));
        a2.setTurbidezNovo(BigDecimal.valueOf(2));
        a2.setTemperaturaNovo(BigDecimal.valueOf(25));
        a2.setDataLeituraNovo(sdf.parse("31/08/2019 10:50"));
        a2.setUsuario(usuario);

        Analise a3 = new Analise();
        a3.setId(null);
        a3.setPh(BigDecimal.valueOf(7));
        a3.setCondutividade(BigDecimal.valueOf(100));
        a3.setAlcalinidade(BigDecimal.valueOf(100));
        a3.setTurbidez(BigDecimal.valueOf(2));
        a3.setTemperatura(BigDecimal.valueOf(25));
        a3.setDataLeitura(sdf.parse("05/09/2019 10:32"));
        a3.setPhN(BigDecimal.valueOf(20));
        a3.setPhP(BigDecimal.valueOf(0));
        a3.setCloro(BigDecimal.valueOf(30));
        a3.setDecantador(BigDecimal.valueOf(10));
        a3.setDataTratamento(sdf.parse("05/09/2019 10:40"));
        a3.setPhNovo(BigDecimal.valueOf(7));
        a3.setCondutividadeNovo(BigDecimal.valueOf(100));
        a3.setTurbidezNovo(BigDecimal.valueOf(2));
        a3.setTemperaturaNovo(BigDecimal.valueOf(25));
        a3.setDataLeituraNovo(sdf.parse("05/09/2019 10:50"));
        a3.setUsuario(usuario);

        Analise a4 = new Analise();
        a4.setId(null);
        a4.setPh(BigDecimal.valueOf(7));
        a4.setCondutividade(BigDecimal.valueOf(100));
        a4.setAlcalinidade(BigDecimal.valueOf(100));
        a4.setTurbidez(BigDecimal.valueOf(2));
        a4.setTemperatura(BigDecimal.valueOf(25));
        a4.setDataLeitura(sdf.parse("10/09/2019 10:32"));
        a4.setPhN(BigDecimal.valueOf(20));
        a4.setPhP(BigDecimal.valueOf(0));
        a4.setCloro(BigDecimal.valueOf(30));
        a4.setDecantador(BigDecimal.valueOf(10));
        a4.setDataTratamento(sdf.parse("10/09/2019 10:40"));
        a4.setPhNovo(BigDecimal.valueOf(7));
        a4.setCondutividadeNovo(BigDecimal.valueOf(100));
        a4.setTurbidezNovo(BigDecimal.valueOf(2));
        a4.setTemperaturaNovo(BigDecimal.valueOf(25));
        a4.setDataLeituraNovo(sdf.parse("10/09/2019 10:50"));
        a4.setUsuario(usuario);

        analiseRepository.saveAll(Arrays.asList(a1, a2, a3, a4));

        usuario.getAnalises().addAll(Arrays.asList(a1, a2, a3, a4));
        usuarioRepository.save(usuario);
    }
}

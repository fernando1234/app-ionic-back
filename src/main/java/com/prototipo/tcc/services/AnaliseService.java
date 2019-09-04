package com.prototipo.tcc.services;

import com.prototipo.tcc.domain.Analise;
import com.prototipo.tcc.domain.Usuario;
import com.prototipo.tcc.repositories.AnaliseRepository;
import com.prototipo.tcc.security.UserSS;
import com.prototipo.tcc.services.exceptions.AuthorizationException;
import com.prototipo.tcc.services.utils.EmailService;
import com.prototipo.tcc.services.utils.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class AnaliseService {

    private final AnaliseRepository repo;
    private final EmailService emailService;
    private final UsuarioService usuarioService;

    public AnaliseService(AnaliseRepository repo, EmailService emailService, UsuarioService usuarioService) {
        this.repo = repo;
        this.emailService = emailService;
        this.usuarioService = usuarioService;
    }

    public Analise insert(Analise obj) {
        obj.setId(null);
        obj = repo.save(obj);
        emailService.sendOrderConfirmationEmail(obj);

        return obj;
    }

    public Page<Analise> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        UserSS user = UserService.authenticated();
        if (user == null) {
            throw new AuthorizationException("Acesso negado");
        }
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
        Usuario usuario = usuarioService.find(user.getId());
        return repo.findByUsuario(usuario, pageRequest);
    }
}

package com.prototipo.tcc.resources;

import com.prototipo.tcc.domain.Configuracao;
import com.prototipo.tcc.repositories.ConfiguracaoRepository;
import com.prototipo.tcc.services.exceptions.ObjectNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(value = "/configuracao")
public class ConfiguracaoResource {

    private final ConfiguracaoRepository repo;

    public ConfiguracaoResource(ConfiguracaoRepository repo) {
        this.repo = repo;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Configuracao> find(@PathVariable Integer id) {
        Optional<Configuracao> obj = repo.findById(id);
        return ResponseEntity.ok().body(obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Configuracao.class.getName())));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@Valid @RequestBody Configuracao obj) {
        obj.setId(null);
        Configuracao save = repo.save(obj);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(save.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@Valid @RequestBody Configuracao obj, @PathVariable Integer id) {
        obj.setId(id);
        repo.save(obj);
        return ResponseEntity.noContent().build();
    }

}

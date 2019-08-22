package com.prototipo.tcc.resources;

import com.prototipo.tcc.domain.Analise;
import com.prototipo.tcc.repositories.AnaliseRepository;
import com.prototipo.tcc.services.AnaliseService;
import com.prototipo.tcc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(value = "/analises")
public class AnaliseResource {

    @Autowired
    private AnaliseRepository repo;

    @Autowired
    private AnaliseService service;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Analise> find(@PathVariable Integer id) {
        Optional<Analise> obj = repo.findById(id);

        Analise analise = obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Analise.class.getName()));

        return ResponseEntity.ok().body(analise);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@Valid @RequestBody Analise obj) {
        obj = service.insert(obj);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<Analise>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "20") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "instante") String orderBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction) {

        Page<Analise> list = service.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok().body(list);
    }
}

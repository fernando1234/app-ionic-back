package com.prototipo.tcc.resources;

import com.pi4j.io.i2c.I2CFactory;
import com.prototipo.tcc.domain.Analise;
import com.prototipo.tcc.repositories.AnaliseRepository;
import com.prototipo.tcc.services.TratamentoService;
import com.prototipo.tcc.services.exceptions.ObjectNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/analises")
public class AnaliseResource {

    private final AnaliseRepository repo;
    //    private final ColetaService coletaService;
    private final TratamentoService tratamentoService;

    public AnaliseResource(AnaliseRepository repo, TratamentoService tratamentoService) {
        this.repo = repo;
        this.tratamentoService = tratamentoService;
    }

    @RequestMapping(value = "/iniciar", method = RequestMethod.GET)
    public ResponseEntity iniciar() throws InterruptedException, IOException, I2CFactory.UnsupportedBusNumberException {
        tratamentoService.processa(null);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Analise> find(@PathVariable Integer id) {
        Optional<Analise> obj = repo.findById(id);

        Analise analise = obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Analise.class.getName()));

        return ResponseEntity.ok().body(analise);
    }

//    @RequestMapping(method = RequestMethod.POST)
//    public ResponseEntity<Void> insert(@Valid @RequestBody Analise obj) {
//        obj = service.insert(obj);
//
//        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
//        return ResponseEntity.created(uri).build();
//    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Analise>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "20") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "instante") String orderBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction) {

//        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

//        Page<Analise> all = repo.findAll(pageRequest);

        List<Analise> all = repo.findAll();

        return ResponseEntity.ok().body(all);
    }
}

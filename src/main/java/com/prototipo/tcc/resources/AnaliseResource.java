package com.prototipo.tcc.resources;

import com.pi4j.io.i2c.I2CFactory;
import com.prototipo.tcc.domain.Analise;
import com.prototipo.tcc.repositories.AnaliseRepository;
import com.prototipo.tcc.services.AnaliseService;
import com.prototipo.tcc.services.AquecedorService;
import com.prototipo.tcc.services.ColetaService;
import com.prototipo.tcc.services.exceptions.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/analises")
public class AnaliseResource {

    private final AnaliseRepository repo;
    private final ColetaService coletaService;
    private final AnaliseService analiseService;
    private final AquecedorService aquecedorService;

    public AnaliseResource(AnaliseRepository repo, ColetaService coletaService, AnaliseService analiseService, AquecedorService aquecedorService) {
        this.repo = repo;
        this.coletaService = coletaService;
        this.analiseService = analiseService;
        this.aquecedorService = aquecedorService;
    }

    @RequestMapping(value = "/iniciar", method = RequestMethod.GET)
    public ResponseEntity iniciar() throws InterruptedException, IOException, I2CFactory.UnsupportedBusNumberException {
        coletaService.nova();
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/aquecer", method = RequestMethod.GET)
    public ResponseEntity aquecer() throws InterruptedException {
        aquecedorService.aquecer();
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Analise> find(@PathVariable Integer id) {
        Optional<Analise> obj = repo.findById(id);

        Analise analise = obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Analise.class.getName()));

        return ResponseEntity.ok().body(analise);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<Analise>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "20") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "dataLeitura") String orderBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction) {

        Page<Analise> all = analiseService.findPage(page, linesPerPage, orderBy, direction);

        return ResponseEntity.ok().body(all);
    }
}

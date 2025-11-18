package com.ifpb.sorrisus.controller;

import com.ifpb.sorrisus.model.Dentista;
import com.ifpb.sorrisus.service.DentistaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dentistas")
public class DentistaController {

    private final DentistaService service;

    public DentistaController(DentistaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Dentista> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dentista> buscarPorId(@PathVariable Long id) {
        Dentista dentista = service.buscarPorId(id);
        return ResponseEntity.ok(dentista);
    }

    @PostMapping
    public ResponseEntity<Dentista> criar(@RequestBody Dentista dentista) {
        return ResponseEntity.ok(service.salvar(dentista));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dentista> atualizar(@PathVariable Long id, @RequestBody Dentista dentista) {
        return ResponseEntity.ok(service.atualizar(id, dentista));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

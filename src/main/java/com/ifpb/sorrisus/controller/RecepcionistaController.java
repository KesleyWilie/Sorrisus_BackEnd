package com.ifpb.sorrisus.controller;

import com.ifpb.sorrisus.model.Recepcionista;
import com.ifpb.sorrisus.service.RecepcionistaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recepcionistas")
public class RecepcionistaController {

    private final RecepcionistaService service;

    public RecepcionistaController(RecepcionistaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Recepcionista> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recepcionista> buscarPorId(@PathVariable Long id) {
        Recepcionista recepcionista = service.buscarPorId(id);
        return ResponseEntity.ok(recepcionista);
    }

    @PostMapping
    public ResponseEntity<Recepcionista> criar(@RequestBody Recepcionista recepcionista) {
        return ResponseEntity.ok(service.salvar(recepcionista));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recepcionista> atualizar(@PathVariable Long id, @RequestBody Recepcionista recepcionista) {
        return ResponseEntity.ok(service.atualizar(id, recepcionista));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

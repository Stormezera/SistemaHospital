package com.hospital.api.controller;

import com.hospital.api.dto.ProntuarioRequestDTO;
import com.hospital.api.dto.ProntuarioResponseDTO;
import com.hospital.api.service.ProntuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prontuarios")
public class ProntuarioController {

    @Autowired
    private ProntuarioService prontuarioService;

    @GetMapping
    public List<ProntuarioResponseDTO> listar() {
        return prontuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProntuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return prontuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProntuarioResponseDTO> cadastrar(@RequestBody @Valid ProntuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prontuarioService.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProntuarioResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ProntuarioRequestDTO dto) {
        return prontuarioService.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluir(@PathVariable Long id) {
        if (prontuarioService.excluir(id)) {
            return ResponseEntity.ok("Prontuário removido com sucesso!");
        }
        return ResponseEntity.notFound().build();
    }
}

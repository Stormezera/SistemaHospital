package com.hospital.api.controller;

import com.hospital.api.dto.ReceitaRequestDTO;
import com.hospital.api.dto.ReceitaResponseDTO;
import com.hospital.api.service.ReceitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {

    @Autowired
    private ReceitaService receitaService;

    @GetMapping
    public List<ReceitaResponseDTO> listar() {
        return receitaService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaResponseDTO> buscarPorId(@PathVariable Long id) {
        return receitaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReceitaResponseDTO> cadastrar(@RequestBody @Valid ReceitaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(receitaService.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceitaResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ReceitaRequestDTO dto) {
        return receitaService.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluir(@PathVariable Long id) {
        if (receitaService.excluir(id)) {
            return ResponseEntity.ok("Receita removida com sucesso!");
        }
        return ResponseEntity.notFound().build();
    }
}

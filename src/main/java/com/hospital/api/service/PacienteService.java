package com.hospital.api.service;

import com.hospital.api.dto.PacienteRequestDTO;
import com.hospital.api.dto.PacienteResponseDTO;
import com.hospital.api.model.Paciente;
import com.hospital.api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PacienteService {
    @Autowired
    private PacienteRepository pacienteRepository;

    public List<PacienteResponseDTO> listarTodos() {
        return pacienteRepository.findAll()
                .stream()
                .map(PacienteResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<PacienteResponseDTO> buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .map(PacienteResponseDTO::new);
    }

    public PacienteResponseDTO salvar(PacienteRequestDTO dto) {
        Paciente paciente = new Paciente();
        preencherDados(paciente, dto);
        return new PacienteResponseDTO(pacienteRepository.save(paciente));
    }

    public Optional<PacienteResponseDTO> atualizar(Long id, PacienteRequestDTO dto) {
        return pacienteRepository.findById(id).map(paciente -> {
            preencherDados(paciente, dto);
            return new PacienteResponseDTO(pacienteRepository.save(paciente));
        });
    }

    public boolean excluir(Long id) {
        if (!pacienteRepository.existsById(id)) return false;
        pacienteRepository.deleteById(id);
        return true;
    }

    private void preencherDados(Paciente paciente, PacienteRequestDTO dto) {
        paciente.setNome(dto.getNome());
        paciente.setCpf(dto.getCpf());
        paciente.setEmail(dto.getEmail());
        paciente.setTelefone(dto.getTelefone());
        paciente.setDataNascimento(dto.getDataNascimento());
    }
}

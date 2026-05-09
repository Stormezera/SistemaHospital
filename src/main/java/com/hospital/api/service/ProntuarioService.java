package com.hospital.api.service;

import com.hospital.api.dto.ProntuarioRequestDTO;
import com.hospital.api.dto.ProntuarioResponseDTO;
import com.hospital.api.model.Paciente;
import com.hospital.api.model.Prontuario;
import com.hospital.api.repository.PacienteRepository;
import com.hospital.api.repository.ProntuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProntuarioService {
    @Autowired
    private ProntuarioRepository prontuarioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<ProntuarioResponseDTO> listarTodos() {
        return prontuarioRepository.findAll()
                .stream()
                .map(ProntuarioResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<ProntuarioResponseDTO> buscarPorId(Long id) {
        return prontuarioRepository.findById(id)
                .map(ProntuarioResponseDTO::new);
    }

    public ProntuarioResponseDTO salvar(ProntuarioRequestDTO dto) {
        Prontuario prontuario = new Prontuario();
        preencherDados(prontuario, dto);
        return new ProntuarioResponseDTO(prontuarioRepository.save(prontuario));
    }

    public Optional<ProntuarioResponseDTO> atualizar(Long id, ProntuarioRequestDTO dto) {
        return prontuarioRepository.findById(id).map(prontuario -> {
            preencherDados(prontuario, dto);
            return new ProntuarioResponseDTO(prontuarioRepository.save(prontuario));
        });
    }

    public boolean excluir(Long id) {
        if (!prontuarioRepository.existsById(id)) return false;
        prontuarioRepository.deleteById(id);
        return true;
    }

    // --- Método auxiliar ---

    private void preencherDados(Prontuario prontuario, ProntuarioRequestDTO dto) {
        prontuario.setHistorico(dto.getHistorico());
        prontuario.setDataAbertura(dto.getDataAbertura());

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado: id=" + dto.getPacienteId()));
        prontuario.setPaciente(paciente);
    }
}

package com.hospital.api.service;

import com.hospital.api.dto.ConsultaRequestDTO;
import com.hospital.api.dto.ConsultaResponseDTO;
import com.hospital.api.model.Consulta;
import com.hospital.api.model.Convenio;
import com.hospital.api.model.Medico;
import com.hospital.api.model.Paciente;
import com.hospital.api.repository.ConsultaRepository;
import com.hospital.api.repository.ConvenioRepository;
import com.hospital.api.repository.MedicoRepository;
import com.hospital.api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsultaService {
    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private ConvenioRepository convenioRepository;

    public List<ConsultaResponseDTO> listarTodos() {
        return consultaRepository.findAll()
                .stream()
                .map(ConsultaResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<ConsultaResponseDTO> buscarPorId(Long id) {
        return consultaRepository.findById(id)
                .map(ConsultaResponseDTO::new);
    }

    public ConsultaResponseDTO salvar(ConsultaRequestDTO dto) {
        Consulta consulta = new Consulta();
        preencherDados(consulta, dto);
        return new ConsultaResponseDTO(consultaRepository.save(consulta));
    }

    public Optional<ConsultaResponseDTO> atualizar(Long id, ConsultaRequestDTO dto) {
        return consultaRepository.findById(id).map(consulta -> {
            preencherDados(consulta, dto);
            return new ConsultaResponseDTO(consultaRepository.save(consulta));
        });
    }

    public boolean excluir(Long id) {
        if (!consultaRepository.existsById(id)) return false;
        consultaRepository.deleteById(id);
        return true;
    }

    private void preencherDados(Consulta consulta, ConsultaRequestDTO dto) {
        consulta.setData(dto.getData());
        consulta.setObservacoes(dto.getObservacoes());

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado: id=" + dto.getPacienteId()));
        consulta.setPaciente(paciente);

        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado: id=" + dto.getMedicoId()));
        consulta.setMedico(medico);

        if (dto.getConvenioId() != null) {
            Convenio convenio = convenioRepository.findById(dto.getConvenioId())
                    .orElseThrow(() -> new IllegalArgumentException("Convênio não encontrado: id=" + dto.getConvenioId()));
            consulta.setConvenio(convenio);
        } else {
            consulta.setConvenio(null);
        }
    }
}

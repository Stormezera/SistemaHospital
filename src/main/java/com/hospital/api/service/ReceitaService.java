package com.hospital.api.service;

import com.hospital.api.dto.ReceitaRequestDTO;
import com.hospital.api.dto.ReceitaResponseDTO;
import com.hospital.api.model.Consulta;
import com.hospital.api.model.Receita;
import com.hospital.api.repository.ConsultaRepository;
import com.hospital.api.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReceitaService {
    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    public List<ReceitaResponseDTO> listarTodos() {
        return receitaRepository.findAll()
                .stream()
                .map(ReceitaResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<ReceitaResponseDTO> buscarPorId(Long id) {
        return receitaRepository.findById(id)
                .map(ReceitaResponseDTO::new);
    }

    public ReceitaResponseDTO salvar(ReceitaRequestDTO dto) {
        Receita receita = new Receita();
        preencherDados(receita, dto);
        return new ReceitaResponseDTO(receitaRepository.save(receita));
    }

    public Optional<ReceitaResponseDTO> atualizar(Long id, ReceitaRequestDTO dto) {
        return receitaRepository.findById(id).map(receita -> {
            preencherDados(receita, dto);
            return new ReceitaResponseDTO(receitaRepository.save(receita));
        });
    }

    public boolean excluir(Long id) {
        if (!receitaRepository.existsById(id)) return false;
        receitaRepository.deleteById(id);
        return true;
    }
    
    private void preencherDados(Receita receita, ReceitaRequestDTO dto) {
        receita.setDescricao(dto.getDescricao());
        receita.setMedicamentos(dto.getMedicamentos());
        receita.setData(dto.getData());

        Consulta consulta = consultaRepository.findById(dto.getConsultaId())
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada: id=" + dto.getConsultaId()));
        receita.setConsulta(consulta);
    }
}

package com.hospital.api.service;

import com.hospital.api.dto.ConvenioRequestDTO;
import com.hospital.api.dto.ConvenioResponseDTO;
import com.hospital.api.model.Convenio;
import com.hospital.api.repository.ConvenioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConvenioService {
    @Autowired
    private ConvenioRepository convenioRepository;

    public List<ConvenioResponseDTO> listarTodos() {
        return convenioRepository.findAll()
                .stream()
                .map(ConvenioResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<ConvenioResponseDTO> buscarPorId(Long id) {
        return convenioRepository.findById(id)
                .map(ConvenioResponseDTO::new);
    }

    public ConvenioResponseDTO salvar(ConvenioRequestDTO dto) {
        Convenio convenio = new Convenio();
        preencherDados(convenio, dto);
        return new ConvenioResponseDTO(convenioRepository.save(convenio));
    }

    public Optional<ConvenioResponseDTO> atualizar(Long id, ConvenioRequestDTO dto) {
        return convenioRepository.findById(id).map(convenio -> {
            preencherDados(convenio, dto);
            return new ConvenioResponseDTO(convenioRepository.save(convenio));
        });
    }

    public boolean excluir(Long id) {
        if (!convenioRepository.existsById(id)) return false;
        convenioRepository.deleteById(id);
        return true;
    }

    private void preencherDados(Convenio convenio, ConvenioRequestDTO dto) {
        convenio.setNome(dto.getNome());
        convenio.setCodigo(dto.getCodigo());
        convenio.setDescricao(dto.getDescricao());
    }
}

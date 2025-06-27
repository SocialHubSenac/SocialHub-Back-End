package com.senac.socialhub.service;

import com.senac.socialhub.controller.dto.InstituicaoRequestDTO;
import com.senac.socialhub.entity.Instituicao;
import com.senac.socialhub.exception.ValidacaoException;
import com.senac.socialhub.repository.InstituicaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstituicaoService {

    private final InstituicaoRepository instituicaoRepository;

    public InstituicaoService(InstituicaoRepository instituicaoRepository) {
        this.instituicaoRepository = instituicaoRepository;
    }

    public Instituicao salvar(InstituicaoRequestDTO dto) {
        if (!dto.isSemFinsLucrativos()) {
            throw new ValidacaoException("A instituição deve ser sem fins lucrativos.");
        }

        if (!dto.getLocalizacao().equalsIgnoreCase("Criciúma")){
            throw new ValidacaoException("A instituição deve estar localizada em criciuma");
        }

        Instituicao nova = Instituicao.builder()
                .Nome(dto.getNome())
                .Descricao(dto.getDescricao())
                .Missao(dto.getMissao())
                .Localizacao(dto.getLocalizacao())
                .semFinsLucrativos(dto.isSemFinsLucrativos())
                .build();

        return instituicaoRepository.save(nova);
    }

    public List<Instituicao> listar() {
        return instituicaoRepository.findAll();
    }

    public Instituicao buscarPorId(Long id) {
        return instituicaoRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Instituiçao não encontrada com este ID: " + id));
    }

    public Instituicao atualizar(Long id, InstituicaoRequestDTO dto) {
        Instituicao existente = buscarPorId(id);
        existente.setNome(dto.getNome());
        existente.setDescricao(dto.getDescricao());
        existente.setMissao(dto.getMissao());
        existente.setLocalizacao(dto.getLocalizacao());
        existente.setSemFinsLucrativos(dto.isSemFinsLucrativos());

        return salvar(dto);
    }

    public void excluir(Long id) {
        Instituicao existente = buscarPorId(id);
        instituicaoRepository.delete(existente);
    }
}

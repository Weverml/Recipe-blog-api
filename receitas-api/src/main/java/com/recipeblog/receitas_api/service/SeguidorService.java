package com.recipeblog.receitas_api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.recipeblog.receitas_api.exception.BusinessException;
import com.recipeblog.receitas_api.exception.ResourceNotFoundException;
import com.recipeblog.receitas_api.model.Seguidor;
import com.recipeblog.receitas_api.model.Usuario;
import com.recipeblog.receitas_api.repository.SeguidorRepository;
import com.recipeblog.receitas_api.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class SeguidorService {
    private final SeguidorRepository seguidorRepository;
    private final UsuarioRepository usuarioRepository;

    public void seguir(Long seguidorId, Long seguidoId) {

        if (seguidorId.equals(seguidoId)) {
            throw new BusinessException("Você não pode seguir a si mesmo.");
        }

        if (seguidorRepository.existsBySeguidorIdAndSeguidoId(seguidorId, seguidoId)) {
            throw new BusinessException("Você já segue esse usuário.");
        }

        Usuario seguidor = usuarioRepository.findById(seguidorId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Usuario seguido = usuarioRepository.findById(seguidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Seguidor novoSeguidor = Seguidor.builder()
                .seguidor(seguidor)
                .seguido(seguido)
                .build();

        seguidorRepository.save(novoSeguidor);
    }

    public void deixarDeSeguir(Long seguidorId, Long seguidoId) {
        Seguidor seguidor = seguidorRepository.findBySeguidorIdAndSeguidoId(seguidorId, seguidoId)
                .orElseThrow(() -> new BusinessException("Você não segue esse usuário."));

        seguidorRepository.delete(seguidor);
    }

    public boolean estaSeguindo(Long seguidorId, Long seguidoId) {
        return seguidorRepository.existsBySeguidorIdAndSeguidoId(seguidorId, seguidoId);
    }

    public List<Long> listarIdsSeguidosPor(Long seguidorId) {
        return seguidorRepository.findBySeguidorId(seguidorId)
                .stream()
                .map(s -> s.getSeguido().getId())
                .collect(Collectors.toList());
    }

    public long contarSeguidores(Long usuarioId) {
        return seguidorRepository.countBySeguidoId(usuarioId);
    }

    public long contarSeguindo(Long usuarioId) {
        return seguidorRepository.countBySeguidorId(usuarioId);
    }
}

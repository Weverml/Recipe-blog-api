package com.recipeblog.receitas_api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.recipeblog.receitas_api.dto.ReceitaDTO;
import com.recipeblog.receitas_api.exception.ResourceNotFoundException;
import com.recipeblog.receitas_api.model.Receita;
import com.recipeblog.receitas_api.model.Usuario;
import com.recipeblog.receitas_api.repository.ReceitaRepository;
import com.recipeblog.receitas_api.repository.UsuarioRepository;
import com.recipeblog.receitas_api.service.SeguidorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
 
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceitaService {
    private final ReceitaRepository receitaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SeguidorService seguidorService;

    private final Cloudinary cloudinary;
 
    public ReceitaDTO criar(Long usuarioId, String titulo, String descricao,
                            String ingredientes, String modoPreparo,
                            String categoria, Integer tempoPreparo,
                            MultipartFile imagem) throws IOException {
 
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
 
        String imagemUrl = null;
        if (imagem != null && !imagem.isEmpty()) {
            imagemUrl = uploadParaCloudinary(imagem);
        }
 
        Receita receita = Receita.builder()
                .titulo(titulo)
                .descricao(descricao)
                .ingredientes(ingredientes)
                .modoPreparo(modoPreparo)
                .categoria(categoria)
                .tempoPreparo(tempoPreparo)
                .imagemUrl(imagemUrl)
                .usuario(usuario)
                .build();
 
        return toDTO(receitaRepository.save(receita));
    }
 
    public List<ReceitaDTO> listarPorUsuario(Long usuarioId) {
        return receitaRepository.findByUsuarioIdOrderByCriadoEmDesc(usuarioId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
 
    public ReceitaDTO buscarPorId(Long id) {
        return receitaRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Receita não encontrada"));
    }
 
    private String uploadParaCloudinary(MultipartFile arquivo) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(
                arquivo.getBytes(),
                ObjectUtils.asMap("folder", "receitas") 
        );
        return (String) uploadResult.get("secure_url");
    }
 
    private ReceitaDTO toDTO(Receita r) {
        return ReceitaDTO.builder()
                .id(r.getId())
                .titulo(r.getTitulo())
                .descricao(r.getDescricao())
                .ingredientes(r.getIngredientes())
                .modoPreparo(r.getModoPreparo())
                .categoria(r.getCategoria())
                .tempoPreparo(r.getTempoPreparo())
                .imagemUrl(r.getImagemUrl())
                .criadoEm(r.getCriadoEm())
                .usuarioId(r.getUsuario().getId())
                .usuarioNome(r.getUsuario().getNome())
                .usuarioUsername(r.getUsuario().getUsername())
                .usuarioFoto(r.getUsuario().getFotoPerfil())
                .build();
    }

    public List<ReceitaDTO> feed(Long usuarioId) {
        List<Long> ids = seguidorService.listarIdsSeguidosPor(usuarioId);
        
        ids.add(usuarioId);
        
        return receitaRepository.findByUsuarioIdInOrderByCriadoEmDesc(ids)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}

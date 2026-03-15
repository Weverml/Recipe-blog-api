package com.recipeblog.receitas_api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.recipeblog.receitas_api.dto.ReceitaDTO;
import com.recipeblog.receitas_api.exception.ResourceNotFoundException;
import com.recipeblog.receitas_api.model.Receita;
import com.recipeblog.receitas_api.model.Usuario;
import com.recipeblog.receitas_api.repository.ReceitaRepository;
import com.recipeblog.receitas_api.repository.UsuarioRepository;
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
 
    // Cloudinary injetado via CloudinaryConfig
    private final Cloudinary cloudinary;
 
    public ReceitaDTO criar(Long usuarioId, String titulo, String descricao,
                            String ingredientes, String modoPreparo,
                            String categoria, Integer tempoPreparo,
                            MultipartFile imagem) throws IOException {
 
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
 
        // Faz upload da imagem para o Cloudinary se ela foi enviada
        // O Cloudinary retorna uma URL pública que funciona em qualquer lugar
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
 
    // Envia o arquivo para o Cloudinary e retorna a URL pública da imagem
    private String uploadParaCloudinary(MultipartFile arquivo) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(
                arquivo.getBytes(),
                ObjectUtils.asMap("folder", "receitas") // salva na pasta "receitas" no Cloudinary
        );
        // "secure_url" retorna a URL com HTTPS
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
                // URL pública do Cloudinary — funciona direto no frontend
                .imagemUrl(r.getImagemUrl())
                .criadoEm(r.getCriadoEm())
                .usuarioId(r.getUsuario().getId())
                .usuarioNome(r.getUsuario().getNome())
                .usuarioUsername(r.getUsuario().getUsername())
                .build();
    }
}

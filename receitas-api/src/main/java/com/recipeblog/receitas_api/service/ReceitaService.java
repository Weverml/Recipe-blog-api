package com.recipeblog.receitas_api.service;

import com.recipeblog.receitas_api.dto.ReceitaDTO;
import com.recipeblog.receitas_api.exception.ResourceNotFoundException;
import com.recipeblog.receitas_api.model.Receita;
import com.recipeblog.receitas_api.model.Usuario;
import com.recipeblog.receitas_api.repository.ReceitaRepository;
import com.recipeblog.receitas_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceitaService {
    // Repositórios injetados para acessar o banco de dados
    private final ReceitaRepository receitaRepository;
    private final UsuarioRepository usuarioRepository;

    // Lê a propriedade "upload.dir" do application.properties
    // Se não estiver definida, usa "uploads" como padrão
    @Value("${upload.dir:uploads}")
    private String uploadDir;

    // Método responsável por criar uma nova receita
    // Recebe todos os campos do formulário + o arquivo de imagem (pode ser nulo)
    public ReceitaDTO criar(Long usuarioId, String titulo, String descricao,
                            String ingredientes, String modoPreparo,
                            String categoria, Integer tempoPreparo,
                            MultipartFile imagem) throws IOException {

        // Verifica se o usuário existe no banco — se não existir, lança erro 404
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        // Só tenta salvar a imagem se ela foi enviada
        String imagemUrl = null;
        if (imagem != null && !imagem.isEmpty()) {
            imagemUrl = salvarArquivo(imagem);
        }

        // Monta o objeto Receita com todos os dados usando o padrão Builder
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

        // Salva no banco e já converte para DTO para retornar ao frontend
        return toDTO(receitaRepository.save(receita));
    }

    // Busca todas as receitas de um usuário (usada na página de perfil)
    public List<ReceitaDTO> listarPorUsuario(Long usuarioId) {
        // stream() + map() percorre a lista e converte cada Receita em ReceitaDTO
        return receitaRepository.findByUsuarioIdOrderByCriadoEmDesc(usuarioId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Busca uma receita específica pelo ID
    public ReceitaDTO buscarPorId(Long id) {
        return receitaRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Receita não encontrada"));
    }

    // Método privado: salva o arquivo de imagem na pasta local e retorna o nome do arquivo
    private String salvarArquivo(MultipartFile arquivo) throws IOException {

        // Cria o diretório "uploads" se ele ainda não existir
        Path diretorio = Paths.get(uploadDir);
        if (!Files.exists(diretorio)) {
            Files.createDirectories(diretorio);
        }

        // Pega a extensão do arquivo original (ex: ".jpg", ".png")
        String extensao = "";
        String nomeOriginal = arquivo.getOriginalFilename();
        if (nomeOriginal != null && nomeOriginal.contains(".")) {
            extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
        }

        // Gera um nome único para evitar conflito entre arquivos com mesmo nome
        // UUID gera um código aleatório como: "a3f8c21d-4b2e-..."
        String nomeArquivo = UUID.randomUUID() + extensao;

        // Copia o arquivo para a pasta de destino
        Files.copy(arquivo.getInputStream(), diretorio.resolve(nomeArquivo), StandardCopyOption.REPLACE_EXISTING);

        // Retorna só o nome do arquivo (a URL completa é montada no toDTO)
        return nomeArquivo;
    }

    // Converte uma entidade Receita (do banco) para ReceitaDTO (para o frontend)
    // Esse padrão evita expor diretamente os objetos do banco de dados
    private ReceitaDTO toDTO(Receita r) {
        return ReceitaDTO.builder()
                .id(r.getId())
                .titulo(r.getTitulo())
                .descricao(r.getDescricao())
                .ingredientes(r.getIngredientes())
                .modoPreparo(r.getModoPreparo())
                .categoria(r.getCategoria())
                .tempoPreparo(r.getTempoPreparo())
                // Monta a URL completa da imagem — o frontend usa esse caminho para exibir
                .imagemUrl(r.getImagemUrl() != null ? "/uploads/" + r.getImagemUrl() : null)
                .criadoEm(r.getCriadoEm())
                .usuarioId(r.getUsuario().getId())
                .usuarioNome(r.getUsuario().getNome())
                .usuarioUsername(r.getUsuario().getUsername())
                .build();
    }
}

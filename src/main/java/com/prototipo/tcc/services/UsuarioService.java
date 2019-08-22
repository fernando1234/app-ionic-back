package com.prototipo.tcc.services;

import com.prototipo.tcc.domain.Usuario;
import com.prototipo.tcc.domain.enums.Perfil;
import com.prototipo.tcc.repositories.UsuarioRepository;
import com.prototipo.tcc.security.UserSS;
import com.prototipo.tcc.services.exceptions.AuthorizationException;
import com.prototipo.tcc.services.exceptions.ObjectNotFoundException;
import com.prototipo.tcc.services.utils.ImageService;
import com.prototipo.tcc.services.utils.S3Service;
import com.prototipo.tcc.services.utils.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.Optional;

@Service
public class UsuarioService {

    @Value("${img.prefix.client.profile}")
    private String prefix;

    @Value("${img.profile.size}")
    private Integer size;

    private final UsuarioRepository repo;
    private final BCryptPasswordEncoder pe;
    private final S3Service s3Service;
    private final ImageService imageService;

    public UsuarioService(UsuarioRepository repo, BCryptPasswordEncoder pe, S3Service s3Service, ImageService imageService) {
        this.repo = repo;
        this.pe = pe;
        this.s3Service = s3Service;
        this.imageService = imageService;
    }

    public Usuario find(Integer id) {
        UserSS user = UserService.authenticated();
        if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
            throw new AuthorizationException("Acesso negado");
        }

        Optional<Usuario> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Usuario.class.getName()));
    }

    public Usuario findByEmail(String email) {
        UserSS user = UserService.authenticated();
        if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
            throw new AuthorizationException("Acesso negado");
        }

        Usuario obj = repo.findByEmail(email);
        if (obj == null) {
            throw new ObjectNotFoundException(
                    "Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + Usuario.class.getName());
        }
        return obj;
    }

    public URI uploadProfilePicture(MultipartFile multipartFile) {
        UserSS user = UserService.authenticated();
        if (user == null) {
            throw new AuthorizationException("Acesso negado");
        }

        BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
        jpgImage = imageService.cropSquare(jpgImage);
        jpgImage = imageService.resize(jpgImage, size);

        String fileName = prefix + user.getId() + ".jpg";

        return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
    }
}

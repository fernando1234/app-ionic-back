package com.prototipo.tcc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.prototipo.tcc.domain.Usuario;
import com.prototipo.tcc.repositories.UsuarioRepository;
import com.prototipo.tcc.resources.exception.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;


public class UsuarioUpdateValidator implements ConstraintValidator<UsuarioUpdate, Usuario> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UsuarioRepository repo;

    @Override
    public void initialize(UsuarioUpdate ann) {
    }

    @Override
    public boolean isValid(Usuario obj, ConstraintValidatorContext context) {

        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Integer uriId = Integer.parseInt(map.get("id"));

        List<FieldMessage> list = new ArrayList<>();

        Usuario aux = repo.findByEmail(obj.getEmail());
        if (aux != null && !aux.getId().equals(uriId)) {
            list.add(new FieldMessage("email", "Email já existente"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}


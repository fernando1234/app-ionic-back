package com.prototipo.tcc.services.validation;

import com.prototipo.tcc.domain.Usuario;
import com.prototipo.tcc.repositories.UsuarioRepository;
import com.prototipo.tcc.resources.exception.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class UsuarioInsertValidator implements ConstraintValidator<UsuarioInsert, Usuario> {

    @Autowired
    private UsuarioRepository repo;

    @Override
    public void initialize(UsuarioInsert ann) {
    }

    @Override
    public boolean isValid(Usuario obj, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        Usuario aux = repo.findByEmail(obj.getEmail());
        if (aux != null) {
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


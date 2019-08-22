//package com.nelioalves.cursomc.services.validation;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.validation.ConstraintValidator;
//import javax.validation.ConstraintValidatorContext;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.nelioalves.cursomc.domain.Usuario;
//import com.nelioalves.cursomc.repositories.UsuarioRepository;
//import com.nelioalves.cursomc.resources.exception.FieldMessage;
//import com.nelioalves.cursomc.services.validation.utils.BR;
//
//public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
//
//    @Autowired
//    private UsuarioRepository repo;
//
//    @Override
//    public void initialize(ClienteInsert ann) {
//    }
//
//    @Override
//    public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
//
//        List<FieldMessage> list = new ArrayList<>();
//
//        Usuario aux = repo.findByEmail(objDto.getEmail());
//        if (aux != null) {
//            list.add(new FieldMessage("email", "Email já existente"));
//        }
//
//        for (FieldMessage e : list) {
//            context.disableDefaultConstraintViolation();
//            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
//                    .addConstraintViolation();
//        }
//        return list.isEmpty();
//    }
//}
//

package com.mycompany.app.dao;

import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.interfaces.TypeUserDao;
import com.mycompany.app.dao.repositories.TypeUserRepository;
import com.mycompany.app.dto.TypeUserDTO;
import com.mycompany.app.model.TypeUser;
import lombok.Getter;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Service
@Setter
@Getter
@NoArgsConstructor
public class TypeUserImplementation implements TypeUserDao {

    @Autowired
    private TypeUserRepository typeUserRepository;

    @Override
    public TypeUserDTO createTypeUser(TypeUserDTO typeUserDTO) throws Exception {
        try {
            validateTypeUserDTO(typeUserDTO);

            if (typeUserRepository.existsByType(typeUserDTO.getType())) {
                throw new Exception("El tipo de usuario ya existe");
            }

            TypeUser typeUser = Helpers.parse(typeUserDTO);
            return Helpers.parse(typeUserRepository.save(typeUser));
        } catch (Exception e) {
            throw new Exception("Error al crear tipo de usuario", e);
        }
    }

    @Override
    public List<TypeUserDTO> getAllTypeUsers() throws Exception {
        try {
            return typeUserRepository.findAll().stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener todos los tipos de usuario", e);
        }
    }

    @Override
    public TypeUserDTO getTypeUserById(Long id) throws Exception {
        try {
            return typeUserRepository.findById(id)
                    .map(Helpers::parse)
                    .orElseThrow(() -> new Exception("Tipo de usuario no encontrado"));
        } catch (Exception e) {
            throw new Exception("Error al obtener tipo de usuario por ID", e);
        }
    }

    @Override
    public TypeUserDTO updateTypeUser(Long id, TypeUserDTO typeUserDTO) throws Exception {
        try {
            validateTypeUserDTO(typeUserDTO);

            TypeUser existing = typeUserRepository.findById(id)
                    .orElseThrow(() -> new Exception("Tipo de usuario no encontrado"));

            if (!existing.getType().equals(typeUserDTO.getType()) &&
                    typeUserRepository.existsByType(typeUserDTO.getType())) {
                throw new Exception("El tipo de usuario ya existe");
            }

            existing.setType(typeUserDTO.getType());
            return Helpers.parse(typeUserRepository.save(existing));
        } catch (Exception e) {
            throw new Exception("Error al actualizar tipo de usuario", e);
        }
    }

    @Override
    public void deleteTypeUser(Long id) throws Exception {
        try {
            if (!typeUserRepository.existsById(id)) {
                throw new Exception("Tipo de usuario no encontrado");
            }
            typeUserRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception("Error al eliminar tipo de usuario", e);
        }
    }

    // Método de validación reutilizable
    private void validateTypeUserDTO(TypeUserDTO typeUserDTO) throws Exception {
        if (typeUserDTO == null) {
            throw new Exception("DTO no puede ser nulo");
        }
        if (typeUserDTO.getType() == null || typeUserDTO.getType().trim().isEmpty()) {
            throw new Exception("El tipo es requerido");
        }
    }
}
package com.mycompany.app.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.interfaces.UserDao;
import com.mycompany.app.dao.repositories.TypeUserRepository;
import com.mycompany.app.dao.repositories.UserRepository;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.model.Person;
import com.mycompany.app.model.TypeUser;
import com.mycompany.app.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Service
@NoArgsConstructor
@Setter
@Getter
public class Userimplementation implements UserDao {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TypeUserRepository typeUserRepository;

    @Override
    public UserDTO createUser(UserDTO userDTO) throws Exception {
        if (userDTO == null) {
            throw new Exception("El DTO de usuario no puede ser nulo");
        }

        // Convertir DTO a entidad
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());

        // Manejo seguro del TypeUser
        if (userDTO.getTypeUser() != null) {
            TypeUser typeUser = typeUserRepository.findByType(userDTO.getTypeUser().getType())
                    .orElseThrow(() -> new Exception("Tipo de usuario no encontrado"));
            user.setTypeUser(typeUser);
        }
        // Manejo de Person (nuevo)
        if (userDTO.getPerson() != null && userDTO.getPerson().getId() != null) {
            // Aquí deberías verificar que la persona exista
            user.setPerson(new Person()); // Crear instancia mínima
            user.getPerson().setId(userDTO.getPerson().getId()); // Solo establecer el ID
        }

        User savedUser = userRepository.save(user);
        return Helpers.parse(savedUser);
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de usuario inválido");
        }
        if (userDTO == null) {
            throw new Exception("DTO de usuario no puede ser nulo");
        }
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (!userOptional.isPresent()) {
                throw new Exception("Usuario no encontrado con ID: " + id);
            }

            User user = userOptional.get();
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());
            user.setAuthenticated(userDTO.isAuthenticated());

            // Actualizar TypeUser si es necesario
            if (userDTO.getTypeUser() != null) {
                Optional<TypeUser> typeUserOptional = typeUserRepository.findById(userDTO.getTypeUser().getId());
                if (!typeUserOptional.isPresent()) {
                    throw new Exception("Tipo de usuario no encontrado");
                }
                user.setTypeUser(typeUserOptional.get());
            }

            User updatedUser = userRepository.save(user);
            return Helpers.parse(updatedUser);
        } catch (Exception e) {
            throw new Exception("Error al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de usuario inválido");
        }
        try {
            // Get the user first to check associations
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new Exception("Usuario no encontrado con ID: " + id));

            // Delete the user
            userRepository.deleteById(id);

            // If user had a person association, delete the person if no other users
            // reference it
            if (user.getPerson() != null) {
                Long personId = user.getPerson().getId();
                if (!userRepository.existsByPersonId(personId)) {
                    personRepository.deleteById(personId);
                }
            }
        } catch (Exception e) {
            throw new Exception("Error al eliminar usuario: " + e.getMessage());
        }
    }

    @Override
    public boolean existsByUsername(String username) throws Exception {
        try {
            return userRepository.existsByUsername(username);
        } catch (Exception e) {
            throw new Exception("Error al verificar nombre de usuario: " + e.getMessage());
        }
    }

    @Override
    public boolean existsByPassword(String password) throws Exception {
        try {
            // Considera usar hash antes de comparar en producción
            return userRepository.existsByPassword(password);
        } catch (Exception e) {
            throw new Exception("Error al verificar contraseña: " + e.getMessage());
        }
    }

    @Override
    public UserDTO findByUsername(String username) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new Exception("Nombre de usuario no puede estar vacío");
        }

        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new Exception("Usuario no encontrado: " + username);
            }
            return Helpers.parse(user);
        } catch (Exception e) {
            throw new Exception("Error al buscar usuario: " + e.getMessage());
        }
    }

    @Override
    public List<UserDTO> getAllUsers() throws Exception {
        try {
            return userRepository.findAll().stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener todos los usuarios: " + e.getMessage());
        }
    }

    @Override
    public UserDTO getUserById(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de usuario inválido");
        }
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (!userOptional.isPresent()) {
                throw new Exception("Usuario no encontrado con ID: " + id);
            }
            return Helpers.parse(userOptional.get());
        } catch (Exception e) {
            throw new Exception("Error al obtener usuario por ID: " + e.getMessage());
        }
    }

    @Override
    public UserDTO findByPersonDocument(Long document) throws Exception {
        if (document == null) {
            throw new Exception("El documento no puede ser nulo");
        }

        User user = userRepository.findByPersonDocument(document)
                .orElse(null); // Retorna null si no se encuentra

        return Helpers.parse(user); // Convierte a DTO usando tus helpers
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app.dao.repositories;

import com.mycompany.app.model.TypeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<TypeUser, Long> {

    public TypeUser findUserById(Long userId);

    public boolean existsByUsername(String username);

    public TypeUser findByUsername(String username);

}

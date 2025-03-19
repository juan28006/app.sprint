package com.mycompany.app.dao.interfaces;

import com.mycompany.app.Dto.ReservationDTO;

public interface UserDao {

    public ReservationDTO findUserById(long userId) throws Exception;

    public boolean existsByUserName(ReservationDTO userDto) throws Exception;

    public void createUser(ReservationDTO userDto) throws Exception;

    public void updateUser(ReservationDTO userDto) throws Exception;

    public void deleteUser(long id) throws Exception;

    public ReservationDTO findByUserName(ReservationDTO userDto) throws Exception;

}

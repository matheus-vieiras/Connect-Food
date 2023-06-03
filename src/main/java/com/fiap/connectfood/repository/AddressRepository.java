package com.fiap.connectfood.repository;

import com.fiap.connectfood.model.AddressModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<AddressModel, Integer> {

    List<AddressModel> findAllByRua(String rua);
    List<AddressModel> findAllByBairro(String bairro);
    List<AddressModel> findAllByCidade(String cidade);
    List<AddressModel> findAllByEstado(String estado);
    List<AddressModel> findAllByCep(String cep);

}

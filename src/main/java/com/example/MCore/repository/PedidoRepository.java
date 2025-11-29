package com.example.MCore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.MCore.model.Pedido;


@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}

package com.example.H2R_Alexis_Rivera_CC7.controller;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ProductoCotroller {




}
import com.example.demo.model.Producto;

public record ProductoResponseDTO(Long id, String nombre, String categoria, Double precio, Integer stock, boolean activo) {

    public static ProductoResponseDTO desde(Producto producto) {
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria(),
                producto.getPrecio(),
                producto.getStock(),
                producto.isActivo());
    }
}
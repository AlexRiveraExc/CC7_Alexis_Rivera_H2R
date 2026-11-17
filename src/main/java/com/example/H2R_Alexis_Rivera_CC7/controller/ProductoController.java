package com.example.H2R_Alexis_Rivera_CC7.controller;


import com.example.H2R_Alexis_Rivera_CC7.controller.Dto.ProductoResponseDTO;
import com.example.H2R_Alexis_Rivera_CC7.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@CrossOrigin("*")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<com.example.H2R_Alexis_Rivera_CC7.controller.Dto.ProductoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<com.example.H2R_Alexis_Rivera_CC7.controller.Dto.ProductoResponseDTO>> listarActivos() {
        return ResponseEntity.ok(productoService.listarActivos());
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<com.example.H2R_Alexis_Rivera_CC7.controller.Dto.ProductoResponseDTO>> listarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.listarPorCategoria(categoria));
    }

    @PatchMapping("/{id}/desactivacion")
    public ResponseEntity<ProductoResponseDTO> desactivarProducto(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.desactivarProducto(id));
    }
}
package com.example.H2R_Alexis_Rivera_CC7.controller;


import com.example.H2R_Alexis_Rivera_CC7.controller.Dto.CrearPedidoDTO;
import com.example.H2R_Alexis_Rivera_CC7.controller.Dto.PedidoResponseDTO;
import com.example.H2R_Alexis_Rivera_CC7.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin("*")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crearPedido(@RequestBody @Valid CrearPedidoDTO payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crearPedido(payload));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarPorEstado(@RequestParam String estado) {
        return ResponseEntity.ok(pedidoService.listarPorEstado(estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> consultarDetalle(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.consultarDetalle(id));
    }

    @PatchMapping("/{id}/cancelacion")
    public ResponseEntity<PedidoResponseDTO> cancelarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelarPedido(id));
    }

    @PatchMapping("/{id}/procesamiento")
    public ResponseEntity<PedidoResponseDTO> procesarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.procesarPedido(id));
    }

    @PatchMapping("/producto/{idProducto}/cancelar-pendientes")
    public ResponseEntity<List<PedidoResponseDTO>> cancelarPendientesDeProducto(@PathVariable Long idProducto) {
        return ResponseEntity.ok(pedidoService.cancelarPendientesDeProducto(idProducto));
    }
}
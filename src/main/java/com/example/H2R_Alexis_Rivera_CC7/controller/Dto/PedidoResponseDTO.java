package com.example.H2R_Alexis_Rivera_CC7.controller.Dto;


import com.example.H2R_Alexis_Rivera_CC7.model.EstadoPedido;
import com.example.H2R_Alexis_Rivera_CC7.model.Pedido;
import com.example.H2R_Alexis_Rivera_CC7.model.Producto;

public record PedidoResponseDTO(
        Long id,
        String nombreCliente,
        Integer cantidad,
        Double precioUnitario,
        Double total,
        EstadoPedido estado,
        Long productoId,
        String productoNombre,
        String productoCategoria) {

    public static PedidoResponseDTO desde(Pedido pedido) {
        Producto producto = pedido.getProducto();
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getNombreCliente(),
                pedido.getCantidad(),
                pedido.getPrecioUnitario(),
                pedido.getTotal(),
                pedido.getEstado(),
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria());
    }
}

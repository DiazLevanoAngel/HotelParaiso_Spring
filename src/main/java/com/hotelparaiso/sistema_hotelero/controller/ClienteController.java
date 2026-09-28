// controller/ClienteController.java
package com.hotelparaiso.sistema_hotelero.controller;

import com.hotelparaiso.sistema_hotelero.model.Cliente;
import com.hotelparaiso.sistema_hotelero.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ClienteController {

    private ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/clientes")
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listar());
        model.addAttribute("cliente", new Cliente());
        return "paginas/clientes";
    }

    @PostMapping("/clientes/guardar")
    public String guardar(@ModelAttribute Cliente cliente) {
        clienteService.guardarCliente(cliente);
        return "redirect:/clientes";
    }

    // Se deja POST porque clientes.html elimina con un formulario
    @PostMapping("/clientes/eliminar/{id}")
    public String eliminar(@PathVariable int id) {
        clienteService.eliminar(id);
        return "redirect:/clientes";
    }
}
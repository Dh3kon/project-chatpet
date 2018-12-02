package com.chatpet.projectchatpet.controller;

import com.chatpet.projectchatpet.exception.RecursoNaoEncontradoException;
import com.chatpet.projectchatpet.model.Cliente;
import com.chatpet.projectchatpet.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/clientes")
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    @GetMapping("/clientes/{id}")
    public  Cliente getClienteById(@PathVariable("id") Long clienteId){
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("ClienteId " + clienteId + " not found."));
    }

    @PostMapping("/clientes")
    public Cliente createCliente(@Valid @RequestBody Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @PutMapping("/clientes/{clienteId}")
    public Cliente updateCliente(@PathVariable("clienteId") Long clienteId, @Valid @RequestBody
            Cliente clienteRequest) {
        return clienteRepository.findById(clienteId).map(cliente -> {
            cliente.setNome(clienteRequest.getNome());
            cliente.setEmail(clienteRequest.getEmail());
            cliente.setPassword(clienteRequest.getPassword());
            return clienteRepository.save(cliente);
        }).orElseThrow(() -> new RecursoNaoEncontradoException("ClienteId " + clienteId + " not found."));
    }

    @DeleteMapping("/clientes/{clienteId}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long clienteId) {
        return clienteRepository.findById(clienteId).map(cliente -> {
            clienteRepository.delete(cliente);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new RecursoNaoEncontradoException("ClienteId " + clienteId + " not found."));
    }
}

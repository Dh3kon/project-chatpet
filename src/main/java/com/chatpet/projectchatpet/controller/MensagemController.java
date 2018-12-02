package com.chatpet.projectchatpet.controller;

import com.chatpet.projectchatpet.exception.RecursoNaoEncontradoException;
import com.chatpet.projectchatpet.model.Mensagem;
import com.chatpet.projectchatpet.repository.ClienteRepository;
import com.chatpet.projectchatpet.repository.MensagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class MensagemController {

    @Autowired
    private MensagemRepository mensagemRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/mensagens")
    private List<Mensagem> getAllMensagens() {
        return mensagemRepository.findAll();
    }

    @GetMapping("/clientes/{clienteId}/mensagens")
    public Page<Mensagem> getAllMensagensByClienteId(@PathVariable("clienteId") Long clienteId,
                                                     Pageable pageable) {
        return mensagemRepository.findByClienteId(clienteId, pageable);
    }

    @PostMapping("/clientes/{clienteId}/mensagens")
    public Mensagem createMensagem(@PathVariable("clienteId") Long clienteId,
                                   @Valid @RequestBody Mensagem mensagem) {
        return clienteRepository.findById(clienteId).map(cliente -> {
            mensagem.setCliente(cliente);
            return mensagemRepository.save(mensagem);
        }).orElseThrow(() -> new RecursoNaoEncontradoException("ClienteId " + clienteId + " not found."));
    }

    @PutMapping("/clientes/{clienteId}/mensagens/{mensagemId}")
    public Mensagem updateMensagem(@PathVariable("clienteId") Long clienteId,
                                   @PathVariable("mensagemId") Long mensagemId,
                                   @Valid @RequestBody Mensagem mensagemRequest) {
        if (!clienteRepository.existsById(clienteId))
            throw new RecursoNaoEncontradoException("ClienteId " + clienteId + " not found.");
        return mensagemRepository.findById(mensagemId).map(mensagem -> {
            mensagem.setText(mensagemRequest.getText());
            return mensagemRepository.save(mensagem);
        }).orElseThrow(() -> new RecursoNaoEncontradoException("ClienteId " + clienteId + " not found."));
    }

    @DeleteMapping("/clientes/{clienteId}/mensagens/{mensagemId}")
    public ResponseEntity<?> deleteMensagem(@PathVariable("clienteId") Long clienteId,
                                            @PathVariable("mensagemId") Long mensgagemId) {
        if (!clienteRepository.existsById(clienteId))
            throw new RecursoNaoEncontradoException("ClienteId " + clienteId + " not found.");
        return mensagemRepository.findById(mensgagemId).map(mensagem -> {
            mensagemRepository.delete(mensagem);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new RecursoNaoEncontradoException("ClienteId " + clienteId + " not found."));
    }
}

package com.chatpet.projectchatpet.controller;

import com.chatpet.projectchatpet.exception.RecursoNaoEncontradoException;
import com.chatpet.projectchatpet.model.Comentario;
import com.chatpet.projectchatpet.repository.ComentarioRepository;
import com.chatpet.projectchatpet.repository.MensagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ComentarioController {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private MensagemRepository mensagemRepository;

    @GetMapping("/mensagens/{mensagemId}/comentarios")
    public Page<Comentario> getAllComentariosByMensagemId(@PathVariable("mensagemId") Long mensagemId,
                                                          Pageable pageable) {
        return comentarioRepository.findByMensagemId(mensagemId, pageable);
    }

    @PostMapping("/mensagens/{mensagemId}/comentarios")
    public Comentario createComentario(@PathVariable("mensagemId") Long mensagemId,
                                       @Valid @RequestBody Comentario comentario) {
        return mensagemRepository.findById(mensagemId).map(mensagem -> {
            comentario.setMensagem(mensagem);
            return comentarioRepository.save(comentario);
        }).orElseThrow(() -> new RecursoNaoEncontradoException("MensagemId " + mensagemId + " not found."));
    }

    @PutMapping("/mensagens/{mensagemId}/comentarios/{comentarioId}")
    public Comentario updateComentario(@PathVariable("mensagemId") Long mensagemId,
                                       @PathVariable("comentarioId") Long comentarioId,
                                       @Valid @RequestBody Comentario comentarioRequest) {
        if (!mensagemRepository.existsById(mensagemId))
            throw new RecursoNaoEncontradoException("MensagemId " + mensagemId + " not found.");
        return comentarioRepository.findById(comentarioId).map(comentario -> {
            comentario.setTexto(comentarioRequest.getTexto());
            return comentarioRepository.save(comentario);
        }).orElseThrow(() -> new RecursoNaoEncontradoException("MensagemId " + mensagemId + " not found."));
    }

    @DeleteMapping("/mensagens/{mensagemId}/comentarios/{comentarioId}")
    public ResponseEntity<?> deleteComentario(@PathVariable("mensagemId") Long mensagemId,
                                              @PathVariable("comentarioId") Long comentarioId) {
        if (!mensagemRepository.existsById(mensagemId))
            throw new RecursoNaoEncontradoException("MensagemId " + mensagemId + " not found.");
        return comentarioRepository.findById(comentarioId).map(comentario -> {
            comentarioRepository.delete(comentario);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new RecursoNaoEncontradoException("MensagemId " + mensagemId + " not found."));
    }
}

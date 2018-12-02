package com.chatpet.projectchatpet.repository;

import com.chatpet.projectchatpet.model.Comentario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    Page<Comentario> findByMensagemId(Long mensagemId, Pageable pageable);
}

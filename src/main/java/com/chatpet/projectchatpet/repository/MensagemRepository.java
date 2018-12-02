package com.chatpet.projectchatpet.repository;

import com.chatpet.projectchatpet.model.Mensagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
    Page<Mensagem> findByClienteId(Long clienteId, Pageable pageable);
}

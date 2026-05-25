package br.com.fiap.clyvovet.repository;

import br.com.fiap.clyvovet.entity.Agendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    Page<Agendamento> findByStatus(String status, Pageable pageable);
    Page<Agendamento> findByTipo(String tipo, Pageable pageable);
    Page<Agendamento> findByPetId(Long petId, Pageable pageable);
    Page<Agendamento> findByVeterinarioId(Long veterinarioId, Pageable pageable);
}

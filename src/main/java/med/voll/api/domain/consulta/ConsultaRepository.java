package med.voll.api.domain.consulta;

import med.voll.api.domain.medico.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    Page<Consulta> findByMotivoCancelamientoIsNull(Pageable paginacion);
    Boolean existsByPacienteIdAndFechaBetween(Long idPaciente, LocalDateTime primerHorario,
                                              LocalDateTime ultimoHorario);
    Boolean existsByMedicoIdAndFecha(Long idMedico, LocalDateTime fecha);
}

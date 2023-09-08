package med.voll.api.domain.consulta.validaciones;

import med.voll.api.domain.consulta.Consulta;

import java.time.LocalDateTime;

public record DatosListadoConsulta(Long id, Long idPaciente, Long idMedico, LocalDateTime fecha) {
    public DatosListadoConsulta(Consulta consulta) {
        this(consulta.getId(), consulta.getPaciente().getId(), consulta.getMedico().getId(), consulta.getFecha());
    }
}

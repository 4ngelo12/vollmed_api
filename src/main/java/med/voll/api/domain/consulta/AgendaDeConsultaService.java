package med.voll.api.domain.consulta;

import med.voll.api.domain.consulta.validaciones.DatosListadoConsulta;
import med.voll.api.domain.consulta.validaciones.ValidadorCancelamientoConsulta;
import med.voll.api.domain.consulta.validaciones.ValidadorDeConsulta;
import med.voll.api.domain.medico.DatosListadoMedico;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errores.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultaService {
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    List<ValidadorDeConsulta> validadores;
    @Autowired
    List<ValidadorCancelamientoConsulta> validadoresCancelamiento;

    public DatosDetalleConsulta agendar(DatosAgendarConsulta datos) {
        if (!pacienteRepository.findById(datos.idPaciente()).isPresent()) {
            throw new ValidacionDeIntegridad("El id ingresado para el paciente no fue encontrado");
        }
        if (datos.idMedico() != null && !medicoRepository.existsById(datos.idMedico())) {
            throw new ValidacionDeIntegridad("El id ingresado para el medico no fue encontrado");
        }

        validadores.forEach(v -> v.validar(datos));
        //Validaciones
        var paciente = pacienteRepository.findById(datos.idPaciente()).get();
        var medico = seleccionarMedico(datos);

        if (medico == null) {
            throw new ValidacionDeIntegridad("No existen medicos disponibles para este horario y especialidad");
        }

        var consulta = new Consulta(medico, paciente, datos.fecha());
        consultaRepository.save(consulta);

        return new DatosDetalleConsulta(consulta);
    }

    private Medico seleccionarMedico(DatosAgendarConsulta datos) {
        if (datos.idMedico() != null) {
            return medicoRepository.getReferenceById(datos.idMedico());
        }
        if (datos.especialidad() == null) {
            throw new ValidacionDeIntegridad("Debe seleccionarse una especialidad para el medico");
        }
        return medicoRepository.seleccionarMedicoConEspecialidadEnFecha(datos.especialidad(), datos.fecha());
    }

    public void cancelar(DatosCancelarConsulta datos) {
        if (!consultaRepository.existsById(datos.id())) {
            throw new ValidacionDeIntegridad("El id de la consulta informada no existe");
        }

        validadoresCancelamiento.forEach(v -> v.validar(datos));

        var consulta = consultaRepository.getReferenceById(datos.id());
        consulta.cancelar(datos.motivoCancelamiento());
    }

    public Page<DatosListadoConsulta> listarConsultas(@PageableDefault(size = 4, page = 0) Pageable paginacion) {
        return consultaRepository.findByMotivoCancelamientoIsNull(paginacion).map(DatosListadoConsulta::new);
    }
}

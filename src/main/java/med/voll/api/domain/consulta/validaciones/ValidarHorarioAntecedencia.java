package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosCancelarConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component("validarHorarioAntecedenciaCancelamiento")
public class ValidarHorarioAntecedencia implements ValidadorCancelamientoConsulta {
    @Autowired
    private ConsultaRepository consultaRepository;

    @Override
    public void validar(DatosCancelarConsulta datos) {
        var consulta = consultaRepository.getReferenceById(datos.id());
        var ahora = LocalDateTime.now();
        var diferenciaEntreHoras = Duration.between(ahora, consulta.getFecha()).toHours();

        if (diferenciaEntreHoras < 24) {
            throw new ValidationException("Las consultas solo pueden ser canceladas con antecedencia " +
                    "mínima de 24 horas");
        }
    }
}
package med.voll.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.voll.api.domain.consulta.*;
import med.voll.api.domain.consulta.validaciones.DatosListadoConsulta;
import med.voll.api.domain.medico.DatosListadoMedico;
import med.voll.api.infra.errores.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
@SecurityRequirement(name = "bearer-key")
public class ConsultaController {
    @Autowired
    private AgendaDeConsultaService consultaService;

    @PostMapping
    @Transactional
    @Operation(
            summary = "Registra una consulta en la base de datos",
            description = "",
            tags = { "consulta", "post" })
    public ResponseEntity agendar(@RequestBody @Valid DatosAgendarConsulta datos)
            throws ValidacionDeIntegridad {
        var response = consultaService.agendar(datos);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
            summary = "Lista las consultas registradas en la base de datos",
            description = "",
            tags = { "consulta", "get" })
    public ResponseEntity<Page<DatosListadoConsulta>> listarConsultas(Pageable paginacion) {
        return ResponseEntity.ok(consultaService.listarConsultas(paginacion));
    }

    @DeleteMapping
    @Transactional
    @Operation(
            summary = "Cancela las consultas",
            description = "",
            tags = { "consulta", "delete" })
    public ResponseEntity Cancelar(@RequestBody @Valid DatosCancelarConsulta datos) {
        consultaService.cancelar(datos);
        return ResponseEntity.noContent().build();
    }
}

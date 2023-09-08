package med.voll.api.domain.persona;

import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import med.voll.api.domain.direccion.Direccion;

@Getter
@EqualsAndHashCode
@MappedSuperclass
public class Persona {
    protected String nombre;
    protected String email;
    protected String telefono;
    protected Boolean activo;
    @Embedded
    protected Direccion direccion;

    public void desactivarPersona() {
        this.activo = false;
    }
}

package pe.edu.vallegrande.hackatontemplate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PRODUCTO")
public class Producto {

    @Id
    private Long id;

    private String codigo;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;

    @Builder.Default
    private Boolean estado = true;

    @CreatedDate
    private LocalDateTime fechaRegistro;
}

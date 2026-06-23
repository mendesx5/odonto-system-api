package com.odonto.odonto_system.procedure;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record ProcedureRequest(
        @NotEmpty(message = "S lista de IDs de procedimentos não pode estar vazia")
        List<UUID> procedureIds
) { }

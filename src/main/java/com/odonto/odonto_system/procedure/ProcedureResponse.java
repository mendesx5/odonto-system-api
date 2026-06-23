package com.odonto.odonto_system.procedure;

import java.util.UUID;

public record ProcedureResponse(
        UUID id,
        String name,
        String category,
        Integer defaultDurationMinutes,
        String description
) {
    public ProcedureResponse(ProcedureCatalog catalog) {
        this(
                catalog.getId(),
                catalog.getName(),
                catalog.getCategory(),
                catalog.getDefaultDurationMinutes(),
                catalog.getDescription()
        );
    }
}

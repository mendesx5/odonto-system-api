package com.odonto.odonto_system.tooth;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OdontogramResponse(

        UUID patientId,
        String patientName,
        List<ToothRecordResponse> teeth,
        LocalDateTime lastUpdated,
        String updatedBy

) {
}

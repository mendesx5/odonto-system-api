package com.odonto.odonto_system.appointment;

import java.time.LocalTime;

public record FreeSlotResponse(

        LocalTime startTime,

        LocalTime endTime

) { }

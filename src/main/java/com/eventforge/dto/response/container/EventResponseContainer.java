package com.eventforge.dto.response.container;

import com.eventforge.dto.response.OneTimeEventResponse;
import com.eventforge.dto.response.RecurrenceEventResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class EventResponseContainer {
    private List<OneTimeEventResponse> oneTimeEvents;
    private List<RecurrenceEventResponse> recurrenceEvents;

}

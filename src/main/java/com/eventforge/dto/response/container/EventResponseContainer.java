package com.eventforge.dto.response.container;

import com.eventforge.dto.response.OneTimeEventResponse;
import com.eventforge.dto.response.RecurrenceEventResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class EventResponseContainer {
    private List<OneTimeEventResponse> oneTimeEvents = new ArrayList<>();
    private List<RecurrenceEventResponse> recurrenceEvents = new ArrayList<>();


    public EventResponseContainer(List<OneTimeEventResponse> oneTimeEvents , List<RecurrenceEventResponse> recurrenceEvents){
        addOneTimeEvents(oneTimeEvents);
        addRecurrenceEvents(recurrenceEvents);
    }

    private void addOneTimeEvents(List<OneTimeEventResponse> oneTimeEvents){
        if(oneTimeEvents!=null){
            this.oneTimeEvents.addAll(oneTimeEvents);
        }
    }

    private void addRecurrenceEvents(List<RecurrenceEventResponse> recurrenceEvents){
        if(recurrenceEvents!=null){
            this.recurrenceEvents.addAll(recurrenceEvents);
        }
    }
}

package com.eventforge.dto.request;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CriteriaFilterRequest {
        private Boolean isEvent; //// we will hardcore this on the frontend according to which html/template we are.
        private boolean sortByExpired; // we will hardcore this on the frontend according to which html/template we are.
        @Nullable
        private String value;
        @Nullable
        private LocalDate startsAt;
        @Nullable
        private LocalDate endsAt;
}

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
        private Boolean isOneTime; //// we will hardcore this on the frontend according to which html/template we are.
        private boolean sortByExpired; // we will hardcore this on the frontend according to which html/template we are.
        @Nullable
        private String name;
        @Nullable
        private String description;
        @Nullable
        private String address;
        @Nullable
        private String organisationName;
        @Nullable
        private Integer minAge;
        @Nullable
        private Integer maxAge;
        @Nullable
        private Boolean isOnline;
        @Nullable
        private String eventCategories;
        @Nullable
        private LocalDate startsAt;
        @Nullable
        private LocalDate endsAt;
}

package com.eventforge.dto.request;

import jakarta.annotation.Nullable;
import lombok.*;

import java.time.LocalDateTime;

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
        private LocalDateTime startsAt;
        @Nullable
        private LocalDateTime endsAt;
}

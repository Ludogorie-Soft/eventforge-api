package com.eventforge.dto.response;

    import lombok.*;

    import java.time.LocalDateTime;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public class OrganisationResponseForAdmin {

        private Long userId;

        private String logo;

        private String email;

        private boolean isEnabled;

        private boolean isApprovedByAdmin;

        private boolean isNonLocked;

        private Integer countEvents;

        private LocalDateTime registeredAt;

        private LocalDateTime updatedAt;

    }

package com.eventforge.dto.response;

    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.time.LocalDateTime;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
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

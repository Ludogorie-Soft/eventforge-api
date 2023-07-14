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

        private Long orgId;
        private String orgName;

        private String fullName;

        private String phoneNumber;

        private String email;

        private boolean isEnabled;

        private boolean isApprovedByAdmin;

        private boolean isNonLocked;

        private LocalDateTime registeredAt;

    }

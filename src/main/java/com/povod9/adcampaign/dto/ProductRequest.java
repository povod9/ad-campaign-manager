package com.povod9.adcampaign.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductRequest(
        @NotBlank String name
) {
}

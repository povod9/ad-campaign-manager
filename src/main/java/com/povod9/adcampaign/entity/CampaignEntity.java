package com.povod9.adcampaign.entity;

import com.povod9.adcampaign.enums.Status;
import com.povod9.adcampaign.enums.TownName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class CampaignEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campaignId;

    @Column(nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(name = "key_word", joinColumns = @JoinColumn(name = "campaign_id"))
    @Column(nullable = false)
    private List<String> keywords = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal bidAmount;

    @Column(nullable = false)
    private BigDecimal campaignFund;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TownName town;

    @Column(nullable = false)
    private int radius;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}

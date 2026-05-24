package com.povod9.adcampaign.controller;

import com.povod9.adcampaign.dto.CampaignRequest;
import com.povod9.adcampaign.dto.CampaignResponse;
import com.povod9.adcampaign.dto.CampaignUpdateRequest;
import com.povod9.adcampaign.service.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/campaigns")
public class CampaignController {

    private final CampaignService service;


    @GetMapping("/{id}")
    public ResponseEntity<CampaignResponse> campaignById(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.campaignById(id));
    }

    @GetMapping
    public ResponseEntity<List<CampaignResponse>> allCampaigns(){
        return ResponseEntity.ok(service.allCampaigns());
    }

    @PostMapping
    public ResponseEntity<CampaignResponse> creatCampaign(@RequestBody @Valid CampaignRequest campaignRequest){
        var campaignToCreate = service.createCampaign(campaignRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(campaignToCreate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampaignResponse> updateById(@PathVariable("id") Long id, @RequestBody @Valid CampaignUpdateRequest campaignUpdateRequest){
        return ResponseEntity.ok(service.updateById(id, campaignUpdateRequest));
    }
}

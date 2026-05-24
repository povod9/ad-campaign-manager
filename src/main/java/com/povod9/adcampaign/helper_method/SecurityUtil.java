package com.povod9.adcampaign.helper_method;

import com.povod9.adcampaign.dto.PrincipalDto;
import com.povod9.adcampaign.exception.InvalidCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static PrincipalDto getCurrentPrincipalOrThrow(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getPrincipal() == null){
            throw new InvalidCredentialsException("You are unauthorized");
        }
        return (PrincipalDto) authentication.getPrincipal();
    }
}

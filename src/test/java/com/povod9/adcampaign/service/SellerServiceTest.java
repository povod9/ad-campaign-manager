 package com.povod9.adcampaign.service;

 import com.povod9.adcampaign.dto.*;
 import com.povod9.adcampaign.entity.SellerEntity;
 import com.povod9.adcampaign.exception.InvalidCredentialsException;
 import com.povod9.adcampaign.mapper.SellerMapper;
 import com.povod9.adcampaign.repository.SellerRepository;
 import com.povod9.adcampaign.security.JwtCore;
 import jakarta.persistence.EntityNotFoundException;
 import org.junit.jupiter.api.Test;
 import org.junit.jupiter.api.extension.ExtendWith;
 import org.mockito.ArgumentCaptor;
 import org.mockito.InjectMocks;
 import org.mockito.Mock;
 import org.mockito.junit.jupiter.MockitoExtension;
 import org.springframework.security.crypto.password.PasswordEncoder;

 import java.math.BigDecimal;
 import java.util.Optional;


 import static org.junit.jupiter.api.Assertions.*;
 import static org.mockito.ArgumentMatchers.any;
 import static org.mockito.Mockito.*;

 @ExtendWith(MockitoExtension.class)
 class SellerServiceTest {


     @Mock
     private SellerRepository repository;

     @Mock
     private PasswordEncoder passwordEncoder;

     @Mock
     private SellerMapper mapper;

     @Mock
     private JwtCore jwtCore;

     @Mock
     private SecurityContextServiceImpl securityContextService;

     @InjectMocks
     private SellerServiceImpl service;

     @Test
    void createSellerAccount() {
         SellerResponse sellerResponse = createSellerResponse();
         SellerEntity sellerEntity = createSellerEntity();
         SellerRequest sellerRequest = createSellerRequest();

        when(repository.existsByEmail(sellerEntity.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(sellerEntity.getPassword())).thenReturn("HashPassword");
        when(repository.save(any(SellerEntity.class))).thenReturn(sellerEntity);
        when(mapper.entityToResponse(any(SellerEntity.class))).thenReturn(sellerResponse);

        service.createSellerAccount(sellerRequest);
        ArgumentCaptor<SellerEntity> sellerEntityArgumentCaptor = ArgumentCaptor.forClass(SellerEntity.class);
        verify(repository).save(sellerEntityArgumentCaptor.capture());
        SellerEntity saveSellerEntity = sellerEntityArgumentCaptor.getValue();

        assertEquals(sellerEntity.getSellerName(),saveSellerEntity.getSellerName());
        assertEquals(sellerEntity.getEmail(),saveSellerEntity.getEmail());
        assertEquals("HashPassword",saveSellerEntity.getPassword());
        assertEquals(sellerEntity.getEmeraldAmountFunds(),saveSellerEntity.getEmeraldAmountFunds());
    }

    @Test
    void throwIfEmailExists(){
        SellerEntity sellerEntity = createSellerEntity();
        SellerRequest sellerRequest = createSellerRequest();

        when(repository.existsByEmail(sellerEntity.getEmail())).thenReturn(true);

        assertThrows(
                IllegalArgumentException.class, () -> service.createSellerAccount(sellerRequest)
        );

        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(mapper);
        verify(repository, never()).save(any(SellerEntity.class));
    }

    @Test
    void loginSeller() {
        LoginRequest loginRequest = createLoginRequest();
        LoginResponse loginResponse = createLoginResponse();
        SellerEntity sellerEntity = createSellerEntity();

        when(repository.findByEmail(loginRequest.email())).thenReturn(Optional.of(sellerEntity));
        when(passwordEncoder.matches(loginRequest.password(), sellerEntity.getPassword())).thenReturn(true);
        when(jwtCore.generateToken(sellerEntity)).thenReturn(loginResponse.accessToken());

        var actualResponse = service.loginSeller(loginRequest);
        assertEquals(loginResponse.accessToken(), actualResponse.accessToken());
        assertEquals(loginResponse.tokenType(), actualResponse.tokenType());
    }

    @Test
    void throwIfBadEmail(){
        LoginRequest loginRequest = createLoginRequest();

        when(repository.findByEmail(loginRequest.email())).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class, () -> service.loginSeller(loginRequest)
        );

        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtCore);
    }

    @Test
    void throwIfBadPassword(){
        LoginRequest loginRequest = createLoginRequest();
        SellerEntity sellerEntity = createSellerEntity();

        when(repository.findByEmail(loginRequest.email())).thenReturn(Optional.of(sellerEntity));
        when(passwordEncoder.matches(loginRequest.password(), sellerEntity.getPassword())).thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class, () -> service.loginSeller(loginRequest)
        );

        verifyNoInteractions(jwtCore);
    }

    @Test
    void updateSellerEmail() {
        SellerEntity sellerEntity = createSellerEntity();
        SellerUpdateRequest sellerUpdateRequest = createEmailSellerUpdateRequest();
        PrincipalDto principalDto = createPrincipalDto();

        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        when(repository.findById(principalDto.id())).thenReturn(Optional.of(sellerEntity));
        when(repository.existsByEmail(sellerUpdateRequest.email())).thenReturn(false);
        when(mapper.entityToResponse(any(SellerEntity.class))).thenAnswer(invocationOnMock -> {
            SellerEntity newSellerEntity = invocationOnMock.getArgument(0);
            return new SellerResponse(
                    newSellerEntity.getSellerId(),
                    newSellerEntity.getSellerName(),
                    newSellerEntity.getEmail(),
                    newSellerEntity.getEmeraldAmountFunds()
            );
        });

        var savedSeller = service.updateSeller(sellerUpdateRequest);
        assertEquals(sellerEntity.getEmail(), savedSeller.email());
        verify(mapper).updateEntityFromResponse(any(),any());
    }

    @Test
    void dontUpdateIfEmailExist(){
        SellerEntity sellerEntity = createSellerEntity();
        SellerUpdateRequest sellerUpdateRequest = createEmailSellerUpdateRequest();
        PrincipalDto principalDto = createPrincipalDto();

        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        when(repository.findById(principalDto.id())).thenReturn(Optional.of(sellerEntity));
        when(repository.existsByEmail(sellerUpdateRequest.email())).thenReturn(true);

        assertThrows(
                IllegalArgumentException.class, () -> service.updateSeller(sellerUpdateRequest)
        );

        verify(mapper, never()).entityToResponse(any());
        verify(mapper, never()).updateEntityFromResponse(any(), any());
    }

    @Test
    void updateSellerPassword(){
        SellerEntity sellerEntity = createSellerEntity();
        SellerUpdateRequest sellerUpdateRequest = createPasswordSellerUpdateRequest();
        PrincipalDto principalDto = createPrincipalDto();

        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        when(repository.findById(principalDto.id())).thenReturn(Optional.of(sellerEntity));
        when(passwordEncoder.encode(sellerUpdateRequest.password())).thenReturn("NewHashPassword");

        when(mapper.entityToResponse(any(SellerEntity.class))).thenAnswer(invocationOnMock -> {
            SellerEntity newSellerEntity = invocationOnMock.getArgument(0);
            return new SellerResponse(
                    newSellerEntity.getSellerId(),
                    newSellerEntity.getSellerName(),
                    null,
                    newSellerEntity.getEmeraldAmountFunds()
            );
        });

        service.updateSeller(sellerUpdateRequest);
        assertEquals("NewHashPassword", sellerEntity.getPassword());
        verify(mapper).updateEntityFromResponse(any(),any());
    }

    @Test
    void dontUpdateIfPasswordSame(){
        SellerEntity sellerEntity = createSellerEntity();
        SellerUpdateRequest sellerUpdateRequest = createPasswordSellerUpdateRequest();
        PrincipalDto principalDto = createPrincipalDto();

        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        when(repository.findById(principalDto.id())).thenReturn(Optional.of(sellerEntity));
        when(passwordEncoder.matches(sellerUpdateRequest.password(), sellerEntity.getPassword())).thenReturn(true);

        assertThrows(
                IllegalArgumentException.class, () -> service.updateSeller(sellerUpdateRequest)
        );

        verify(passwordEncoder, never()).encode(anyString());
        verify(mapper, never()).updateEntityFromResponse(any(), any());
    }

    private PrincipalDto createPrincipalDto(){
         return new PrincipalDto(
                 1L,
                 "johndoe@example.com"
         );
    }
    private SellerUpdateRequest createEmailSellerUpdateRequest(){
         return new SellerUpdateRequest(
                 null,
                 "johndoetwo@example.com",
                 null
         );
    }

    private SellerUpdateRequest createPasswordSellerUpdateRequest(){
         return new SellerUpdateRequest(
                 null,
                 null,
                 "Qwerty12341!"
         );
    }
    private SellerResponse createSellerResponse(){
        return new SellerResponse(
                1L,
                "John Doe",
                "johndoe@example.com",
                BigDecimal.valueOf(5000)
        );
    }

    private LoginRequest createLoginRequest(){
         return new LoginRequest(
                 "johndoe@example.com",
                 "Qwerty1234!"
         );
    }
    private LoginResponse createLoginResponse(){
         return new LoginResponse(
                 "someToken",
                 "Bearer"
         );
    }
    private SellerEntity createSellerEntity(){
        return new SellerEntity(
                1L,
                "John Doe",
                "johndoe@example.com",
                "Qwerty1234!",
                BigDecimal.valueOf(5000)
        );
    }
    private SellerRequest createSellerRequest(){
        return new SellerRequest(
                "John Doe",
                "johndoe@example.com",
                "Qwerty1234!",
                BigDecimal.valueOf(5000)
        );
    }
 }

package imjangdan.ddps.service;

import imjangdan.ddps.dto.request.member.MemberLoginDto;
import imjangdan.ddps.dto.request.member.MemberRegisterDto;
import imjangdan.ddps.dto.request.member.MemberUpdateDto;
import imjangdan.ddps.dto.response.member.MemberResponseDto;
import imjangdan.ddps.dto.response.member.MemberTokenDto;
import imjangdan.ddps.dto.response.member.ResMemberListDto;
import imjangdan.ddps.entity.Member;
import imjangdan.ddps.repository.MemberRepository;
import imjangdan.ddps.security.jwt.CustomUserDetailsService;
import imjangdan.ddps.security.jwt.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllMembers() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        Page<Member> membersPage = new PageImpl<>(Collections.singletonList(new Member()));
        when(memberRepository.findAll(pageable)).thenReturn(membersPage);

        // Act
        Page<ResMemberListDto> result = memberService.getAllMembers(pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void checkIdDuplicate() {
        // Arrange
        String email = "test@example.com";
        when(memberRepository.findByEmail(email)).thenReturn(java.util.Optional.empty());

        // Act
        HttpStatus result = memberService.checkIdDuplicate(email);

        // Assert
        assertEquals(HttpStatus.OK, result);
    }

    @Test
    void register() {
        // Arrange
        MemberRegisterDto registerDto = new MemberRegisterDto();
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("password");
        registerDto.setPasswordCheck("password");

        when(memberRepository.findByEmail(registerDto.getEmail())).thenReturn(java.util.Optional.empty());
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword");
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> {
            Member member = invocation.getArgument(0);
            member.setId(1L);
            return member;
        });

        // Act
        MemberResponseDto result = memberService.register(registerDto);

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void login() {
        // Arrange
        MemberLoginDto loginDto = new MemberLoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(loginDto.getEmail())).thenReturn(userDetails);
        when(passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword())).thenReturn(true);

        String token = "testToken";
        when(jwtTokenUtil.generateToken(userDetails)).thenReturn(token);

        // Act
        MemberTokenDto result = memberService.login(loginDto);

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals(token, result.getToken());
    }

    @Test
    void check() {
        // Arrange
        Member member = new Member();
        member.setEmail("test@example.com");
        member.setPassword("encodedPassword");

        when(userDetailsService.loadUserByUsername(member.getEmail())).thenReturn(member);

        // Act & Assert
        assertDoesNotThrow(() -> memberService.check(member, "password"));
    }

    @Test
    void update() {
        // Arrange
        Member member = new Member();
        member.setEmail("test@example.com");

        MemberUpdateDto updateDto = new MemberUpdateDto();
        updateDto.setPassword("newPassword");
        updateDto.setPasswordCheck("newPassword");

        when(passwordEncoder.encode(updateDto.getPassword())).thenReturn("encodedNewPassword");
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(java.util.Optional.of(member));

        // Act
        MemberResponseDto result = memberService.update(member, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }
}

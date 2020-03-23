package kr.joyful.doit.jwt;

import kr.joyful.doit.jwt.dto.JwtAuthenticationRequest;
import kr.joyful.doit.jwt.dto.JwtAuthenticationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class JwtAuthenticationController {

    private final JwtAuthenticationGenerator jwtAuthenticationGenerator;
    private final UserDetailsService userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/api/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws Exception {
        final UserDetails userDetails = userDetailService.loadUserByUsername(authenticationRequest.getEmail());
        if(!passwordEncoder.matches(authenticationRequest.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Password not matched");
        }
        JwtAuthenticationDto authentication = jwtAuthenticationGenerator.createJwtAuthenticationFromUserDetails(userDetails);
        return ResponseEntity.ok(authentication);
    }

    @PostMapping("/api/re-authenticate")
    public ResponseEntity<?> regenerateAuthenticationToken(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String refreshToken) throws Exception {

        if(!refreshToken.startsWith("Bearer") && jwtTokenUtil.validateToken(refreshToken.substring(7))) {
            throw new BadCredentialsException("Invalid Token");
        }

        final UserDetails userDetails = userDetailService.loadUserByUsername(jwtTokenUtil.getIdFromToken(refreshToken.substring(7)));
        JwtAuthenticationDto authentication = jwtAuthenticationGenerator.createJwtAuthenticationFromUserDetails(userDetails);
        return ResponseEntity.ok(authentication);
    }
}
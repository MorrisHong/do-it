package kr.joyful.doit.jwt;

import kr.joyful.doit.jwt.dto.JwtAuthenticationRequest;
import kr.joyful.doit.jwt.dto.JwtAuthenticationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class JwtAuthenticationController {

    private final JwtAuthenticationGenerator jwtAuthenticationGenerator;
    private final UserDetailsService userDetailService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/api/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws Exception {
        final UserDetails userDetails = userDetailService.loadUserByUsername(authenticationRequest.getEmail());
        if(!passwordEncoder.matches(authenticationRequest.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Password not matched");
        }
        JwtAuthenticationDto authentication = jwtAuthenticationGenerator.createJwtAuthenticationFromUserDetails(userDetails);
        return ResponseEntity.ok(authentication);
    }
}
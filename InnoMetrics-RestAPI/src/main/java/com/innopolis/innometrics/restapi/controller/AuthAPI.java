package com.innopolis.innometrics.restapi.controller;

import com.innopolis.innometrics.restapi.config.JwtToken;
import com.innopolis.innometrics.restapi.dto.AuthRequest;
import com.innopolis.innometrics.restapi.dto.AuthResponse;
import com.innopolis.innometrics.restapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
@RequiredArgsConstructor
public class AuthAPI {
    private final AuthenticationManager authenticationManager;
    @Autowired
    private JwtToken jwtToken;
    private final UserService userService;
    private static final Logger LOG = LogManager.getLogger();


    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authenticationRequest) throws Exception {
        LOG.info("Request received from:{} ", authenticationRequest.getEmail());
        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        final UserDetails userDetails = userService
                .loadUserByUsername(authenticationRequest.getEmail());

        final String token = jwtToken.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}

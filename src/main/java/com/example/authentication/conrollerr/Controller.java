package com.example.authentication.conrollerr;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.authentication.commons.UniversalResponse;
import com.example.authentication.dto.RoleToUser;
import com.example.authentication.model.AppUser;
import com.example.authentication.model.Role;
import com.example.authentication.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("api")
public class Controller{
    private final UserService userService;
    @PostMapping("/add/user")
    ResponseEntity <UniversalResponse>addUsers(@RequestBody AppUser appUser){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequestUri().path("/api/add/user").toUriString());
        AppUser appUser1 = userService.saveUser(appUser);
        return ResponseEntity.created(uri).body(UniversalResponse.builder().status(200).message("user created").data(appUser1).build());
    }
    @GetMapping("/get/user")
    ResponseEntity <UniversalResponse>getUsers(){
       List<AppUser> userList = userService.getUser();
       return ResponseEntity.ok().body(UniversalResponse.builder().status(200).message("user retrieved").data(userList).build());
    }
    @PostMapping("/add/role")
    ResponseEntity <UniversalResponse>addRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequestUri().path("/api/add/role").toUriString());
        Role role1 = userService.saveRole(role);
        return ResponseEntity.created(uri).body(UniversalResponse.builder().status(201).message("user created").data(role1).build());
    }
    @GetMapping("/get/role")
    ResponseEntity <UniversalResponse>getRole(){
        List<AppUser> userList = userService.getUser();
        return ResponseEntity.ok().body(UniversalResponse.builder().status(200).message("user retrieved").data(userList).build());
    }

@PostMapping("/role/add/role/user")
    ResponseEntity<UniversalResponse>addRoleToUser(@RequestBody RoleToUser roleToUser){
        userService.addRoleToUser(roleToUser.getUsername(),roleToUser.getRoleName());
        return ResponseEntity.ok(UniversalResponse.builder().message("user saved to roles").build());

}
    @GetMapping("/token/refresh")
    public void  refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT  = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                AppUser user  = userService.getUser(username);
                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+10*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",user.getRole().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token",accessToken);
                tokens.put("refresh_token",refreshToken);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            }catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                /** @  response.sendError(FORBIDDEN.value());
                 *
                 */
                Map<String, String> error = new HashMap<>();
                error.put("error_message :", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }else {
            throw new RuntimeException("refresh_token is missing");
        }
    }



}

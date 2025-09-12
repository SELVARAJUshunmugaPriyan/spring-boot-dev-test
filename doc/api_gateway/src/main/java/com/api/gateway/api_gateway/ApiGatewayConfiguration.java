package com.api.gateway.api_gateway;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

// import org.springframework.cloud.client.loadbalancer.LoadBalanced;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

// import org.springframework.web.client.RestTemplate;

@Configuration
@SuppressWarnings("unused")
public class ApiGatewayConfiguration {
    
    // @Bean
    // @LoadBalanced
    // RestTemplate restTemplate() {
    //     return new RestTemplate();
    // }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
        throws Exception {
            http
                .authorizeHttpRequests(auth -> auth
                    .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(Customizer.withDefaults())
                )
                .formLogin(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
            
            return http.build();
        }

        @Bean
        UserDetailsService userDetailsService() {
            UserDetails user = User
                .withUsername("admin")
                .password(passwordEncoder().encode("Sweden19%"))
                .roles("USER")
                .build();
            
                return new InMemoryUserDetailsManager(user);
        }

        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public JwtDecoder jwtDecoder() throws Exception {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();

        JWSSigner signer = new RSASSASigner(privateKey);
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("Shunmuga Priyan Selvaraju")
                .issuer("dev-issuer")
                .audience("test-audience")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .claim("role", "developer")
                .build();

        SignedJWT jwt = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).build(), claims);
        jwt.sign(signer);

        System.out.println("JWT:");
        System.out.println(jwt.serialize());

        return NimbusJwtDecoder.withPublicKey(publicKey).build();
        }
}

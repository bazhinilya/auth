package ru.test.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import ru.test.auth.models.RolesResponse;
import ru.test.auth.models.StatusResponse;
import ru.test.auth.models.User;
import ru.test.auth.models.enums.Status;
import ru.test.auth.utils.AuthUtils;

@Service
public class AuthSerice {

    private final String baseUrl = "http://193.19.100.32:7000/api";
    private final String uriGetRoles = "/get-roles";
    private final String uriSignUp = "/sign-up";
    private final String uriGetCode = "/get-code";
    private final String uriSetStatus = "/set-status";

    private WebClient.Builder webClient = WebClient.builder().baseUrl(baseUrl);

    @Autowired
    private AuthUtils authUtils;

    public String authenticate(User user) throws Exception {
        try {
            if (!authUtils.isRoleExist(getRoles(), user.getRole()))
                throw new Exception("the role does not exist");

            String messageResponse = setUserData(user);
            if (messageResponse != "Данные внесены")
                throw new Exception("incorrect user filling in");

            String userCode = getCode(user.getEmail());
            return setStatus(user.getEmail(), userCode);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private RolesResponse getRoles() {
        return webClient.build().get()
                .uri(uriGetRoles)
                .retrieve()
                .bodyToMono(RolesResponse.class)
                .onErrorResume(e -> Mono.empty())
                .block();
    }

    private String setUserData(User user) {
        return webClient.build().post()
                .uri(uriSignUp)
                .contentType(MediaType.APPLICATION_JSON)
                .body(user, User.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private String getCode(String email) {
        return webClient.build().get()
                .uri(uriBuilder -> uriBuilder.path(uriGetCode)
                        .queryParam("email", email).build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private String setStatus(String email, String code) {
        return webClient.build().post()
                .uri(uriSetStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(StatusResponse.builder()
                        .status(Status.INCREASED.toString())
                        .token(authUtils.encode(email, code))
                        .build(),
                        StatusResponse.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
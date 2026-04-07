package com.vinsguru.aggregator.service;

import com.vinsguru.user.UserInformation;
import com.vinsguru.user.UserInformationRequest;
import com.vinsguru.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userClient;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserInformation getUserInformation(int userId) {
        var request = UserInformationRequest.newBuilder()
                .setUserId(userId)
                .build();

        long startTime = System.currentTimeMillis();
        UserInformation response = this.userClient.getUserInformation(request);
        long elapsed = System.currentTimeMillis() - startTime;

        log.info("gRPC запит getUserInformation для userId={} виконано за {} ms", userId, elapsed);

        // Перевірка часу відповіді
        if (elapsed > 150) {
            log.warn("УВАГА! Час відповіді gRPC запиту перевищує 150 мс: {} ms", elapsed);
        }

        return response;
    }
}

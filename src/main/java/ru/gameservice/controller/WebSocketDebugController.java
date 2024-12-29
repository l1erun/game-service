package ru.gameservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketDebugController {

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @GetMapping("/debug/sessions")
    public void logActiveSessions() {
        for (SimpUser user : simpUserRegistry.getUsers()) {
            System.out.println("Пользователь: " + user.getName());
            for (SimpSession session : user.getSessions()) {
                System.out.println("Сессия: " + session.getId());
            }
        }
    }
}


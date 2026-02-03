package com.ifpb.sorrisus.controller;

import com.ifpb.sorrisus.dto.WhatsAppRequestDTO;
import com.ifpb.sorrisus.service.WhatsAppService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppController {

    private final WhatsAppService whatsAppService;

    public WhatsAppController(WhatsAppService whatsAppService) {
        this.whatsAppService = whatsAppService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody WhatsAppRequestDTO dto) {

        if (dto.getPhone() == null || dto.getMessage() == null) {
            return ResponseEntity.badRequest().body("Número e mensagem são obrigatórios");
        }

        whatsAppService.sendMessage(dto.getPhone(), dto.getMessage());

        return ResponseEntity.ok("Mensagem enviada com sucesso");
    }
}
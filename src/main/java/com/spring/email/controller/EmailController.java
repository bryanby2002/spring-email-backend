package com.spring.email.controller;

import com.spring.email.dto.EmailDTO;
import com.spring.email.dto.EmailFileDTO;
import com.spring.email.service.IEmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class EmailController {

    private final IEmailService iEmailService;
    public EmailController(IEmailService iEmailService){
        this.iEmailService = iEmailService;
    }

    @PostMapping("/send-message")
    public ResponseEntity<?> receiveRequestEmail(@RequestBody EmailDTO emailDTO){

        System.out.println("mensaje recibido "+emailDTO);
        iEmailService.sendEmail(emailDTO.getToUser(), emailDTO.getSubject(), emailDTO.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("status", "send");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-message-file")
    public ResponseEntity<?> receiveRequestEmailWithFile(@ModelAttribute EmailFileDTO emailFileDTO){

        try {

            String fileName = emailFileDTO.getFile().getOriginalFilename();
            Path path = Paths.get("/src/main/resources/files/"+fileName);
            Files.createDirectories(path.getParent());
            Files.copy(emailFileDTO.getFile().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            File file = path.toFile();

            iEmailService.sendEmailWithFile(emailFileDTO.getToUser(), emailFileDTO.getSubject(),
                    emailFileDTO.getMessage(), file);

            Map<String, String> response = new HashMap<>();
            response.put("status", "send");
            response.put("file", fileName);

            return ResponseEntity.ok(response);

        }catch (Exception e){
            throw new RuntimeException("Error al enviar el email con archivo "+e.getMessage());
        }

    }
}
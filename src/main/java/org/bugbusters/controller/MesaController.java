package org.bugbusters.controller;

import org.bugbusters.entity.Mesa;
import org.bugbusters.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
@CrossOrigin(origins = "*")
public class MesaController {
    @Autowired
    private MesaRepository mesaRepository;

    @GetMapping
    public List<Mesa> getMesas() {
        return mesaRepository.findAll();
    }
}
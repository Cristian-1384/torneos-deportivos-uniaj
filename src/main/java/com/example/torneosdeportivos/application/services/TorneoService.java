package com.example.torneosdeportivos.application.services;

import com.example.torneosdeportivos.application.ports.in.TorneoUseCase;
import com.example.torneosdeportivos.application.ports.out.TorneoRepository;
import com.example.torneosdeportivos.domain.exceptions.ReglaDeNegocioException;
import com.example.torneosdeportivos.domain.exceptions.RecursoNoEncontradoException;
import com.example.torneosdeportivos.domain.models.Torneo;
import com.example.torneosdeportivos.domain.models.Wrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class TorneoService implements TorneoUseCase {

    private final TorneoRepository torneoRepository;
    private static final Duration TTL = Duration.ofMinutes(15);

    private final RedisTemplate<String, String> redisTemplate;
    private final Gson gson;

    private static final List<String> DISCIPLINAS_PERMITIDAS = Arrays.asList("Fútbol", "Voleibol", "Baloncesto");

    public TorneoService(TorneoRepository torneoRepository,
                         RedisTemplate<String, String> redisTemplate,
                         Gson gson) {
        this.torneoRepository = torneoRepository;
        this.redisTemplate = redisTemplate;
        this.gson = gson;
    }

    @Override
    public Wrapper<Torneo> listar(int page, int size, String sortBy, String disciplina, String estado, boolean refresh) {

        String cacheKey = String.format("torneos:p%d:s%d:ob%s:d%s:e%s", page, size, sortBy, disciplina, estado);

        String valor = redisTemplate.opsForValue().get(cacheKey);

        if (valor == null || refresh) {
            Wrapper<Torneo> torneos = torneoRepository.getAll(page, size, sortBy, disciplina, estado);
            String json = gson.toJson(torneos);
            redisTemplate.opsForValue().set(cacheKey, json, TTL);
            return torneos;
        }

        Type pageTorneos = new TypeToken<Wrapper<Torneo>>() {}.getType();
        return gson.fromJson(valor, pageTorneos);
    }

    @Override
    public Torneo crear(Torneo torneo) {

        if (!DISCIPLINAS_PERMITIDAS.contains(torneo.getDisciplina())) {
            throw new ReglaDeNegocioException("La disciplina '" + torneo.getDisciplina() + "' no está permitida en este sistema de torneos.");
        }

        torneo.setEstado("PLANIFICADO");
        torneo.setFechaCreacion(LocalDateTime.now());

        return torneoRepository.save(torneo);
    }

    @Override
    public Torneo buscarPorId(Long idTorneo) {
        return torneoRepository.findById(idTorneo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Torneo con ID " + idTorneo + " no fue encontrado."));
    }

    @Override
    public Torneo actualizar(Long idTorneo, Torneo torneo) {

        buscarPorId(idTorneo);
        return torneoRepository.update(idTorneo, torneo);
    }

    @Override
    public Torneo iniciarTorneo(Long idTorneo) {
        Torneo torneo = buscarPorId(idTorneo);
        torneo.setEstado("EN_CURSO");
        return torneoRepository.update(idTorneo, torneo);
    }

    @Override
    public Torneo cerrarTorneo(Long idTorneo) {
        Torneo torneo = buscarPorId(idTorneo);
        torneo.setEstado("FINALIZADO");
        return torneoRepository.update(idTorneo, torneo);
    }
}
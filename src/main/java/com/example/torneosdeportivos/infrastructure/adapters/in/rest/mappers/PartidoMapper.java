package com.example.torneosdeportivos.infrastructure.adapters.in.rest.mappers;

import com.example.torneosdeportivos.domain.models.Partido;
import com.example.torneosdeportivos.domain.models.Usuario;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.PartidoRequest;
import com.example.torneosdeportivos.infrastructure.adapters.in.rest.dto.PartidoResponse;

public interface PartidoMapper {

    public static Partido toDomain(PartidoRequest request) {
        if (request == null) return null;

        Partido partido = new Partido();
        partido.setIdTorneo(request.getIdTorneo());
        partido.setIdEquipoLocal(request.getIdEquipoLocal());
        partido.setIdEquipoVisitante(request.getIdEquipoVisitante());
        partido.setFechaPartido(request.getFechaPartido());

        if (request.getIdArbitro() != null) {
            Usuario arbitro = new Usuario();
            arbitro.setIdUsuario(request.getIdArbitro());
            partido.setArbitro(arbitro);
        }
        return partido;
    }

    public static PartidoResponse toResponse(Partido domain) {
        if (domain == null) return null;

        PartidoResponse response = new PartidoResponse();
        response.setIdPartido(domain.getIdPartido());
        response.setIdTorneo(domain.getIdTorneo());
        response.setIdEquipoLocal(domain.getIdEquipoLocal());
        response.setIdEquipoVisitante(domain.getIdEquipoVisitante());
        response.setFechaPartido(domain.getFechaPartido());
        response.setMarcadorLocal(domain.getMarcadorLocal());
        response.setMarcadorVisitante(domain.getMarcadorVisitante());
        response.setEstado(domain.getEstado());

        if (domain.getArbitro() != null) {
            response.setIdArbitro(domain.getArbitro().getIdUsuario());
        }
        return response;
    }
}
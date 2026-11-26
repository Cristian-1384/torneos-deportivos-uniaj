package com.example.torneosdeportivos.application.ports.in;

public interface AutenticacionUseCase {

    String login(String username, String password);

    void registrar(String username, String password, String email, Integer idRol);
}
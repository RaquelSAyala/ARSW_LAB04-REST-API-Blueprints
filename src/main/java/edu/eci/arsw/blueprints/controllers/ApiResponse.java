package edu.eci.arsw.blueprints.controllers;

/**
 * Respuesta uniforme para todos los endpoints de la API.
 *
 * @param <T>     tipo del dato retornado
 * @param code    código HTTP de la respuesta
 * @param message mensaje descriptivo
 * @param data    datos del resultado (null en caso de error sin cuerpo)
 */
public record ApiResponse<T>(int code, String message, T data) {
}

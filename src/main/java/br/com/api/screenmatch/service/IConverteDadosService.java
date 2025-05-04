package br.com.api.screenmatch.service;

public interface IConverteDadosService {
    <T> T obterDados(String json, Class<T> classe);
}

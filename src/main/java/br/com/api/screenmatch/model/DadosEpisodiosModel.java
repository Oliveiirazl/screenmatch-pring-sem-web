package br.com.api.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodiosModel(@JsonAlias("Title") String titulo,  @JsonAlias("Episode") Integer numero,
                                  @JsonAlias("imdbRating") String avaliacao, @JsonAlias("Released")  String dataLancamento) {
}

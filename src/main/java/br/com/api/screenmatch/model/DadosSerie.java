package br.com.api.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DadosSerie (@JsonAlias("Title") String titulo,@JsonAlias("totalSeasons") Integer totalTemporadas,@JsonAlias("imdbRating ") String avalicao) {
}

package br.com.api.screenmatch.app;

import br.com.api.screenmatch.model.DadosEpisodiosModel;
import br.com.api.screenmatch.model.DadosSerieModel;
import br.com.api.screenmatch.model.DadosTemporadaModel;
import br.com.api.screenmatch.service.ConsumoApiService;
import br.com.api.screenmatch.service.ConverteDadosService;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrincipalScreenmatchApplication {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoApiService consumo = new ConsumoApiService();
    private ConverteDadosService conversor = new ConverteDadosService();

    private final String ENDERECO = "https://omdbapi.com/?t=";
    private final String API_KEY = "&apikey=2274833f" ;

    public void exibeMenu () {
        System.out.println("Digite o nome da serie para busca");
        var nomeSerie = scanner.nextLine();
        var consumoApi = new ConsumoApiService();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSerieModel dados = conversor.obterDados(json,DadosSerieModel.class);
        System.out.println(dados);

        List<DadosTemporadaModel> temporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalTemporadas() ; i++) {
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") +"&season=" + i + API_KEY);
            DadosTemporadaModel dadosTemporada = conversor.obterDados(json, DadosTemporadaModel.class);
            temporadas.add(dadosTemporada);
        } temporadas.forEach(System.out::println);

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

    }
}



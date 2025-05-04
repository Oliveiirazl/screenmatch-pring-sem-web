package br.com.api.screenmatch;

import br.com.api.screenmatch.model.DadosEpisodiosModel;
import br.com.api.screenmatch.model.DadosSerieModel;
import br.com.api.screenmatch.model.DadosTemporadaModel;
import br.com.api.screenmatch.service.ConsumoApiService;
import br.com.api.screenmatch.service.ConverteDadosService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoApiService();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=2274833f");
		System.out.println(json);
		ConverteDadosService conversor = new ConverteDadosService();
		DadosSerieModel dados = conversor.obterDados(json,DadosSerieModel.class);
		System.out.println(dados);

		json = consumoApi.obterDados("https://omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=2274833f");
		DadosEpisodiosModel dadosEpisodio = conversor.obterDados(json, DadosEpisodiosModel.class);
		System.out.println(dadosEpisodio);

		List<DadosTemporadaModel> temporadas = new ArrayList<>();

		for (int i = 1; i <= dados.totalTemporadas() ; i++) {
			json = consumoApi.obterDados("https://omdbapi.com/?t=gilmore+girls&season=" + i + "&&apikey=2274833f");
			DadosTemporadaModel dadosTemporada = conversor.obterDados(json, DadosTemporadaModel.class);
			temporadas.add(dadosTemporada);
		} temporadas.forEach(System.out::println);



	}
}



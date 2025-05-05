package br.com.api.screenmatch.app;

import br.com.api.screenmatch.model.DadosEpisodiosModel;
import br.com.api.screenmatch.model.DadosSerieModel;
import br.com.api.screenmatch.model.DadosTemporadaModel;
import br.com.api.screenmatch.model.EpisodioModel;
import br.com.api.screenmatch.service.ConsumoApiService;
import br.com.api.screenmatch.service.ConverteDadosService;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

        List<DadosEpisodiosModel> dadosEpisodios = temporadas.stream().flatMap(t -> t.episodios().stream()).collect(Collectors.toList());

//        System.out.println("\n Top 10 episodios");
//        dadosEpisodios.stream().filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primeiro filtro (N/A) "))
//                .sorted(Comparator.comparing(DadosEpisodiosModel::avaliacao).reversed())
//                .peek(e -> System.out.println("Ordenaçao " + e))
//                .limit(10)
//                .peek(e -> System.out.println("Limite " + e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("Mapeando " + e))
//                .forEach(System.out::println);

        List<EpisodioModel> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new EpisodioModel(t.numero(),d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

//        System.out.println("Digite o espisodio que deseja encontrar");
//        var trechoTitulo = scanner.nextLine();
//        Optional<EpisodioModel> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();
//        if (episodioBuscado.isPresent()) {
//            System.out.println("Episodio encontrado");
//            System.out.println("Temporada: " + episodioBuscado.get());
//        } else {
//            System.out.println("Episodio não encontrado");
//        }




//
//        System.out.println("A partir de qual ano deseja ver os episodios?");
//        var ano = scanner.nextInt();
//        scanner.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream().filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println("Temporada: " + e.getTemporada() + "Episodio: " + e.getTitulo() +
//                        "Data lançamento: " + e.getDataLancamento().format(formatador)));


        Map<Integer, Double> avaliacaoPorEpisodio = episodios.stream()

                .collect(Collectors.groupingBy(EpisodioModel::getTemporada,
                        Collectors.averagingDouble(EpisodioModel::getAvaliacao)));
        System.out.println(avaliacaoPorEpisodio);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(EpisodioModel::getAvaliacao));
        System.out.println("Media: " + est.getAverage());
        System.out.println("Melhor Episodio: " + est.getMax());
        System.out.println("Pior Episodio: " + est.getMin());
        System.out.println("Quantidade de Epidios Avaliados: " + est.getCount());
    }
}



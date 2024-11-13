package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.models.DadosEpisodio;
import br.com.alura.screenmatch.models.DadosSerie;
import br.com.alura.screenmatch.models.DadosTemporada;
import br.com.alura.screenmatch.models.Episodio;
import br.com.alura.screenmatch.services.ConsumoApi;
import br.com.alura.screenmatch.services.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner sc = new Scanner(System.in);

    private ConsumoApi consumo = new ConsumoApi();

    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=ef601764";

    public void exibeMenu() {
        System.out.println("Digite o nome da série para buscar: ");
        var nomeSerie = sc.nextLine().toLowerCase().replaceAll(" ", "+");
        var json = consumo.obterDados(ENDERECO + nomeSerie + API_KEY);

        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i < dadosSerie.totalTemporadas(); i++) {
			json = consumo.obterDados(ENDERECO + nomeSerie + "&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);


//        -----------------------------------------------------
//        LISTA O TÍTULO DE TODOS EPISÓDIOS DE TODAS TEMPORADAS

//        for (int i = 0; i < dadosSerie.totalTemporadas(); i++) {
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }


//        -----------------------------------------------------
//        LISTA O TÍTULO DE TODOS EPISÓDIOS DE TODAS TEMPORADAS

//        temporadas
//                .forEach(temporada -> temporada.episodios()
//                        .forEach(episodio -> System.out.println(episodio.titulo())
//                        )
//                );

//        temporadas.forEach(System.out::println);

//        -----------------------------------------------------
//        LISTA O TÍTULO DE TODOS EPISÓDIOS DE TODAS TEMPORADAS

//        for (DadosTemporada temporada : temporadas) {
//            System.out.println();
//            System.out.println("TEMPORADA " + temporada.numero());
//            for (DadosEpisodio episodio : temporada.episodios()) {
//                System.out.println(episodio.numero() + " - " + episodio.titulo());
//            }
//        }

//        ---------------------------------------------
//        FILTRA 5 EPISÓDIOS COM AS MELHORES AVALIAÇÕES

//        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream())
//                .collect(Collectors.toList());
//
//        System.out.println("Top 5 episódios: ");
//        dadosEpisodios.stream()
//                .filter(e-> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .limit(5)
//                .forEach(System.out::println);


//        -------------------------------------------
//        FILTRA EPISÓDIOS A PARTIR DO ANO DE ENTRADA

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("A partir de qual ano você deseja selecionar os episódios: ");
        var ano = sc.nextInt();
        sc.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyy");

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento()
                        .isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                ", episódio: " + e.getTitulo() +
                                ", data de lançamento: " + e.getDataLancamento().format(formatador)
                ));
    }
}

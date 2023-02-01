import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) throws Exception {
        
        // fazer uma conexão HTPP e buscar os top 250 filmes
        String url = "https://imdb-api.com/en/API/Top250Movies/k_nnqvdk91";        
        URI endereco = URI.create(url);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(endereco).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String body = response.body();
        
        // extrair só os dados q interessa (tiulo, poster, classificação)
        var parser = new JsonParser();
        List<Map<String, String>> listaDeFilmes = parser.parse(body);        

        // exibir e manibular os dados 
        var geradora = new GeradoraDeFigurinhas();

        for (int i = 0; i < 10; i++){

            Map<String, String> filme = listaDeFilmes.get(i);

            String urlImagem = 
                        filme.get("image")                        
                        .replaceAll("(@+)(.*).jpg$", "$1.jpg");
            String titulo = filme.get("title");
            
            InputStream inputStream = new URL(urlImagem).openStream();
            String nomeArquivo = "saida/" + titulo + ".png";
           
           geradora.cria(inputStream, nomeArquivo);

            System.out.println(titulo);
            System.out.println();
        }
    }
}

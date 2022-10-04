package com.takuba.jsoup.scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class IndautorPageScrapper {
	private List<ItemDTO> listItems =null;
	private final String url="https://isbnmexico.indautor.cerlalc.org/catalogo.php?mode=resultados_avanzada&isbn=&titulo=%s&autor=&id_materia=&materia=&idioma=&fecha_aparicion=&id_subtema=";
	public IndautorPageScrapper() {
		listItems  = new ArrayList<>();
	}
	
	public void findTitle(String title) {
		String tmp =String.format(url, title.replace(' ', '+'));
		findTitleInfoInUrl(tmp);
		
	}
	
	private void findTitleInfoInUrl(String url) {
		String currentUrl = url;
		System.out.println("Comienza busqueda de titulos");
		do {
			// Compruebo si me da un 200 al hacer la petición
	        if (getStatusConnectionCode(currentUrl) == 200) {
				
	            // Obtengo el HTML de la web en un objeto Document
	            Document document = getHtmlDocument(currentUrl);
				
	            // Busco todas las entradas que estan dentro de: 
	            Elements entradas = document.select("div.row.lista_libros div.col-md-6");
	            // Paseo cada una de las entradas
	            for (Element elem : entradas) {
	                String urlTmp=elem.select("div.col-md-7.no-padding a.titulo").attr("abs:href"); 
	                String editorial=elem.select("div.col-md-7.no-padding a.texto").get(1).text();
	                listItems.add(getItemInfo(urlTmp,editorial.replace("Editorial", "").trim()));
	                // Con el método "text()" obtengo el contenido que hay dentro de las etiquetas HTML
	                // Con el método "toString()" obtengo todo el HTML con etiquetas incluidas
	            }
	            Element next = document.select("div.row div.col-md-12 ul.pagination.top.pull-right a[aria-label=Next]").first();
		    	if(next != null) {
		    		//System.out.println(next);
		    		currentUrl = next.attr("abs:href");
		    	}else{
		    		currentUrl = null;
		    	}	    	
	 	    
	        }else {
	            System.out.println("El Status Code no es OK es: "+getStatusConnectionCode(url));
	            currentUrl = null;
	        }	
		}while(currentUrl != null);
		System.out.println("Finaliza busqueda de titulos");
	}
	
	private ItemDTO getItemInfo(String url,String editorial) {
		Document htmlItemDocument = getHtmlDocument(url);
		Elements entradas = htmlItemDocument.select("div.row.lista_libros div.col-md-6 div.col-md-7.no-padding");
		String isbn = entradas.select("span.isbn").text().replace("ISBN", "");
		String titulo = entradas.select("span.TituloNolink").text();
		String autor = entradas.select("span.texto a.texto").get(0).text();
		String numero = extractInt(titulo);
		return new ItemDTO(titulo.replaceAll("[0-9]", ""), autor, editorial, isbn, numero);
	}
	
	public void displayItems() {
		for(ItemDTO item:listItems) {
			System.out.println(item);
		}
	}
	
	private int getStatusConnectionCode(String url) {
		
        Response response = null;
    	
        try {
    	response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(10000).ignoreHttpErrors(true).execute();
        } catch (IOException ex) {
    	System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
        }
        return response.statusCode();
    }
	
    private Document getHtmlDocument(String url) {

        Document doc = null;
    	try {
    	    doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
    	    } catch (IOException ex) {
    		System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
    	    }
        return doc;
    }
    
    private String extractInt(String str)
    {
        str = str.replaceAll("[^0-9]", ""); // regular expression
        return str;
    }

}

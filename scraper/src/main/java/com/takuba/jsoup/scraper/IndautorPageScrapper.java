package com.takuba.jsoup.scraper;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class IndautorPageScrapper {
	private List<ItemDTO> listItems;
	private final String url="https://isbnmexico.indautor.cerlalc.org/catalogo.php?mode=resultados_avanzada&isbn=&titulo=%s&autor=&id_materia=&materia=&idioma=&fecha_aparicion=&id_subtema=";
	private Connection conn = null;
	public IndautorPageScrapper() {
		listItems  = new ArrayList<>();
		try {
			conn =
					DriverManager.getConnection("jdbc:mysql://localhost:3306/db?" +
							"user=user&password=password");

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
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
					String editorial = StringUtils.stripStart(getItem("Editorial:", elem.select("span.labels, span.texto a.texto")).replace("Editorial", ""),null);
					Integer intEditorial = getId(IdEnum.EDITORIALS,editorial);
					if(intEditorial == null){
						if(insertId(IdEnum.EDITORIALS,editorial)){
							intEditorial = getId(IdEnum.EDITORIALS,editorial);
						}
					}
					ItemDTO itemInfo = getItemInfo(urlTmp,intEditorial);
					if(itemInfo != null){
						listItems.add(itemInfo);
					}
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
	
	private ItemDTO getItemInfo(String url, Integer idEditorial) {
		Document htmlItemDocument = getHtmlDocument(url);
		Elements entradas = htmlItemDocument.select("div.row.lista_libros div.col-md-6 div.col-md-7.no-padding");
		String soporte = getItem("Soporte:", entradas.select("span.labels,span.textofecha"));
		if(StringUtils.equalsIgnoreCase(soporte,"Digital")){
			return null;
		}
		Integer idAuthor = this.getId(IdEnum.AUTHORS,getItem("Autor:",entradas.select("span.labels, span.texto a.texto")));
		Integer idBinding= this.getId(IdEnum.BINDINGS,getItem("Encuadernación:",entradas.select("span.labels,span.textofecha")));
		String numberOfPAges = getItem("Número de páginas:", entradas.select("span.labels,span.textofecha"));
		if(idAuthor == null || idBinding == null || idEditorial == null){
			return null;
		}
		return new ItemDTO(
				entradas.select("span.TituloNolink").text(),//title
				idAuthor,//author
				idEditorial ,//editorial
				entradas.select("span.isbn").text().replace("ISBN", "").trim(),//isbn
				extractInt(entradas.select("span.TituloNolink").text()),//number
				StringUtils.isNotBlank(numberOfPAges) ? Integer.valueOf(numberOfPAges) : null,//numberOfPages
				entradas.select("span:contains(cm.)").text(),//size
				extractDouble(entradas.select("span:contains($)").text()),//price
				idBinding
		);
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
    
    private Integer extractInt(String str)
    {
        str = str.replaceAll("[^0-9]", ""); // regular expression
		return StringUtils.isNotBlank(str) ? Integer.parseInt(str) : 0;
    }

	private Double extractDouble(String str){
		str = str.replaceAll("[^0-9]", "");
		return StringUtils.isNotBlank(str) ? Double.parseDouble(str) : 0;
	}

	private String getItem(String item,Elements elements){
		int pos = -1;
		String result = null;
		for(int i = 0; i< elements.size();i++){
			if(StringUtils.equals(elements.get(i).text(),item)){
				pos = i;
				break;
			}
		}
		if(pos>=0){
			result = elements.get(pos+1).text();
		}
		return result;
	}

	private Integer getId(IdEnum idType, String value){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Integer result = null;
		try {
			pstmt = conn.prepareStatement("SELECT id FROM "+idType.tableName +" WHERE "+idType.columnName+" = ?");
			pstmt.setString(1,value);
			if(pstmt.execute()){
				rs = pstmt.getResultSet();
				int res = rs.getInt("id");
				result =res > 0 ? new Integer(res) : null;
			}
			pstmt.close();
			rs.close();
		}catch (SQLException ex){
			System.out.println(ex.getMessage());
		}
		return result;
	}
	private boolean insertId(IdEnum idType, String value){
		PreparedStatement pstmt = null;
		boolean result =false;
		try{
			pstmt = conn.prepareStatement("INSERT INTO "+idType.tableName+" ("+ idType.columnName+") VALUES (?)");
			pstmt.setString(1,value);
			result  = pstmt.executeUpdate() > 0;
			pstmt.close();
		}catch (SQLException ex){
			System.out.println(ex.getMessage());
		}
		return result;
	}

	private boolean insertComic(ItemDTO comic){
		PreparedStatement pstmt = null;
		boolean result = false;
		String query ="INSERT INTO comics\n" +
				"(isbn, title, id_author, id_editorial, `number`, numberOfPages, `size`, price, id_binding)\n" +
				"VALUES(?, ?, ?, ?, ?, ?, ?,?, ?)";
		try{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,comic.getIsbn());
			pstmt.setString(2,comic.getTitle());
			pstmt.setInt(3,comic.getIdAuthor());
			pstmt.setInt(4,comic.getIdEditorial());
			pstmt.setInt(5,comic.getNumber());
			pstmt.setInt(6,comic.getNumberOfPages());
			pstmt.setString(7,comic.getSize());
			pstmt.setDouble(8,comic.getPrice());
			pstmt.setInt(9,comic.getIdBinding());
		}catch (SQLException ex){
			System.out.println(ex.getMessage());
		}
		return false;
	}
}

package com.takuba.jsoup.scraper;

import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        String htmlString="<span class=\"labels\">Autor:</span>\n" +
                "<span class=\"editorial\">Editorial:</span>\n" +
                "<span class=\"meteria\">Materia:</span>\n" +
                "<span class=\"labels\">Clasificación:</span>\n" +
                "<span class=\"labels\">Público objetivo:</span>\n" +
                "<span class=\"textofecha\"><a class=\"texto\" href=\"/catalogo.php?mode=busqueda_menu&amp;id_audiencia=3\">Jóvenes adultos</a></span>\n" +
                "<span class=\"labels\">Publicado:</span>\n" +
                "<span class=\"textofecha\">2023-02-20</span>\n" +
                "<span class=\"labels\">Número de edición:</span>\n" +
                "<span class=\"textofecha\">1</span>\n" +
                "<span class=\"labels\">Número de páginas:</span>\n" +
                "<span class=\"textofecha\">200</span>\n" +
                "<span class=\"labels\">Tamaño:</span>\n" +
                "<span class=\"textofecha\">11.4x17.7cm.</span>\n" +
                "<span class=\"labels\">Precio:</span>\n" +
                "<span class=\"textofecha\">$149</span>\n" +
                "<span class=\"labels\">Encuadernación:</span>\n" +
                "<span class=\"textofecha\">Tapa blanda o rústica</span>\n" +
                "<span class=\"labels\">Soporte:</span>\n" +
                "<span class=\"textofecha\">Impreso</span>\n" +
                "<span class=\"labels\">Idioma:</span>\n";
        Document document = Jsoup.parse(htmlString);
        Elements entradas =document.select("span.labels,span.textofecha");
        int pos = -1;
        for(int i = 0; i< entradas.size();i++){
            if(StringUtils.equals(entradas.get(i).text(),"Número de páginas:")){
                pos = i;
                break;
            }
        }
        if(pos>=0){
            System.out.println(entradas.get(pos+1).text());
        }
        assertTrue( pos == 8 );
    }
}

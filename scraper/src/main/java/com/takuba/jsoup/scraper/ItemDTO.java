package com.takuba.jsoup.scraper;

import lombok.*;

@Data
@AllArgsConstructor
public class ItemDTO {

	private String title;
	private String author;
	private String editorial;
	private String isbn;
	private Integer number;
	private Integer numberOfPages;
	private String size;
	private Double price;
	private String binding;//encuadernacion


}

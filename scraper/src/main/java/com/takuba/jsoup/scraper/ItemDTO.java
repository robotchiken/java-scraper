package com.takuba.jsoup.scraper;

import lombok.*;

@Data
@AllArgsConstructor
public class ItemDTO {

	private String title;
	private Integer idAuthor;
	private Integer idEditorial;
	private String isbn;
	private Integer number;
	private Integer numberOfPages;
	private String size;
	private Double price;
	private Integer IdBinding;//encuadernacion


}

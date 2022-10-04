package com.takuba.jsoup.scraper;

public class ItemDTO {
	
	private String titulo;
	private String autor;
	private String editorial;
	private String isbn;
	private String numero;
	
	public ItemDTO(String titulo, String autor, String editorial, String isbn,String numero) {
		super();
		this.titulo = titulo;
		this.autor = autor;
		this.editorial = editorial;
		this.isbn = isbn;
		this.numero = numero;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public String getEditorial() {
		return editorial;
	}
	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	@Override
	public String toString() {
		return "ItemDTO [titulo=" + titulo + ", autor=" + autor + ", editorial=" + editorial + ", isbn=" + isbn + (!numero.equals("") ? ", numero="+numero:"")+"]";
	}
}

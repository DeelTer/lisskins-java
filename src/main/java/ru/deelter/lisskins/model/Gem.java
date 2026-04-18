package ru.deelter.lisskins.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gem {
	private List<String> identifiers;
	private List<String> imageUrls;
	private String name;
	private String description;
	private String color;
}
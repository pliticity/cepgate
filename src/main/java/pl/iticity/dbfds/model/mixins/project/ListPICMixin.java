package pl.iticity.dbfds.model.mixins.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"domain"})
public class ListPICMixin {
}

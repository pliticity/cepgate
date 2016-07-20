package pl.iticity.dbfds.model.mixins.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"domain"})
public class ListPICMixin {
}

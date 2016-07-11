package pl.iticity.dbfds.model.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"domain"})
public class FileInfoMixin {
}

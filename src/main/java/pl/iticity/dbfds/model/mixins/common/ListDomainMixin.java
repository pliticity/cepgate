package pl.iticity.dbfds.model.mixins.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"principals","lastMasterDocumentNumber","lastMasterProductNumber","lastMasterQuotationNumber","lastMasterProjectNumber"})
public class ListDomainMixin {
}

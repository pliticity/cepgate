package pl.iticity.dbfds.service.document;

public class PushFileDTO {

    public PushFileDTO(String dicId, String symbol) {
        this.dicId = dicId;
        this.symbol = symbol;
    }

    private String dicId;

    private String symbol;

    public String getDicId() {
        return dicId;
    }

    public void setDicId(String dicId) {
        this.dicId = dicId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}

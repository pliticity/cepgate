package pl.iticity.dbfds.model.document;

public class RevisionSymbol {

    private String prefix;

    private long number;

    public RevisionSymbol() {
        this.prefix = String.valueOf(0);
    }

    public RevisionSymbol(long number) {
        this.prefix = String.valueOf(number);
        this.number = number;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public void setEffective(String effective){
        // do nothing
    }

    public String getEffective(){
        return prefix;
    }

    public RevisionSymbol next(){
        RevisionSymbol revisionSymbol = new RevisionSymbol();
        revisionSymbol.setNumber(getNumber()+1);
        revisionSymbol.setPrefix(String.valueOf(revisionSymbol.getNumber()));
        return revisionSymbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RevisionSymbol that = (RevisionSymbol) o;

        if (number != that.number) return false;
        return prefix != null ? prefix.equals(that.prefix) : that.prefix == null;

    }

    @Override
    public int hashCode() {
        int result = prefix != null ? prefix.hashCode() : 0;
        result = 31 * result + (int) (number ^ (number >>> 32));
        return result;
    }
}

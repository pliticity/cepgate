package pl.iticity.dbfds.model;

import org.apache.commons.lang.StringUtils;

public class RevisionSymbol {

    private String prefix;

    private long number;

    public RevisionSymbol() {
        this.prefix = StringUtils.EMPTY;
    }

    public RevisionSymbol(long number) {
        this.prefix = StringUtils.EMPTY;
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
        return prefix+number;
    }

    public RevisionSymbol next(){
        RevisionSymbol revisionSymbol = new RevisionSymbol();
        revisionSymbol.setPrefix(getPrefix());
        revisionSymbol.setNumber(getNumber()+1);
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

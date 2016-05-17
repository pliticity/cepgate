package pl.iticity.dbfds.model;

import java.util.Date;

/**
 * Created by pmajchrz on 4/5/16.
 */
public class DocumentActivity {

    private String principalId;

    private Date date;

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

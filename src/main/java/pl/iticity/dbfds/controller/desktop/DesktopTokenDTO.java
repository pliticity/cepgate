package pl.iticity.dbfds.controller.desktop;

public class DesktopTokenDTO {

    public DesktopTokenDTO(String token) {
        this.token = token;
    }

    public DesktopTokenDTO() {
    }

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

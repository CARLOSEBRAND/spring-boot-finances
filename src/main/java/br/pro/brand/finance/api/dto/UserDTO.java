package br.pro.brand.finance.api.dto;

public class UserDTO {

    private String email;
    private String name;
    private String password;


    public UserDTO() {
    }

    public UserDTO(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserDTO email(String email) {
        setEmail(email);
        return this;
    }

    public UserDTO name(String name) {
        setName(name);
        return this;
    }

    public UserDTO password(String password) {
        setPassword(password);
        return this;
    }    
    
}

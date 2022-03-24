package nl.miwteam2.cryptomero.domain;

public class UserAccountDTO {
        private String email;
        private String password;

        public UserAccountDTO(String email, String password) {
            this.email = email;
            this.password = password;
        }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

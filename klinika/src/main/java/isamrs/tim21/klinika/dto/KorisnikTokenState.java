package isamrs.tim21.klinika.dto;

//DTO koji enkapsulira generisani JWT i njegovo trajanje koji se vracaju klijentu

public class KorisnikTokenState {
	private String accessToken;
    private Long expiresIn;
    private String role;

    public KorisnikTokenState() {
        this.accessToken = null;
        this.expiresIn = null;
        this.role = null;
    }

    public KorisnikTokenState(String accessToken, long expiresIn, String role) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }
    
    

    public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}

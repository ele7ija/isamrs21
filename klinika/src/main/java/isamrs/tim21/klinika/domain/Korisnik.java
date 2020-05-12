package isamrs.tim21.klinika.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.springframework.security.core.userdetails.UserDetails;

import isamrs.tim21.klinika.jsonSerialize.IdentitySerializable;

@Entity
@Table(name="korisnik")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.STRING)
public abstract class Korisnik implements IdentitySerializable, UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	
	@Column(name="email", nullable=false, unique=true)
	protected String email;
	
	@Column(name="sifra", nullable=false)
	protected String sifra;
	
	@Column(name="ime", nullable=false)
	protected String ime;
	
	@Column(name="prezime", nullable=false)
	protected String prezime;
	
	@Column(name = "enabled")
	protected boolean enabled;

	@Column(name = "poslednja_promena_sifre")
	protected java.sql.Timestamp poslednjaPromenaSifre;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private List<Authority> authorities;
	
	@Version
	@Column(name="version", columnDefinition="integer DEFAULT 0", nullable=false)
	private Long version;
	
	public Korisnik(){
		authorities = new ArrayList<Authority>();
	}

	public Korisnik(String email, String sifra, String ime, String prezime) {
		super();
		this.email = email;
		this.sifra = sifra;
		this.ime = ime;
		this.prezime = prezime;
		this.enabled = true;
		authorities = new ArrayList<Authority>();
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	

	public java.sql.Timestamp getPoslednjaPromenaSifre() {
		return poslednjaPromenaSifre;
	}

	public void setPoslednjaPromenaSifre(java.sql.Timestamp poslednjaPromenaSifre) {
		this.poslednjaPromenaSifre = poslednjaPromenaSifre;
	}


	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}
	
	@Override
	public List<Authority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.sifra;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}


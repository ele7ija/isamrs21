package isamrs.tim21.klinika.jsonSerialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import isamrs.tim21.klinika.domain.Korisnik;

public class KorisnikSerializer extends JsonSerializer<Korisnik>{

	@Override
	public void serialize(Korisnik arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
		arg1.writeStartObject();
		arg1.writeNumberField("id", arg0.getId());
		arg1.writeStringField("ime", arg0.getIme());
		arg1.writeStringField("prezime", arg0.getPrezime());
		arg1.writeEndObject();
	}

}

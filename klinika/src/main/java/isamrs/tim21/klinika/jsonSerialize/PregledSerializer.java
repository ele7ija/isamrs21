package isamrs.tim21.klinika.jsonSerialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import isamrs.tim21.klinika.domain.Pregled;

public class PregledSerializer extends JsonSerializer<Pregled>{

	@Override
	public void serialize(Pregled arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
		arg1.writeStartObject();
		arg1.writeNumberField("id", arg0.getId());
		arg1.writeNumberField("cena", arg0.getCena());
		arg1.writeNumberField("popust", arg0.getPopust());
		arg1.writeNumberField("konacnaCena", arg0.getKonacnaCena());
		arg1.writeObjectField("pocetakPregleda", arg0.getPocetakPregleda());
		arg1.writeObjectField("krajPregleda", arg0.getKrajPregleda());
		arg1.writeEndObject();
	}

}

package isamrs.tim21.klinika.jsonSerialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import isamrs.tim21.klinika.domain.TipPregleda;

public class TipPregledaSerializer extends JsonSerializer<TipPregleda>{

	@Override
	public void serialize(TipPregleda arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
		arg1.writeStartObject();
		arg1.writeNumberField("id", arg0.getId());
		arg1.writeStringField("naziv", arg0.getNaziv());
		arg1.writeStringField("opis", arg0.getOpis());
		arg1.writeEndObject();
		
	}

}

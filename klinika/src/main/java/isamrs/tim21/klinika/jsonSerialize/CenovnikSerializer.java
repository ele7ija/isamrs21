package isamrs.tim21.klinika.jsonSerialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import isamrs.tim21.klinika.domain.Cenovnik;

public class CenovnikSerializer extends JsonSerializer<Cenovnik>{

	@Override
	public void serialize(Cenovnik arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
		arg1.writeStartObject();
		arg1.writeNumberField("id", arg0.getId());
		arg1.writeNumberField("iznosUDinarima", arg0.getIznosUDinarima());
		arg1.writeStringField("naziv", arg0.getNaziv());
		arg1.writeEndObject();
		
	}

}

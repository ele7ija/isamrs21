package isamrs.tim21.klinika.jsonSerialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import isamrs.tim21.klinika.domain.Poseta;

public class PosetaSerializer extends JsonSerializer<Poseta>{

	@Override
	public void serialize(Poseta arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
		arg1.writeStartObject();
		arg1.writeNumberField("id", arg0.getId());
		arg1.writeFieldName("zdravstveniKarton");
		ZdravstveniKartonSerializer zks = new ZdravstveniKartonSerializer();
		zks.serialize(arg0.getZdravstveniKarton(), arg1, arg2);
		arg1.writeEndObject();
		
	}

}

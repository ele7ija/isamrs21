package isamrs.tim21.klinika.jsonSerialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import isamrs.tim21.klinika.domain.RadniKalendar;

public class RadniKalendarSerializer extends JsonSerializer<RadniKalendar>{

	@Override
	public void serialize(RadniKalendar arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
		arg1.writeStartObject();
		arg1.writeNumberField("id", arg0.getId());
		arg1.writeNumberField("dnevnoRadnoVremeSati", arg0.getDnevnoRadnoVremeSati());
		arg1.writeEndObject();
		
	}

}

package isamrs.tim21.klinika.jsonSerialize;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import isamrs.tim21.klinika.domain.Pregled;

public class PregledListSerializer extends JsonSerializer<List<Pregled>>{

	@Override
	public void serialize(List<Pregled> arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
		arg1.writeStartArray();
		PregledSerializer serializer = new PregledSerializer();
		for(Pregled arg: arg0){
			serializer.serialize(arg, arg1, arg2);
		}
		arg1.writeEndArray();
		
	}

}

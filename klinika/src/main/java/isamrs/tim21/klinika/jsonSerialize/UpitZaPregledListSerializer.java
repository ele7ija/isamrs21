package isamrs.tim21.klinika.jsonSerialize;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import isamrs.tim21.klinika.domain.UpitZaPregled;

public class UpitZaPregledListSerializer extends JsonSerializer<List<UpitZaPregled>>{

	@Override
	public void serialize(List<UpitZaPregled> arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
		arg1.writeStartArray();
		UpitzaPregledSerializer serializer = new UpitzaPregledSerializer();
		for(UpitZaPregled arg: arg0){
			serializer.serialize(arg, arg1, arg2);
		}
		arg1.writeEndArray();
		
	}

}

package isamrs.tim21.klinika.jsonSerialize;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import isamrs.tim21.klinika.domain.TipPregleda;

public class TipPregledaListSerializer extends JsonSerializer<List<TipPregleda>>{

	@Override
	public void serialize(List<TipPregleda> arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
		arg1.writeStartArray();
		TipPregledaSerializer serializer = new TipPregledaSerializer();
		for(TipPregleda arg: arg0){
			serializer.serialize(arg, arg1, arg2);
		}
		arg1.writeEndArray();
		
	}

}

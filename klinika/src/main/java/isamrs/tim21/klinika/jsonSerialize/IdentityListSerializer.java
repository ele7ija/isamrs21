package isamrs.tim21.klinika.jsonSerialize;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class IdentityListSerializer extends JsonSerializer<List<IdentitySerializable>>{

	@Override
	public void serialize(List<IdentitySerializable> arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
		arg1.writeStartArray();
		IdentitySerializer serializer = new IdentitySerializer();
		for(IdentitySerializable arg: arg0){
			serializer.serialize(arg, arg1, arg2);
		}
		arg1.writeEndArray();
		
	}

}

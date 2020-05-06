package isamrs.tim21.klinika.jsonSerialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import isamrs.tim21.klinika.domain.ZdravstveniKarton;

public class ZdravstveniKartonSerializer extends JsonSerializer<ZdravstveniKarton>{

	@Override
	public void serialize(ZdravstveniKarton arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
		arg1.writeStartObject();
		arg1.writeNumberField("id", arg0.getId());
		arg1.writeFieldName("pacijent");
		KorisnikSerializer ks = new KorisnikSerializer();
		ks.serialize(arg0.getPacijent(), arg1, arg2);
		arg1.writeEndObject();
		
	}

}

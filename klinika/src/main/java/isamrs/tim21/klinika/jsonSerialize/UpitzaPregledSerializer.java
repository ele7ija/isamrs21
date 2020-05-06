package isamrs.tim21.klinika.jsonSerialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import isamrs.tim21.klinika.domain.UpitZaPregled;

public class UpitzaPregledSerializer extends JsonSerializer<UpitZaPregled>{

	@Override
	public void serialize(UpitZaPregled arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
		arg1.writeStartObject();
		arg1.writeNumberField("id", arg0.getId());
		arg1.writeBooleanField("adminObradio", arg0.getAdminObradio());
		arg1.writeBooleanField("odobren", arg0.getOdobren());
		arg1.writeBooleanField("pacijentObradio", arg0.getPacijentObradio());
		arg1.writeBooleanField("potvrdjen", arg0.getPotvrdjen());
		
		arg1.writeFieldName("pacijent");
		KorisnikSerializer ks = new KorisnikSerializer();
		ks.serialize(arg0.getPacijent(), arg1, arg2);
		arg1.writeEndObject();
		
	}

}

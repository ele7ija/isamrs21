package isamrs.tim21.klinika.configuration;

import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Configuration
public class JacksonConfiguration {
	 public JacksonConfiguration(ObjectMapper objectMapper) {
		 SimpleFilterProvider provider =  new SimpleFilterProvider().setFailOnUnknownId(false);
		 objectMapper.setFilterProvider(provider); 
	 }
}

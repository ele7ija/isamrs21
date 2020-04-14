package isamrs.tim21.klinika.aop;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import isamrs.tim21.klinika.domain.Sala;

@Component
@Aspect
public class SalaKontrolerAspect {
	@Pointcut("execution(* isamrs.tim21.klinika.controller.SalaKontroler.getAllSale(..))")
    public void getAll() {}
	
	@Pointcut("execution(* isamrs.tim21.klinika.controller.SalaKontroler.getSala(..))"
			+ " || execution(* isamrs.tim21.klinika.controller.SalaKontroler.addSala(..))"
			+ " || execution(* isamrs.tim21.klinika.controller.SalaKontroler.updateSala(..))")
	public void getOrPostOrPut() {}
	
	@Pointcut("execution(* isamrs.tim21.klinika.controller.SalaKontroler.deleteSala(..))")
	public void delete() {}
	
	@SuppressWarnings("unchecked")
	@Around("getAll()")
	public Object aroundMethod1(ProceedingJoinPoint joinPoint) throws Throwable {
		//EXECUTE CONTROLLER METHOD
		ResponseEntity<List<Sala>> interceptedResult = (ResponseEntity<List<Sala>>)joinPoint.proceed();
		if(interceptedResult.getStatusCode() != HttpStatus.OK){
			return interceptedResult;
		}
		
		//GET THE BEAN THAT NEEDS TO BE FILTERED
		List<Sala> responseBody = interceptedResult.getBody();
		
		//SPECIFY FILTERS 
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
	    filterProvider.addFilter("sala_to_klinika_filter", SimpleBeanPropertyFilter.filterOutAllExcept("id"));
	     
	    //FILTER BEAN AS STRING
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.setFilterProvider(filterProvider);
	    String filteredJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseBody);
	    return filteredJson;
	    
	}
	
	@SuppressWarnings("unchecked")
	@Around("getOrPostOrPut()")
	public Object aroundMethod2(ProceedingJoinPoint joinPoint) throws Throwable {
		//EXECUTE CONTROLLER METHOD
		ResponseEntity<Sala> interceptedResult = (ResponseEntity<Sala>)joinPoint.proceed();
		if(interceptedResult.getStatusCode() != HttpStatus.OK){
			return interceptedResult;
		}
		
		//GET THE BEAN THAT NEEDS TO BE FILTERED
		Sala responseBody = interceptedResult.getBody();
		
		//SPECIFY FILTERS 
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
	    filterProvider.addFilter("sala_to_klinika_filter", SimpleBeanPropertyFilter.filterOutAllExcept("id"));
	     
	    //RETURN RESPONSE
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.setFilterProvider(filterProvider);
	    String filteredJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseBody);
	    System.out.println(filteredJson);
	    return filteredJson;
	   
	}
	
	@SuppressWarnings("unchecked")
	@Around("delete()")
	public Object aroundMethod3(ProceedingJoinPoint joinPoint) throws Throwable {
		//EXECUTE CONTROLLER METHOD
		ResponseEntity<Boolean> interceptedResult = (ResponseEntity<Boolean>)joinPoint.proceed();
		if(interceptedResult.getStatusCode() != HttpStatus.OK){
			return interceptedResult;
		}
		
		//GET THE BEAN THAT NEEDS TO BE FILTERED
		Boolean responseBody = interceptedResult.getBody();
		
		//SPECIFY FILTERS 
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
	    filterProvider.addFilter("tipPregleda_to_klinika_filter", SimpleBeanPropertyFilter.filterOutAllExcept("id"));
	     
	    //RETURN RESPONSE
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.setFilterProvider(filterProvider);
	    String filteredJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseBody);
	    return filteredJson;
	}
}


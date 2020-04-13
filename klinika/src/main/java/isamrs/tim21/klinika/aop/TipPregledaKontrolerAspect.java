package isamrs.tim21.klinika.aop;

import java.util.ArrayList;
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

import isamrs.tim21.klinika.domain.TipPregleda;

@Component
@Aspect
public class TipPregledaKontrolerAspect {
	@Pointcut("execution(* isamrs.tim21.klinika.controller.TipPregledaKontroler.getAllTipoviPregleda(..))")
    public void getAll() {}
	
	@Pointcut("execution(* isamrs.tim21.klinika.controller.TipPregledaKontroler.getTipPregleda(..))"
			+ " || execution(* isamrs.tim21.klinika.controller.TipPregledaKontroler.addTipPregleda(..))"
			+ " || execution(* isamrs.tim21.klinika.controller.TipPregledaKontroler.updateTipPregleda(..))")
	public void getOrPostOrPut() {}
	
	@Pointcut("execution(* isamrs.tim21.klinika.controller.TipPregledaKontroler.deleteTipPregleda(..))")
	public void delete() {}
	
	@SuppressWarnings("unchecked")
	@Around("getAll()")
	public Object aroundMethod1(ProceedingJoinPoint joinPoint) throws Throwable {
		//EXECUTE CONTROLLER METHOD
		ResponseEntity<List<TipPregleda>> interceptedResult = (ResponseEntity<List<TipPregleda>>)joinPoint.proceed();
		if(interceptedResult.getStatusCode() != HttpStatus.OK){
			return interceptedResult;
		}
		
		//GET THE BEAN THAT NEEDS TO BE FILTERED
		List<TipPregleda> responseBody = interceptedResult.getBody();
		
		//SPECIFY FILTERS 
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
	    filterProvider.addFilter("tipPregleda_to_klinika_filter", SimpleBeanPropertyFilter.filterOutAllExcept("id"));
	     
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
		ResponseEntity<TipPregleda> interceptedResult = (ResponseEntity<TipPregleda>)joinPoint.proceed();
		if(interceptedResult.getStatusCode() != HttpStatus.OK){
			return interceptedResult;
		}
		
		//GET THE BEAN THAT NEEDS TO BE FILTERED
		TipPregleda responseBody = interceptedResult.getBody();
		
		//SPECIFY FILTERS 
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
	    filterProvider.addFilter("tipPregleda_to_klinika_filter", SimpleBeanPropertyFilter.filterOutAllExcept("id"));
	     
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

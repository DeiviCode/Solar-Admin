package com.solar.task;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.solar.model.Estacion;
import com.solar.model.IsantandRadiaction;
import com.solar.model.IsantandService;
import com.solar.model.Radiacion;
import com.solar.repository.RadiacionRepository;
import com.solar.service.EstacionServiceIMPL;


@Component
public class WundergroundService {


	
	@Autowired
	private RadiacionRepository radiacionRepository;
	
	@Autowired
	private EstacionServiceIMPL  estacionServiceIMPL;

	// cada hora
	@Scheduled(fixedRate = 3600000 )
	public void getDataFromISANTAND30() {
		System.out.println("Ejecutando para isantand 30!");
		try {
			String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			date ="20190604";
			String uri = "https://api.weather.com/v2/pws/history/hourly?stationId=ISANTAND30&format=json&units=m&date="+date+"&apiKey=b3bc2e2c48a644b6bc2e2c48a614b691";
			
			RestTemplate restTemplate = new RestTemplate();
			IsantandService isantand = restTemplate.getForObject(uri, IsantandService.class);
			List<IsantandRadiaction> radiaciones = isantand.getObservations();
			List<Radiacion> radiacionesAguardar = new ArrayList<>();
			Estacion Isantand30 = estacionServiceIMPL.findByNombre_estacion("UPB - Piedecuesta");
			
			System.out.println(radiaciones.size() +"datos encontrados");
			
			if(radiaciones.size()>0) {
				for(IsantandRadiaction radiacion : radiaciones) {
					Radiacion currentRadiation = new Radiacion(Isantand30, Double.valueOf(radiacion.getSolarRadiationHigh()), Timestamp.valueOf(radiacion.getObsTimeLocal()));
					radiacionesAguardar.add(currentRadiation);
				}
				radiacionRepository.saveAll(radiacionesAguardar);
				System.out.println("Se guardaron " + radiacionesAguardar.size() +" datos");
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		}
	}
	
	// cada 1.005 horas
	@Scheduled(fixedRate = 3618000 )
	public void getDataFromISANTAND31() {
		System.out.println("Ejecutando servicio  para isantand 31!");
		try {
			String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			String uri = "https://api.weather.com/v2/pws/history/hourly?stationId=ISANTAND31&format=json&units=m&date="+date+"&apiKey=b3bc2e2c48a644b6bc2e2c48a614b691";
			
			RestTemplate restTemplate = new RestTemplate();
			IsantandService isantand = restTemplate.getForObject(uri, IsantandService.class);
			List<IsantandRadiaction> radiaciones = isantand.getObservations();
			List<Radiacion> radiacionesAguardar = new ArrayList<>();
			Estacion Isantand30 = estacionServiceIMPL.findByNombre_estacion("Paralela Bosque");
			
			System.out.println(radiaciones.size() +"datos encontrados");
			
			if(radiaciones.size()>0) {
				for(IsantandRadiaction radiacion : radiaciones) {
					Radiacion currentRadiation = new Radiacion(Isantand30, Double.valueOf(radiacion.getSolarRadiationHigh()), Timestamp.valueOf(radiacion.getObsTimeLocal()));
					radiacionesAguardar.add(currentRadiation);
				}
				radiacionRepository.saveAll(radiacionesAguardar);
				System.out.println("Se guardaron " + radiacionesAguardar.size() +" datos");
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		}
	}
}

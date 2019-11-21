package com.consultorjava.server;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.google.gson.Gson;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class StartServerReto implements WebMvcConfigurer {

	static List<Cliente> listaStoreClientes = new ArrayList<Cliente>();

	public static void main(String[] args) {

		// inicializamos con 2 clientes en memoria
		Cliente cliente = new Cliente();
		cliente.setNombre("Jose");
		cliente.setApellido("Bugarin");
		cliente.setEdad(37);
		cliente.setDiaNac(24);
		cliente.setMesNac(4);
		cliente.setaNac(1982);
		cliente.calculoFechaMuerte();

		Cliente cliente2 = new Cliente();
		cliente2.setNombre("Paolo");
		cliente2.setApellido("Guerrero");
		cliente2.setEdad(35);
		cliente2.setDiaNac(01);
		cliente2.setMesNac(01);
		cliente2.setaNac(1984);
		cliente2.calculoFechaMuerte();

		listaStoreClientes.add(cliente);
		listaStoreClientes.add(cliente2);

		SpringApplication.run(StartServerReto.class, args);
	}

	@RestController
	@EnableSwagger2
	private class ClienteController {
		@GetMapping("/retoms/kpideclientes")
		public KPICliente listaKPIClientes() {

			KPICliente kpi = new KPICliente();
			kpi.promedioEdades(listaStoreClientes);
			kpi.desviacionEstandarCalc(kpi.getPromedioEdades(), listaStoreClientes);

			return kpi;
		}

		@PostMapping(path = "/retoms/creacliente", consumes = "application/json", produces = "application/json")
		public void creaCliente(@RequestBody String cliente) {

			Gson gson = new Gson();
			Cliente cli = gson.fromJson(cliente, Cliente.class);

			cli.calculoFechaMuerte();
			listaStoreClientes.add(cli);
		}

		@GetMapping("/retoms/listaclientes")
		public List<Cliente> listaClientes() {

			return listaStoreClientes;
		}
	}

	class KPICliente {

		public double getPromedioEdades() {
			return promedioEdades;
		}

		public void setPromedioEdades(double promedioEdades) {
			this.promedioEdades = promedioEdades;
		}

		public double getDesviacionEstandar() {
			return desviacionEstandar;
		}

		public void setDesviacionEstandar(double desviacionEstandar) {
			this.desviacionEstandar = desviacionEstandar;
		}

		double promedioEdades;
		double desviacionEstandar;

		public void promedioEdades(List<Cliente> listaClientes) {

			double edades = 0.00;
			double promedio = 0.00;

			for (int i = 0; i < listaClientes.size(); i++) {
				edades = edades + listaClientes.get(i).getEdad();
			}
			promedio = edades / listaClientes.size();
			setPromedioEdades(promedio);
		}

		public void desviacionEstandarCalc(double promedio, List<Cliente> listaClientes) {

			double acumulador = 0.00;
			double total = 0.00;
			for (int i = 0; i < listaClientes.size(); i++) {
				double distancia = listaClientes.get(i).getEdad() - promedio;
				double cuadrado = Math.pow(distancia, 2);
				acumulador = acumulador + cuadrado;
			}
			total = Math.sqrt(acumulador / listaClientes.size());
			setDesviacionEstandar(total);

		}

	}

	static class Cliente {

		String nombre;
		String apellido;
		int edad;
		int aNac;
		int mesNac;
		int diaNac;
		int fechaMuerte;

		public Cliente() {

		}

		public int getaNac() {
			return aNac;
		}

		public void setaNac(int aNac) {
			this.aNac = aNac;
		}

		public int getMesNac() {
			return mesNac;
		}

		public void setMesNac(int mesNac) {
			this.mesNac = mesNac;
		}

		public int getDiaNac() {
			return diaNac;
		}

		public void setDiaNac(int diaNac) {
			this.diaNac = diaNac;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getApellido() {
			return apellido;
		}

		public void setApellido(String apellido) {
			this.apellido = apellido;
		}

		public int getEdad() {
			return edad;
		}

		public void setEdad(int edad) {
			this.edad = edad;
		}

		public int getFechaMuerte() {
			return fechaMuerte;
		}

		public void setFechaMuerte(int fechaMuerte) {
			this.fechaMuerte = fechaMuerte;
		}

		public void calculoFechaMuerte() {
			// tasa mortalidad sbs
			this.fechaMuerte = 88 - getEdad() + 2019;
		}

	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
	}

}

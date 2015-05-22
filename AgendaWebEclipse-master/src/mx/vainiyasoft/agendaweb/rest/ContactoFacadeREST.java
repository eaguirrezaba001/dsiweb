package mx.vainiyasoft.agendaweb.rest;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import mx.vainiyasoft.agendaweb.entity.Contacto;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("contacto")
public class ContactoFacadeREST extends AbstractFacade<Contacto> {
	
	private EntityManager em;
	private final String JSON_RESPONSE = "{\"uuid\":\"%s\", \"androidId\":%d, \"creado\":\"%s\", \"actualizado\":\"%s\", \"operacion\":\"%s\", \"resultado\":\"%s\"}";
	
	public ContactoFacadeREST() {
		super(Contacto.class);
	}
	
	@POST
	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String create(Contacto entity) {
		entity.setUuid(UUID.randomUUID().toString());
		long currentMillis = System.currentTimeMillis();
		entity.setCreado(new Timestamp(currentMillis));
		entity.setActualizado(new Timestamp(currentMillis));
		String resultado = super.create(entity);
		return String.format(JSON_RESPONSE, entity.getUuid(), entity.getAndroidId(),
				entity.getCreado(), entity.getActualizado(), "insert", resultado);
	}
	
	@PUT
	@Path("{uuid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String edit(@PathParam("uuid") String uuid, Contacto entity) {
		String resultado = super.edit(entity);
		return String.format(JSON_RESPONSE, entity.getUuid(), entity.getAndroidId(),
				entity.getCreado(), entity.getActualizado(), "update", resultado);
	}
	
	@DELETE
	@Path("{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public String remove(@PathParam("uuid") String uuid) {
		String resultado = super.remove(super.find(uuid));
		String mensaje = "{\"uuid\":\"%s\", \"operacion\":\"%s\", \"resultado\":\"%s\"}";
		return String.format(mensaje, uuid, "delete", resultado);
	}
	
	@GET
	@Path("{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Contacto find(@PathParam("uuid") String uuid) {
		return super.find(uuid);
	}
	
	@GET
	@Override
	@Produces(MediaType.APPLICATION_JSON)
	public List<Contacto> findAll() {
		return super.findAll();
	}
	
	@GET
	@Override
	@Path("propietario/{owner}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Contacto> findAllByOwner(@PathParam("owner") String owner) {
		return super.findAllByOwner(owner);
	}
	
	@GET
	@Path("{from}/{to}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Contacto> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
		return super.findRange(from, to); // Utilizamos varargs
	}
	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String countREST() {
		return String.valueOf(super.count());
	}
	
	@Override
	protected EntityManager getEntityManager() {
		if( em == null ) {
			EntityManagerFactory factory = Persistence.createEntityManagerFactory("agendaweb");
			em = factory.createEntityManager();
		}
		return em;
	}
	
	
	
	
	
	

}

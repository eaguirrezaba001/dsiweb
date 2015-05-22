package mx.vainiyasoft.agendaweb.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * The persistent class for the CONTACTO database table.
 * 
 */
@Entity
@Table(name="CONTACTO")
@NamedQueries({
	@NamedQuery(name="Contacto.findAll", query="SELECT c FROM Contacto c"),
	@NamedQuery(name="Contacto.findByPropietario", query="SELECT c FROM Contacto c WHERE c.propietario = :propietario")
})
public class Contacto implements Serializable, AbstractEntity<Contacto> {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=36)
	private String uuid;

	@Column(nullable=false)
	private Timestamp actualizado;

	@Column(nullable=false)
	private BigInteger androidId;

	@Column(nullable=false)
	private Timestamp creado;

	@Column(length=255)
	private String direccion;

	@Column(length=255)
	private String email;

	@Column(length=255)
	private String imageUri;

	@Column(nullable=false, length=255)
	private String nombre;

	@Column(nullable=false, length=45)
	private String propietario;

	@Column(length=45)
	private String telefono;

	public Contacto() {
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Timestamp getActualizado() {
		return this.actualizado;
	}

	public void setActualizado(Timestamp actualizado) {
		this.actualizado = actualizado;
	}

	public BigInteger getAndroidId() {
		return this.androidId;
	}

	public void setAndroidId(BigInteger androidId) {
		this.androidId = androidId;
	}

	public Timestamp getCreado() {
		return this.creado;
	}

	public void setCreado(Timestamp creado) {
		this.creado = creado;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImageUri() {
		return this.imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPropietario() {
		return this.propietario;
	}

	public void setPropietario(String propietario) {
		this.propietario = propietario;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	@Override
	public void merge(Contacto entity) {
		this.uuid = entity.uuid;
		this.androidId = entity.androidId;
		this.creado = entity.creado;
		this.actualizado = entity.actualizado;
		this.nombre = entity.nombre;
		this.direccion = entity.direccion;
		this.email = entity.email;
		this.imageUri = entity.imageUri;
		this.propietario = entity.propietario;
		this.telefono = entity.telefono;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creado == null) ? 0 : creado.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contacto other = (Contacto) obj;
		if (creado == null) {
			if (other.creado != null)
				return false;
		} else if (!creado.equals(other.creado))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
	
	

}
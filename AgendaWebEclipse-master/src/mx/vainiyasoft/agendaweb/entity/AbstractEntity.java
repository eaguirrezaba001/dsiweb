package mx.vainiyasoft.agendaweb.entity;

@SuppressWarnings("rawtypes")
public interface AbstractEntity<E extends AbstractEntity> {
	public void merge(E entity);
}

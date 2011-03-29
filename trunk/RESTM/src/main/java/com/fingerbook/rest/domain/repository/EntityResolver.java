package com.fingerbook.rest.domain.repository;

public interface EntityResolver {
    /**
     * Busca una entidad
     * @param type El tipo de entidad
     * @param id El id de la entidad
     * @return La entidad requerida
     */
    public <T> T findById(Class<T> type, Long id);
    
    /**
     * @param object La entidad a buscar
     * @return El id de la entidad
     * @throws IllegalStateException Si el objeto no es persistente
     */
    public Long getId(Object o);
}

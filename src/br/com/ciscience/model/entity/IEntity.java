package br.com.ciscience.model.entity;

public interface IEntity {

	/**
	 * Implemente este método para validar se os dados estão nulos ou vazios
	 * 
	 * @return {@link Boolean}
	 */
	public boolean validateEmptyFields();

}

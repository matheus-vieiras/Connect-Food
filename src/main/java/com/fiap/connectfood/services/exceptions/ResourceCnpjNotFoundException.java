package com.fiap.connectfood.services.exceptions;

public class ResourceCnpjNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceCnpjNotFoundException(Object cnpj) {
        super("Resource not found. CNPJ " + cnpj);
    }

}

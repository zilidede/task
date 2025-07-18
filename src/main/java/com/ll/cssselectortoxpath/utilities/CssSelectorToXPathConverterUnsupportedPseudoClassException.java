package com.ll.cssselectortoxpath.utilities;

public class CssSelectorToXPathConverterUnsupportedPseudoClassException extends CssSelectorToXPathConverterException {

    private static final long serialVersionUID = 1L;
    private static final String PSEUDO_CLASS_UNSUPPORTED_ERROR_FORMAT = "Unable to convert \"%s\". A converter for CSS Seletor Pseudo-Classes has not been implement at this time. TODO: A future capability.";

    public CssSelectorToXPathConverterUnsupportedPseudoClassException(String pseudoClass) {
        super(getPseudoClassUnsupportedError(pseudoClass));
    }

    public static String getPseudoClassUnsupportedError(String pseudoClass) {
//		System.out.println(pseudoClass);
        return String.format(PSEUDO_CLASS_UNSUPPORTED_ERROR_FORMAT, pseudoClass);

    }
}

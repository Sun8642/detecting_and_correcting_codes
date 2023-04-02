package model;

import lombok.Getter;
import math.BigInt;

import java.math.BigInteger;

@Getter
public class CyclicRedundancyCodeParameter extends CodeParameter {

    private BigInt generatorPolynomial = new BigInt(Long.parseLong("1011", 2));

    public void setGeneratorPolynomial(String generatorPolynomial) {
        if (generatorPolynomial == null || generatorPolynomial.length() == 0) {
            throw new IllegalArgumentException(ProgramParameter.ERROR_POLYNOMIAL_GENERATOR_MIN_VALUE);
        }
        generatorPolynomial = generatorPolynomial.trim();
        int oneCount = 0;
        for (int i = 0; i < generatorPolynomial.length(); i++) {
            if (generatorPolynomial.charAt(i) != '0' && generatorPolynomial.charAt(i) != '1') {
                throw new IllegalArgumentException("The polynomial generator can only contain 1s and 0s as characters.");
            }
            if (generatorPolynomial.charAt(i) == '1') {
                oneCount++;
            }
        }
        if (oneCount == 0 || (oneCount == 1 && generatorPolynomial.charAt(generatorPolynomial.length() - 1) == '1')) {
            throw new IllegalArgumentException(ProgramParameter.ERROR_POLYNOMIAL_GENERATOR_MIN_VALUE);
        }
        this.generatorPolynomial = BigInt.from(new BigInteger(generatorPolynomial, 2));
    }
}

package com.odonto.odonto_system.shared.util;

public class CpfValidator {

    public static boolean isValid (String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int[] numbers = new int[11];
            for (int i = 0; i < 11; i++) {
                numbers[i] = Character.getNumericValue(cpf.charAt(i));
            }

            int sum1 = 0, sum2 = 0;
            for (int i = 0; i < 9; i++) {
                sum1 += numbers[i] * (10 - i);
                sum2 += numbers[i] * (11 - i);
            }

            int dv1 = (sum1 % 11 < 2) ? 0 : 11 - (sum1 % 11);
            sum2 += dv1 * 2;
            int dv2 = (sum2 % 11 < 2) ? 0 : 11 - (sum2 % 11);

            return numbers[9] == dv1 && numbers[10] == dv2;
        } catch (Exception e) {
            return false;
        }
    }

}

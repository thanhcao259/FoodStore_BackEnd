package com.example.store.util;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.*;
import java.math.BigDecimal;
import java.text.NumberFormat;

@Component
public class CurrencyUtils {

    public String formatCurrency(double price){

        DecimalFormat fmt  = new DecimalFormat("#,### VND");
        String rs = fmt.format(price);
        return rs;

    }
}

package control.invest.IC.utilities;

import java.text.DecimalFormat;

public class Utilities {
public String formatarValor(double valor){
    DecimalFormat decimalFormat = new DecimalFormat("#.00");

    return decimalFormat.format(valor);

}
}



import org.junit.Test;

import java.math.RoundingMode;
import java.text.DecimalFormat;


public class Testhhh {


    @Test
    public void map() {

//        Map<Integer, String> map = new HashMap<>();
//        map.put(2, "cnm2");
//        System.out.println(map.computeIfAbsent(1, k -> "cnm".concat(",hh")));
//        System.out.println(map.computeIfAbsent(2, k -> "cnm".concat(",hh")));
//        System.out.println(map.computeIfPresent(2, (k, ov) -> "cnm".concat(",hh")));
//        System.out.println(map.compute(2, (k, ov) -> ov.concat(",hh")));
//        System.out.println(map.merge(2, "newValue", (ov, nv) -> nv.concat(ov)));
        double a = 2.555;
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        System.out.println(df.format(a));
        System.out.println(String.format("%.2f",a));

    }

}

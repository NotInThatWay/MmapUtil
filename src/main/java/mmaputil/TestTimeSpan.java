package mmaputil;

import mmaputil.read.rule.ReadRule;
import mmaputil.read.rule.TimeSpan;

import java.util.Date;

public class TestTimeSpan {
    public static void main(String[] args) throws Exception {
        Date start = new Date(System.currentTimeMillis()-100000);
        Date end = new Date();
        ReadRule rule = new TimeSpan("yyyy-MM-dd-HH-mm-ssZ", start, end,1000);


    }
}

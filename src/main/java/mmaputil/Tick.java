package mmaputil;

import lombok.Data;

@Data
public class Tick {
    // 股票代码
    public String code;

    // 当前时间戳
    public long time;

    // 昨日收盘价
    public float last = 14.1f;

    // 开盘价
    public float open = 14.1f;

    // 今日最高点
    public float high = 14.1f;

    // 今日最低点
    public float low = 14.1f;

    // 实时价格
    public float price = 14.1f;

    // 成交量
    public long volT = 41000;

    // 成交金额
    public float amtT = 578100.0f;

    // 成交量增量
    public long vol = 41000;

    public String id;

    public long outsideVol = 0;
    public long insideVol = 41000;
    public float amt = 578100.0f;
    public int num = 0;
    public int numT = 0;
    public float wb = -0.43f;
    public String bs = "B";

    public float buyAmt = 137370.3f;
    public float sellAmt = 440729.7f;
    public String phase = "T111";
    public int sn1 = 2500;
    public int sn2 = 13300;
    public int sn3 = 13300;
    public int sn4 = 15900;
    public int sn5 = 72300;
    public int bn1 = 29000;
    public int bn2 = 3100;
    public int bn3 = 2800;
    public int bn4 = 2000;
    public int bn5 = 9800;
    public float sp1 = 14.11f;
    public float sp2 = 14.12f;
    public float sp3 = 14.13f;
    public float sp4 = 14.14f;
    public float sp5 = 14.15f;
    public float bp1 = 14.1f;
    public float bp2 = 14.09f;
    public float bp3 = 14.08f;
    public float bp4 = 14.07f;
    public float bp5 = 14.06f;

    public Tick(String code, long time){
        this.code = code;
        this.time = time;
    }
}
